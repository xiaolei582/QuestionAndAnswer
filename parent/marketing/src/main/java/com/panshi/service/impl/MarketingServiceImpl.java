package com.panshi.service.impl;

import com.panshi.config.ThreadPool;
import com.panshi.domain.*;
import com.panshi.dta.Message;
import com.panshi.exception.CustomizeException;
import com.panshi.mapper.CommodityMapper;
import com.panshi.mapper.MarketingMapper;
import com.panshi.mapper.OrderFormMapper;
import com.panshi.service.MarketingService;
import com.panshi.service.MemberService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.zookeeper.KeeperException;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author huangxiaolei
 * @description 营销接口实现类
 * @create 2019/07/20
 */
@Service(version = "1.0.0")
public class MarketingServiceImpl implements MarketingService {

    @Autowired
    MarketingMapper marketingMapper;

    @Autowired
    private CommodityMapper commodityMapper;

    @Autowired
    private OrderFormMapper orderFormMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Reference(version = "1.0.0")
    MemberService memberService;
    
    @Value("${adopt-an-idea.active-time-end}")
    private String activeTimeEnd;

    @Value("${adopt-an-idea.integral}")
    private long integral;

    @Override
    public void questionnaireAdd(QuaysQuestionnaire quaysQuestionnaire) {
        marketingMapper.questionnaireAdd(quaysQuestionnaire);
    }

    @Override
    public QuaysQuestionnaire questionnaireQuery(long id) {
        return marketingMapper.questionnaireQuery(id);
    }

    @Override
    @Transactional
    public void questionnaireContentAdd(List<QuestionnaireContent> qusysQdetail) {
        for (QuestionnaireContent questionnaireContent : qusysQdetail) {
            QuestionnaireContent questionnaireContent1 = contentQuery(questionnaireContent.getQuestion());
            //判断问题是否重复
            if(!StringUtils.isEmpty(questionnaireContent1)){
                throw new CustomizeException(500,"问题重复");
            }
            marketingMapper.questionnaireContentAdd(questionnaireContent);
        }
    }

    @Override
    public List<QuestionnaireContent> contentQueryAll(long id) {
        return marketingMapper.contentQueryAll(id);
    }

    @Override
    public QuestionnaireContent contentQuery(String question) {
        return marketingMapper.contentQuery(question);
    }

    @KafkaListener(topics = "activity")
    public void listen(ConsumerRecord<?, ?> cr) throws ParseException {
        if("adoptAnIdea".equals(cr.key())){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //拆分时间段
            String[] split = activeTimeEnd.split("@");
            System.out.println(Arrays.toString(split));
            //活动开始时间
            long startTime = simpleDateFormat.parse(split[0]).getTime();
            //活动结束时间
            long endTime = simpleDateFormat.parse(split[1]).getTime();
            //当前时间
            long currentTime = System.currentTimeMillis();

            if(currentTime >= startTime && currentTime <= endTime){
                QuaysQuestion quaysQuestion = new QuaysQuestion(Long.valueOf(cr.value() + ""), integral);
                //赠送活动积分
                memberService.plus(quaysQuestion);
            }

        }
    }


    /**
     *  减库存、扣积分
     * @param userId
     * @param commodityId
     * @return
     */
    @Override
    public Message seckill(long userId, long commodityId) throws KeeperException, InterruptedException {
        //判断商品信息
        Commodity commodity = commodityMapper.commodityIdQuery(commodityId);

        //扣除积分
        memberService.subtract(new QuaysQuestion(userId,commodity.getCommodityIntegral()));

        //减库存
        int count = commodityMapper.inventoryReduction(commodityId,commodity.getVersion());

        //加乐观锁和版本号是为了防止超卖的情况
        if(count == 0){
            //发送kafka回退积分
            kafkaTemplate.send("refund","{mid:"+userId+",integral:"+commodity.getCommodityIntegral()+"}");
            throw new CustomizeException(500,"版本号不一致");
        }

        //订单记录
        orderFormMapper.insert(commodityId,userId);

        return new Message<>(200,"兑换成功");
    }


    /**
     * 兑换限流
     * @param userId
     * @param commodityId
     * @return
     */
    @Override
    public Message exchangeLimiting(long userId, long commodityId){
        QuaysMember quaysMember = memberService.userIdQuery(userId);

        //判断用户是否存在
        if (StringUtils.isEmpty(quaysMember)){
            throw new CustomizeException(500,"用户不存在");
        }

        //判断商品信息
        Commodity commodity = commodityMapper.commodityIdQuery(commodityId);
        if (StringUtils.isEmpty(commodity)){
            throw new CustomizeException(500,"没有该商品");
        }

        if (commodity.getCommodityNumber() <= 0){
            throw new CustomizeException(500,"商品已抢空");
        }

        //判断用户积分是否足够
        if(quaysMember.getUserIntegral() < commodity.getCommodityIntegral()){
            throw new CustomizeException(500,"您的积分不足");
        }

        OrderForm orderForm = orderFormMapper.userIdAndCommodityIdOrderFormQuery(userId, commodityId);

        //判断用户积分是否足够
        if(!StringUtils.isEmpty(orderForm)){
            throw new CustomizeException(500,"您已兑换过该商品");
        }

        String status = redisTemplate.opsForValue().get("active:came:" + userId);
        if(status == null){
            boolean exchange = queueExchange(userId,commodityId);

            //判断排队状况
            if(exchange){
                //排队成功信息保存一天
                redisTemplate.opsForValue().set("active:came:" + userId,"2",1, TimeUnit.DAYS);
                status = "2";
            }else {
                //排队失败信息保存一秒
                redisTemplate.opsForValue().set("active:came:" + userId,"1",1, TimeUnit.SECONDS);
                status = "1";
            }

        }

        //1 表示已经在排队了  2 表示排队成功
        Message<Object> message = new Message<>();
        if("2".equals(status)){
            message.setCode(200);
            message.setMessage("排队成功");
        }

        if("1".equals(status)){
            message.setCode(300);
            message.setMessage("正在处理");
        }

        return message;
    }

    /**
     * 兑换排队
     * @param userId
     * @param commodityId
     * @return
     */
    public boolean queueExchange(long userId, long commodityId){
        //获取排队的长度
        long size = redisTemplate.opsForList().size("active:exchange");
        if(size >= 200){
            return false;
        }

        //给需要兑换的用户排队,向list右边插入
        redisTemplate.opsForList().rightPush("active:exchange",userId+":"+commodityId);
        return true;
    }


    /**
     * 兑换
     */
    @Scheduled(cron = "0/30 * * * * *")
    public void exchange() {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        //获取15个在排队的兑换用户
        List<String> range = listOperations.range("active:exchange", 0, 15);

        ThreadPoolExecutor executorService = ThreadPool.getExecutorService();

        for (String ex : range) {
            //多线程执行
            executorService.submit(()->{
                //删除处理过的兑换请求
                listOperations.remove("active:exchange",1,ex);

                String[] split = ex.split(":");
                //兑换请求
                try {
                    seckill(Long.valueOf(split[0]),Long.valueOf(split[1]));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });
        }
    }

}

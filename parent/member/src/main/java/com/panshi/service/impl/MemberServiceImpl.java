package com.panshi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.panshi.domain.QuaysIntegral;
import com.panshi.service.QuestionService;
import com.panshi.domain.QuaysMember;
import com.panshi.domain.QuaysQuestion;
import com.panshi.dta.Message;
import com.panshi.exception.CustomizeException;
import com.panshi.mapper.MemberMapper;
import com.panshi.service.MemberService;
import com.panshi.utils.Md5Util;
import com.panshi.utils.ZookeeperLock;
import lombok.extern.log4j.Log4j2;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
* @description 会员接口实现类
* @author huangxiaolei
* @create 2019-07-19
*/
@CacheConfig(cacheNames = {"myCache"})
@Service(version = "1.0.0")
@Log4j2
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Reference(version = "1.0.0")
    QuestionService questionService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public Message add(String username, String password){

        //查询用户账号
        QuaysMember userQuery = usernameQuery(username);

        //判断账号是否存在
        if (!StringUtils.isEmpty(userQuery)){
            throw new CustomizeException(500,"账号已经存在");
        }

        QuaysMember quaysMember = new QuaysMember(username, password);


        //密码加密
        String md5Code = Md5Util.getMd5Code(quaysMember.getPassword());

        //加密后的密码加上用户名再次进行加密
        md5Code = Md5Util.getMd5Code(md5Code + quaysMember.getUsername());

        quaysMember.setPassword(md5Code);

        memberMapper.add(quaysMember);

        return new Message(200,"注册成功");
    }

    @Override
    public QuaysMember usernameQuery(String username){
        return memberMapper.usernameQuery(username);
    }

    @Override
    public QuaysMember userIdQuery(long userId){
        return memberMapper.userIdQuery(userId);
    }

    @Override
    public QuaysMember integralQuery(String id){
       return memberMapper.integralQuery(id);
    }

    @Override
    public void plus(QuaysQuestion quaysQuestion) {
        //加积分
        memberMapper.plus(quaysQuestion);

        //保存流水
        QuaysIntegral quaysIntegral = new QuaysIntegral(quaysQuestion.getMid(),quaysQuestion.getIntegral(),"1");
        questionService.integralFlow(quaysIntegral);
    }

    @Override
    public void subtract(QuaysQuestion quaysQuestion) throws KeeperException, InterruptedException {

        ZookeeperLock zookeeperLock = null;
        try {
            zookeeperLock = new ZookeeperLock("/subtract" + quaysQuestion.getMid());

            //获取锁
            boolean lock = zookeeperLock.getLock();

            if (lock){
                //减积分
                memberMapper.subtract(quaysQuestion);

                //保存流水
                QuaysIntegral quaysIntegral = new QuaysIntegral(quaysQuestion.getMid(),-quaysQuestion.getIntegral(),"2");
                questionService.integralFlow(quaysIntegral);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //释放锁
            zookeeperLock.deleteLock();
        }
        
    }

    @KafkaListener(topics = "refund")
    public void refund(ConsumerRecord cr){
        Object value = cr.value();
        QuaysQuestion quaysQuestion = JSONObject.toJavaObject(JSON.parseObject(value.toString()),QuaysQuestion.class);

        //加积分
        memberMapper.plus(quaysQuestion);

        //保存流水
        QuaysIntegral quaysIntegral = new QuaysIntegral(quaysQuestion.getMid(),quaysQuestion.getIntegral(),"3");
        questionService.integralFlow(quaysIntegral);
    }

    @Override
    public List<QuaysMember> ranking(){
        //获取string对象
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        //查询key中的value
        String leaderboard = valueOperations.get("leaderboard");
        //判断value是否为空
        if(leaderboard != null){
            //转json数组
            JSONArray objects = JSONObject.parseArray(leaderboard);
            //转Java集合
            List<QuaysMember> list = JSONArray.parseArray(objects.toString(), QuaysMember.class);
            return list;
        }

        //当redis中没有数据缓存时就从数据库中查询
        List<QuaysMember> ranking = memberMapper.ranking();
        //数据保存到redis缓存中并且只保存10 秒
        valueOperations.set("leaderboard",JSON.toJSONString(ranking),10, TimeUnit.SECONDS);
        return ranking;
    }

    @Override
    @Cacheable(key = "'user:'+#userId" )
    public List<QuaysMember> dataCache(long userId){
        List<QuaysMember> ranking = memberMapper.ranking();
        return ranking;
    }


}

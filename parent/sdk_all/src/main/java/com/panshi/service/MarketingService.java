package com.panshi.service;

import com.panshi.domain.QuaysQuestionnaire;
import com.panshi.domain.QuestionnaireContent;
import com.panshi.dta.Message;
import org.apache.dubbo.config.annotation.Service;
import org.apache.zookeeper.KeeperException;

import java.util.List;

/**
 * @description 营销接口
 * @author huangxiaolei
 * @create 2019/07/18
 */
public interface MarketingService {

    /**
     *  增加问卷调查
     * @param quaysQuestionnaire
     */
    void questionnaireAdd(QuaysQuestionnaire quaysQuestionnaire);

    /**
     * 根据问卷表id查询
     * @param id
     * @return
     */
    QuaysQuestionnaire questionnaireQuery(long id);

    /**
     *  增加问卷中的问题
     * @param qusysQdetail
     */
    void questionnaireContentAdd(List<QuestionnaireContent> qusysQdetail);

    /**
     *  通过问卷id查询问卷内容
     * @param id
     * @return
     */
    List<QuestionnaireContent> contentQueryAll(long id);

    /**
     *  通过问卷内容查询
     * @param question
     * @return
     */
    QuestionnaireContent contentQuery(String question);

    /**
     *  兑换积分
     * @param userId
     * @param commodityId
     * @return
     * @throws InterruptedException
     */
    Message seckill(long userId, long commodityId) throws InterruptedException, KeeperException;

    /**
     * 兑换限流
     * @param userId
     * @param commodityId
     * @return
     */
    Message exchangeLimiting(long userId, long commodityId);
}

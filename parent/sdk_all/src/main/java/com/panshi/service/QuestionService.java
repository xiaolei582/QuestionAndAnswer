package com.panshi.service;


import com.panshi.domain.MatchKeywordsVO;
import com.panshi.domain.QuaysAnswer;
import com.panshi.domain.QuaysIntegral;
import com.panshi.domain.QuaysQuestion;
import com.panshi.dta.Message;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.List;

/**
 * @description: 问答接口
 * @author huangxiaolei
 * @create: 2019/07/18
 */
public interface QuestionService {

    /**
     *  查询所有的问题
     * @return
     */
    List<QuaysQuestion> questionQueryAll();

    /**
     *  通过问题内容查询
     * @param question
     * @return
     */
    QuaysQuestion questionQuery(String question);

    /**
     *  保存发布的问题提
     * @param quaysQuestion
     * @return
     */
    void postQuestions(QuaysQuestion quaysQuestion);

    /**
     *  保存问题的答案
     * @param quaysAnswer
     */
    Message answer(QuaysAnswer quaysAnswer);

    /**
     *  采纳答案
     * @param quaysAnswer
     */
    void adoption(QuaysAnswer quaysAnswer);

    /**
     * 增加积分流水
     * @param quaysIntegral
     */
    void integralFlow(QuaysIntegral quaysIntegral);

    /**
     * 搜索关键字
     * @param question
     * @return
     * @throws IOException
     * @throws SolrServerException
     */
    List<MatchKeywordsVO> keywordQuery(String question) throws IOException, SolrServerException;
}

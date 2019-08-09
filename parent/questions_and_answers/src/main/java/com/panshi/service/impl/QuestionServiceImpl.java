package com.panshi.service.impl;

import com.panshi.domain.MatchKeywordsVO;
import com.panshi.domain.QuaysAnswer;
import com.panshi.domain.QuaysIntegral;
import com.panshi.domain.QuaysQuestion;
import com.panshi.dta.Message;
import com.panshi.exception.CustomizeException;
import com.panshi.mapper.QuestionMapper;
import com.panshi.service.MemberService;
import com.panshi.service.QuestionService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.MapSolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangxiaolei
 * @description 问题接口实现类
 * @create 2019/07/19
 */
@Service(version = "1.0.0")
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    QuestionMapper questionMapper;

    @Reference(version = "1.0.0")
    MemberService memberService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public List<QuaysQuestion> questionQueryAll() {
        List<QuaysQuestion> list = questionMapper.questionQueryAll();
        for (QuaysQuestion quaysQuestion : list) {
            //通过问题id查询问题下的所有答案
            List<QuaysAnswer> quaysAnswers = questionMapper.answerQuery(quaysQuestion.getQuestionId());
            quaysQuestion.setQuaysAnswerList(quaysAnswers);
        }
        return list;
    }

    @Override
    public QuaysQuestion questionQuery(String question) {
        //查询问题
        QuaysQuestion quaysQuestion = questionMapper.questionQuery(question);
        //通过问题id查询问题下的所有答案
        List<QuaysAnswer> list = questionMapper.answerQuery(quaysQuestion.getQuestionId());
        quaysQuestion.setQuaysAnswerList(list);
        return quaysQuestion;
    }

    @Override
    public void postQuestions(QuaysQuestion quaysQuestion){
        //保存发布的问题
        questionMapper.postQuestions(quaysQuestion);
        //扣除用户发布问题所需的积分
        memberService.subtract(quaysQuestion);
    }

    @Override
    public Message answer(QuaysAnswer quaysAnswer){
        //通过id查询问题信息
        QuaysQuestion quaysQuestion = questionMapper.questionIdQuery(quaysAnswer.getQid());
        //判断问题是否存
        if(StringUtils.isEmpty(quaysQuestion)){
            return new Message(200,"该问题已经不存在");
        }
        //保存答案
        questionMapper.answer(quaysAnswer);
        return new Message(200,"答案已发布成功，等待采纳");
    }

    @Override
    public void adoption(QuaysAnswer quaysAnswer){
        //查询答案的详细信息
        QuaysAnswer answer = questionMapper.answerIdQuery(quaysAnswer.getId());

        //通过问题表id和答案类型查询
        QuaysAnswer answer1 = questionMapper.answerTypeQuery(answer.getQid());

        //判断问题是否已经被采纳过了
        if(StringUtils.isEmpty(answer1)){
            throw new CustomizeException(500,"该问题已经有采纳过的答案");
        }

        //采纳答案
        questionMapper.adoption(quaysAnswer);

        //查询问题积分
        QuaysQuestion quaysQuestion = questionMapper.questionIdQuery(answer.getQid());

        //采纳问题后给用户加积分
        memberService.plus(new QuaysQuestion(answer.getMid(),quaysQuestion.getIntegral()));

        //发送采纳问题的消息
        kafkaTemplate.send("activity", "adoptAnIdea", answer.getMid()+"");
    }

    @Override
    public void integralFlow(QuaysIntegral quaysIntegral){
        questionMapper.integralFlow(quaysIntegral);
    }

    /**
     *  搜索关键字
     * @param question
     * @return
     */
    @Override
    public List<MatchKeywordsVO>  keywordQuery(String question) throws IOException, SolrServerException {
        HttpSolrClient httpSolrClient = new HttpSolrClient.Builder("http://localhost:8983/solr").build();
        Map<String, String> map = new HashMap<>();
        map.put("q","question:"+question);

        QueryResponse queryResponse = httpSolrClient.query("test", new MapSolrParams(map));
        List<MatchKeywordsVO> list = queryResponse.getBeans(MatchKeywordsVO.class);

        return list;
    }

}

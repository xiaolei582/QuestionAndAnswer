package com.panshi.mapper;

import com.panshi.domain.QuaysAnswer;
import com.panshi.domain.QuaysIntegral;
import com.panshi.domain.QuaysQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description 问题接口
 * @author huangxiaolei
 * @create 2019-07-19
 */
@Mapper
@Repository
public interface QuestionMapper {

    /**
     *  查询所有的问题
     * @return
     */
    List<QuaysQuestion> questionQueryAll();

    /**
     *  问题查询
     * @param question
     * @return
     */
    QuaysQuestion questionQuery(String question);

    /**
     *  发布的问题保存到数据库
     * @param quaysQuestion
     * @return 返回主键id
     */
    void postQuestions(QuaysQuestion quaysQuestion);

    /**
     *  保存问题的答案
     * @param quaysAnswer
     */
    void answer(QuaysAnswer quaysAnswer);

    /**
     *  根据id查询问题
     * @param id
     * @return
     */
    QuaysQuestion questionIdQuery(long id);


    /**
     *  通过问题表的id查询所有答案
     * @param id
     * @return
     */
    List<QuaysAnswer> answerQuery(long id);

    /**
     * 通过答案表id查询
     * @param id
     * @return
     */
    QuaysAnswer answerIdQuery(long id);

    /**
     * 采纳答案
     * @param quaysAnswer
     */
    void adoption(QuaysAnswer quaysAnswer);

    /**
     * 通过问题表id和答案类型查询
     * @param qid
     * @return
     */
    QuaysAnswer answerTypeQuery(long qid);

    /**
     * 增加积分流水
     * @param quaysIntegral
     */
    void integralFlow(QuaysIntegral quaysIntegral);
}

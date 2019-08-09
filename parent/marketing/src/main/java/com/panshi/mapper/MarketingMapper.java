package com.panshi.mapper;

import com.panshi.domain.QuaysQuestionnaire;
import com.panshi.domain.QuestionnaireContent;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @description 营销mapper
* @author huangxiaolei
* @create 2019-07-20
*/
@Mapper
@Repository
public interface MarketingMapper {

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
    void questionnaireContentAdd(QuestionnaireContent qusysQdetail);

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

}

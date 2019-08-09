package com.panshi.controller;

import com.panshi.domain.MatchKeywordsVO;
import com.panshi.domain.QuaysAnswer;
import com.panshi.domain.QuaysQuestion;
import com.panshi.dta.Message;
import com.panshi.service.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author huangxiaolei
 * @description 问题
 * @create 2019/07/19
 */
@RestController
@RequestMapping("/questionAndAnswerController")
@Api(value = "问答模块")
public class QuestionAndAnswerController {

    @Reference(version = "1.0.0")
    QuestionService questionService;

    @GetMapping("/questionQuery")
    @ApiOperation(value = "通过问题内容查询")
    public Message questionQuery(@RequestParam String question){
        QuaysQuestion quaysQuestion = questionService.questionQuery(question);
        return new Message(200,quaysQuestion);
    }


    @GetMapping("/questionQueryAll")
    @ApiOperation(value = "查询所有问题信息")
    public Message questionQueryAll(){
        List<QuaysQuestion> list = questionService.questionQueryAll();
        return new Message(200,list);
    }


    @PostMapping("/postQuestions")
    @ApiOperation(value = "发布问题")
    public Message postQuestions(@RequestBody QuaysQuestion quaysQuestion){
        questionService.postQuestions(quaysQuestion);
        return new Message(200,"发布成功");
    }

    @PostMapping("/answer")
    @ApiOperation(value = "回答问题")
    public Message answer(@RequestBody QuaysAnswer quaysAnswer){
        return questionService.answer(quaysAnswer);
    }

    @PostMapping("/adoption")
    @ApiOperation(value = "采纳问题")
    public Message adoption(@RequestBody QuaysAnswer quaysAnswer){
        questionService.adoption(quaysAnswer);
        return new Message(200,"答案已采纳");
    }

    @GetMapping("/matchKeywords")
    @ApiOperation(value = "搜索关键字")
    public Message matchKeywords(@RequestParam String keywords){
        List<MatchKeywordsVO> list = null;
        try {
            list = questionService.keywordQuery(keywords);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return new Message(200,list);
    }

}

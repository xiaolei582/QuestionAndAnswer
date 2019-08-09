package com.panshi.controller;

import com.panshi.domain.QuaysQuestion;
import com.panshi.domain.QuaysQuestionnaire;
import com.panshi.domain.QuestionnaireContent;
import com.panshi.dta.Message;
import com.panshi.service.MarketingService;
import com.panshi.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author huangxiaolei
 * @description 营销模块
 * @create 2019/07/19
 */
@RestController
@RequestMapping("/marketingController")
@Api(value = "营销模块")
public class MarketingController {

    @Value("${inviteIntegral}")
    private int inviteIntegral;

    @Reference(version = "1.0.0")
    private MemberService memberService;

    @Reference(version = "1.0.0")
    MarketingService marketingService;

    @GetMapping("/inviteCode/{userId}")
    @ApiOperation("产生邀请注册码")
    public Message inviteCode(@PathVariable long userId){
        String url = "http:/.....?userId="+userId;
        return new Message(200,url);
    }

    @GetMapping("/inviteRegister/{userId}/{username}/{password}")
    @ApiOperation("产生邀请注册码")
    public Message inviteRegister(@PathVariable long userId,@PathVariable String username,@PathVariable String password){
        //注册
        Message message = memberService.add(username, password);
        QuaysQuestion quaysQuestion = new QuaysQuestion(userId, inviteIntegral);
        //送积分
        memberService.plus(quaysQuestion);
        return message;
    }

    @PostMapping("/createQuestionnaire")
    @ApiOperation("创建问卷")
    public Message createQuestionnaire(@RequestBody QuaysQuestionnaire quaysQuestionnaire){
        marketingService.questionnaireAdd(quaysQuestionnaire);
        return new Message(200,"问卷已创建");
    }

    @PostMapping("/questionnaireContent")
    @ApiOperation("增加问卷中的内容")
    public Message questionnaireContentAdd(@RequestBody List<QuestionnaireContent> list){
        marketingService.questionnaireContentAdd(list);
        return new Message(200,"内容以添加");
    }

    @GetMapping("/spikeActivity/{userId}/{commodityId}")
    @ApiOperation("积分兑换")
    public Message spikeActivity(@PathVariable long userId,@PathVariable long commodityId) throws InterruptedException {
        return marketingService.exchangeLimiting(userId, commodityId);
    }

}

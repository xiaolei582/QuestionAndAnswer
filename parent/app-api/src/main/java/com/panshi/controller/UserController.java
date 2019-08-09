package com.panshi.controller;

import com.panshi.domain.QuaysMember;
import com.panshi.dta.Message;
import com.panshi.exception.CustomizeException;
import com.panshi.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
/**
 * @description 用户
 * @author huangxiaolei
 * @create 2019/07/18
 */
@RestController
@RequestMapping("/userController")
@Api(value = "会员模块")
public class UserController {

    @Reference(version = "1.0.0")
    MemberService memberService;


    /**
     * 会员注册
     * @param username
     * @param password
     * @return
     * @throws ParseException
     */
    @GetMapping("/register")
    @ApiOperation(value = "会员注册")
    public Message register(@RequestParam String username, @RequestParam String password) throws ParseException {
        //判断是否为空
        if (StringUtils.isEmpty(username) && StringUtils.isEmpty(password)){
            throw new CustomizeException(500,"请输入用户名和密码");
        }

        return memberService.add(username,password);
    }


    /**
     *  通过用户id查询用户积分流水明细
     * @param id
     * @return
     */
    @GetMapping("/integralQuery")
    @ApiOperation("查询用户积分流水")
    public Message<QuaysMember> integralQuery(@RequestParam String id){
        QuaysMember quaysMember = memberService.integralQuery(id);
        return new Message<>(200,quaysMember);
    }



    @GetMapping("/ranking")
    @ApiOperation("查询排行榜")
    public List<QuaysMember> ranking(){
        return memberService.ranking();
    }

    @RequestMapping("/dataCache/{userId}")
    @ApiOperation("spring缓存")
    public List<QuaysMember> dataCache(@PathVariable long userId){
         return memberService.dataCache(userId);
    }

}

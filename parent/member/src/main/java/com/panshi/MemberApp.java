package com.panshi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @description:会员程序入口
 * @author:huangxiaolei
 * @create:2019/07/18
 */
@SpringBootApplication
@EnableCaching
public class MemberApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(MemberApp.class,args);
    }
}

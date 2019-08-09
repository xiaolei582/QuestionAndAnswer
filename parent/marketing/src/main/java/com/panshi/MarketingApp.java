package com.panshi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
* @description App
* @author huangxiaolei
* @create 2019-07-19
 *
 *  EnableScheduling开启定时任务注解
*/
@EnableScheduling
@SpringBootApplication
public class MarketingApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(MarketingApp.class,args);
    }
}

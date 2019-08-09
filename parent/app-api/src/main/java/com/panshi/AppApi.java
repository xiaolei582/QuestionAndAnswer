package com.panshi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * app启动类
* @description: AppApi
* @author: 黄小磊
* @create: 2019-07-19
*/
@SpringBootApplication
@EnableSwagger2
public class AppApi
{
    public static void main( String[] args )
    {
        SpringApplication.run(AppApi.class,args);
    }
}


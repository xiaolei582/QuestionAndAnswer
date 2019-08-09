package com.panshi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
* @description Swagger2配置类
* @author huangxiaolei
* @create 2019-07-19
*/
@Configuration
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //扫描包下所有加过@Api注解的类
                .apis(RequestHandlerSelectors.basePackage("com.panshi.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("swagger")
                .description("描述信息")
                //设置服务器的端口和访问路径
                .termsOfServiceUrl("http://localhost:8088/swagger-ui.html")
                .version("1.0")
                .build();
    }

}

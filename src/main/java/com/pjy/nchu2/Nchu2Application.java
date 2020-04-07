package com.pjy.nchu2;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.pjy.nchu2.controller.server.NettyServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableKnife4j
@SpringBootApplication
@Import(BeanValidatorPluginsConfiguration.class)
@MapperScan(basePackages = "com.pjy.nchu2.mapper")
public class Nchu2Application {

    public static void main(String[] args) {

        SpringApplication.run(Nchu2Application.class, args);
        try {
            new NettyServer(12345).start();
            System.out.println("https://blog.csdn.net/moshowgame");
            System.out.println("http://127.0.0.1:6688/netty-websocket/index");
        }catch(Exception e) {
            System.out.println("NettyServerError:"+e.getMessage());
        }
    }

    @Bean
    public Docket communityApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.pjy.nchu2.controller"))
                .paths(PathSelectors.any())
                .build();
    }

}

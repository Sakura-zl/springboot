package com.sakura.pmp.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author : lzl
 * @date : 21:32 2019/12/1
 */
@SpringBootApplication
@MapperScan(basePackages = "com.sakura.pmp.model.mapper")
public class MainApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MainApplication.class);
    }

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext run = SpringApplication.run(MainApplication.class, args);
    }
}

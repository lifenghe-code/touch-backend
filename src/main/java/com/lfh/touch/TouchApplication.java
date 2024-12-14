package com.lfh.touch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.lfh.touch.mapper")
@ServletComponentScan // 扫描过滤器

public class TouchApplication {

    public static void main(String[] args) {

        SpringApplication.run(TouchApplication.class, args);
        // 开启
//        new Thread(() -> {
//            NettyServer server = new NettyServer();
//            try {
//                server.start();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }).start();
    }

}

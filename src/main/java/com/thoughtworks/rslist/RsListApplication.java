package com.thoughtworks.rslist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:application_bean.xml")
public class RsListApplication {

    public static void main(String[] args) {
        SpringApplication.run(RsListApplication.class, args);
    }

}

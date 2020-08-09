package com.thoughtworks.rslist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ImportResource("classpath:application_bean.xml")
@ComponentScan("com.*")
@EnableJpaRepositories("com.thoughtworks.rslist.repository")
public class RsListApplication {

    public static void main(String[] args) {
        SpringApplication.run(RsListApplication.class, args);
    }

}

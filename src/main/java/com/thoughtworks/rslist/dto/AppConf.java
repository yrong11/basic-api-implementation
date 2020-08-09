package com.thoughtworks.rslist.dto;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.service.UserService;
import com.thoughtworks.rslist.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import javax.annotation.Resource;
import javax.annotation.Resources;

@ComponentScan("com.thoughtworks.rslist.repository")
@Configuration
//@ImportResource("classpath:application_bean.xml")
public class AppConf {

    @Autowired(required = false)
    RsEventRepository rsEventRepository;
    @Autowired(required = false)
    UserRepository userRepository;


    @Bean
    public RsEventService rsEventService(){
        return new RsEventService(userRepository, rsEventRepository);
    }

    @Bean
    public UserService userService(){
        return new UserService(userRepository);
    }
//
//    @Bean
//    public VoteService voteService(){
//        return new VoteService();
//    }
}

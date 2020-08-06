package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@ComponentScan(basePackages = { "com.*"})
public class UserController {
    public static List<User> userList;
    @Autowired
    UserService userService;

    @PostMapping("/user")
    public ResponseEntity registerUser(@RequestBody @Validated User user){

        userService.registerUser(user);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/users")
    public ResponseEntity getUsers(){
        return ResponseEntity.ok(userList);
    }

}

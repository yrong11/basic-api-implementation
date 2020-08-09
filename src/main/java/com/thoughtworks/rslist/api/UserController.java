package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.AppConf;
import com.thoughtworks.rslist.exception.UserIndexNotValidException;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ComponentScan(basePackages = { "com.*"})
public class UserController {
    public static List<User> userList;
    ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConf.class);
    public UserService userService = ctx.getBean(UserService.class);

    @PostMapping("/user")
    public ResponseEntity registerUser(@RequestBody @Validated User user){

        userService.registerUser(user);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/users")
    public ResponseEntity getUsers(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size){

        return ResponseEntity.ok(userService.getUsers(page, size));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getUser(@PathVariable int id) throws UserIndexNotValidException {
        User user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable int id) throws UserIndexNotValidException{
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}

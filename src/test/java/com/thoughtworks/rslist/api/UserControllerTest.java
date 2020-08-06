package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    User user;
    UserDto userDto;

    @BeforeEach
    void setup(){
        UserController.userList = new ArrayList<>();
        user = new User("yurong1", "femal", 22, "a@b.com", "13277145678");
        User user2 = new User("libai", "male", 24, "b@a.com", "14277145678");
        User user3 = new User("dufu", "male", 24, "b@a.com", "15277145678");

        UserController.userList.add(user);
        UserController.userList.add(user2);
        UserController.userList.add(user3);
        userRepository.deleteAll();


    }

    public int initAddUser(){
        userDto = UserDto.builder().name(user.getName()).gender(user.getGender()).age(user.getAge())
                .email(user.getEmail()).phone(user.getPhone()).voteNum(user.getVoteNum()).build();
        userRepository.save(userDto);
        return userDto.getId();
    }

    @Test
    public void should_register_user() throws Exception {
        User user = new User("yurong", "femal", 23, "yurong@gmail.com", "11321111111");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<UserDto> userDtos = userRepository.findAll();
        assertEquals(1, userDtos.size());
        assertEquals("yurong", userDtos.get(0).getName());
        assertEquals(user.getEmail(),userDtos.get(0).getEmail());

    }

    @Test
    public void name_should_not_null() throws Exception {
        User user = new User("", "femal", 23, "yurong@gmail.com", "1321111111");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    public void name_size_should_less_than_8() throws Exception {
        User user = new User("yurongyur", "femal", 23, "yurong@gmail.com", "1321111111");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    public void age_should_between_18_and_100() throws Exception {
        User user = new User("yurong", "femal", 11, "yurong@gmail.com", "1321111111");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    public void email_should_conform_specification_format() throws Exception {
        User user = new User("yurong", "femal", 19, "yurong.com", "1321111111");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    public void phone_length_should_be_11() throws Exception {
        User user = new User("yurong", "femal", 19, "yurong@gmail.com", "132111111");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    public void phone_should_begin_with_1() throws Exception {
        User user = new User("yurong", "femal", 19, "yurong@gmail.com", "3321111101");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }


    @Test
    public void should_return_json_when_get_user() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$[0].user_name", is("yurong1")))
                .andExpect(jsonPath("$[0].user_age", is(22)))
                .andExpect(jsonPath("$[0].user_gender", is("femal")))
                .andExpect(jsonPath("$[0].user_email", is("a@b.com")))
                .andExpect(jsonPath("$[0].user_phone", is("13277145678")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_user() throws Exception {
        int userId = initAddUser();
        mockMvc.perform(get("/user/"+userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_name", is("yurong1")));
    }

    @Test
    public void should_delete_user() throws Exception{
        int userId = initAddUser();
        mockMvc.perform(delete("/user/delete/"+userId))
                .andExpect(status().isOk());
        userRepository.deleteById(userId);
        userRepository.deleteAll();
        List<UserDto> userDtos = userRepository.findAll();
        assertEquals(0, userDtos.size());
    }
    
}
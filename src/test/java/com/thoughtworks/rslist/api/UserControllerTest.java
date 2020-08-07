package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
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
    @Autowired
    RsEventRepository rsEventRepository;
    User user;
    UserDto userDto;


    @BeforeEach
    void setup(){
        user = new User("yurong1", "femal", 22, "a@b.com", "13277145678", 10);
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
    }

    public int addUserToDatabase(){
        userDto = UserDto.builder().name(user.getName()).gender(user.getGender()).age(user.getAge())
                .email(user.getEmail()).phone(user.getPhone()).voteNum(user.getVoteNum()).build();
        userRepository.save(userDto);
        return userDto.getId();
    }

    public int addRsEventToDatabase(int userId){
        RsEvent rsEvent = new RsEvent("eventName", "keyword", userId);
        UserDto userDto1 = userRepository.findById(userId).get();
        RsEventDto rsEventDto = RsEventDto.builder().userDto(userDto1).keyword(rsEvent.getKeyword())
                .eventName(rsEvent.getEventName()).build();
        rsEventRepository.save(rsEventDto);
        return rsEventDto.getId();
    }

    @Test
    public void should_register_user() throws Exception {
        User user = new User("yurong", "femal", 23, "yurong@gmail.com", "11321111111", 10);
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
        User user = new User("", "femal", 23, "yurong@gmail.com", "1321111111", 10);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    public void name_size_should_less_than_8() throws Exception {
        User user = new User("yurongyur", "femal", 23, "yurong@gmail.com", "1321111111", 10);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    public void age_should_between_18_and_100() throws Exception {
        User user = new User("yurong", "femal", 11, "yurong@gmail.com", "1321111111", 10);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    public void email_should_conform_specification_format() throws Exception {
        User user = new User("yurong", "femal", 19, "yurong.com", "1321111111", 10);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    public void phone_length_should_be_11() throws Exception {
        User user = new User("yurong", "femal", 19, "yurong@gmail.com", "132111111", 10);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    public void phone_should_begin_with_1() throws Exception {
        User user = new User("yurong", "femal", 19, "yurong@gmail.com", "3321111101", 10);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }


    @Test
    public void should_return_json_when_get_users() throws Exception {
        addUserToDatabase();
        user.setName("yurong2");addUserToDatabase();
        mockMvc.perform(get("/users?page=1&size=1"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].user_name", is("yurong1")))
                .andExpect(jsonPath("$[0].user_age", is(22)))
                .andExpect(jsonPath("$[0].user_gender", is("femal")))
                .andExpect(jsonPath("$[0].user_email", is("a@b.com")))
                .andExpect(jsonPath("$[0].user_phone", is("13277145678")))
                .andExpect(status().isOk());
    }

    @Test
    public void should_get_user() throws Exception {
        int userId = addUserToDatabase();
        mockMvc.perform(get("/user/"+userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_name", is("yurong1")));
    }

    @Test
    public void should_delete_user() throws Exception{
        int userId = addUserToDatabase();
        mockMvc.perform(delete("/user/delete/"+userId))
                .andExpect(status().isOk());
        List<UserDto> userDtos = userRepository.findAll();
        assertEquals(0, userDtos.size());
    }

    @Test
    public void should_delete_rs_event_when_delete_create_event_user() throws Exception {
        int userId = addUserToDatabase();
        int rsEventId = addRsEventToDatabase(userId);
        List<RsEventDto> rsEventDtos = rsEventRepository.findAll();
        assertEquals(1, rsEventDtos.size());
        mockMvc.perform(delete("/user/delete/"+userId))
                .andExpect(status().isOk());
        List<UserDto> userDtos = userRepository.findAll();
        assertEquals(0, userDtos.size());
        assertEquals(0, rsEventRepository.findAll().size());
    }

}
package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.awt.*;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class RsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup(){
        RsController.rsList = new ArrayList<>();
        UserController.userList = new ArrayList<>();
        User user1 = new User("yurong1", "femal", 22, "a@b.com", "13277145678");
        User user2 = new User("libai", "male", 24, "b@a.com", "14277145678");
        User user3 = new User("dufu", "male", 24, "b@a.com", "15277145678");

        UserController.userList.add(user1);
        UserController.userList.add(user2);
        UserController.userList.add(user3);

        RsController.rsList.add(new RsEvent("第一条事件","无标签", user1));
        RsController.rsList.add(new RsEvent("第二条事件","无标签", user2));
        RsController.rsList.add(new RsEvent("第三条事件","无标签", user3));
    }

    @Test
    public void get_rs_event_list() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[0]",not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[1]",not(hasKey("user"))))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")))
                .andExpect(jsonPath("$[2]",not(hasKey("user"))));

    }

    @Test
    public void get_event_list_should_throw_index_invalid_when_index_outof_range() throws Exception {
        mockMvc.perform(get("/rs/list?start=0&end=10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid index")));
    }

    @Test
    public void get_events_between() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")));
        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")));
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")));

    }


    @Test
    void get_event_when_given_index_is_1() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyword", is("无标签")));
    }

    @Test
    void get_event_when_given_index_is_2() throws Exception {
        mockMvc.perform(get("/rs/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyword", is("无标签")));
    }

    @Test
    void get_event_when_given_index_is_3() throws Exception {
        mockMvc.perform(get("/rs/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyword", is("无标签")));
    }

    @Test
    void get_event_should_throw_invalid_index_error_when_index_outof_range() throws Exception {
        mockMvc.perform(get("/rs/4"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error",is("invalid index")));
    }

    @Test
    void should_add_rs_event_when_given_event_name_or_keyword() throws Exception {
        String jsonString = "{\"eventName\":\"猪肉涨价啦\",\"keyword\":\"经济\",\"user\": {\"name\":\"yurong\"" +
                ",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\"}}";

        mockMvc.perform(post("/rs/add/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")))
                .andExpect(jsonPath("$[3].eventName", is("猪肉涨价啦")))
                .andExpect(jsonPath("$[3].keyword", is("经济")));

    }

    @Test
    void add_event_should_have_user() throws Exception {
        RsEvent rsEvent = new RsEvent("猪肉涨价啦", "经济");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/add/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void add_event_should_have_user_and_user_should_have_name() throws Exception {
        String jsonString = "{\"eventName\":\"猪肉涨价了\",\"keyword\":\"经济\",\"user\": {"+
                ",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\"}}";
        mockMvc.perform(post("/rs/add/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void get_events_when_delete_event_index_1() throws Exception {
        mockMvc.perform(get("/rs/delete/1")).andExpect(status().isOk()).andExpect(header().stringValues("index", "1"));
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")));
    }

    @Test
    void get_events_when_delete_event_index_2() throws Exception {
        mockMvc.perform(get("/rs/delete/2")).andExpect(status().isOk()).andExpect(header().stringValues("index", "2"));
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")));
    }


    @Test
    void get_events_when_delete_event_index_3() throws Exception {
        mockMvc.perform(get("/rs/delete/3")).andExpect(status().isOk())
                .andExpect(header().stringValues("index", "3"));
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")));
    }

    @Test
    void get_event_when_modify_event_1() throws Exception {
        RsEvent rsEvent = new RsEvent("重大利好", "");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/modify/1").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(header().stringValues("index", "1"));;
        mockMvc.perform(get("/rs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("重大利好")))
                .andExpect(jsonPath("$.keyword", is("无标签")));
    }

    @Test
    void get_event_when_modify_event_2() throws Exception {
        RsEvent rsEvent = new RsEvent("猪肉降价啦", "经济");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/modify/2").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(header().stringValues("index", "2"));;
        mockMvc.perform(get("/rs/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("猪肉降价啦")))
                .andExpect(jsonPath("$.keyword", is("经济")));
    }
}

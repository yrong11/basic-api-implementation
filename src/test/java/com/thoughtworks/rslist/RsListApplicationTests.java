package com.thoughtworks.rslist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.domain.RsEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.awt.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class RsListApplicationTests {

    private MockMvc mockMvc;

    @BeforeEach
    void setup(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(new RsController()).build();
    }

    @Test
    public void get_rs_event_list() throws Exception {
        mockMvc.perform(get("/rs/list"))
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
    void should_add_rs_event_when_given_event_name_or_keyword() throws Exception {
        RsEvent rsEvent = new RsEvent("猪肉涨价啦", "经济");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/add/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无标签")))
                .andExpect(jsonPath("$[3].eventName", is("猪肉涨价啦")))
                .andExpect(jsonPath("$[3].keyword", is("经济")));



    }

}

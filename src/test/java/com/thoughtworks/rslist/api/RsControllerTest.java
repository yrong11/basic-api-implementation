package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.naming.ldap.PagedResultsControl;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class RsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RsEventRepository rsEventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VoteRepository voteRepository;
    private UserDto userDto;
    private RsEventDto rsEventDto;
    User user;
    @BeforeEach
    void setup(){
        voteRepository.deleteAll();
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
        user = new User("yurong1", "femal", 22, "a@b.com", "13277145678", 10);
        initData();
    }

    public void initData(){
        userDto = UserDto.builder().name(user.getName()).gender(user.getGender()).age(user.getAge())
                .email(user.getEmail()).phone(user.getPhone()).voteNum(user.getVoteNum()).build();
        userRepository.save(userDto);
        rsEventDto = RsEventDto.builder().userDto(userDto).eventName("第一条事件").keyword("无标签").build();
        rsEventRepository.save(rsEventDto);
    }

    @Test
    public void get_rs_event_list() throws Exception {
        rsEventDto = RsEventDto.builder().userDto(userDto).eventName("第二条事件").keyword("无标签").build();
        rsEventRepository.save(rsEventDto);
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无标签")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无标签")));
    }

    @Test
    public void get_event_list_when_index_outof_range() throws Exception {
        mockMvc.perform(get("/rs/list?page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()",is(1)));
    }


    @Test
    void get_event_when_given_index_is_valid() throws Exception {
        mockMvc.perform(get("/rs/" + rsEventDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyword", is("无标签")));
    }

    @Test
    void get_event_when_given_invalid_index() throws Exception {
        mockMvc.perform(get("/rs/"+ rsEventDto.getId()+1))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_rs_event_when_user_exisit() throws Exception {
        User user = new User("yurong1", "femal", 22, "a@b.com", "13277145678");
        userDto = UserDto.builder().name(user.getName()).gender(user.getGender()).age(user.getAge())
                .email(user.getEmail()).phone(user.getPhone()).voteNum(user.getVoteNum()).build();
        userRepository.save(userDto);
        String jsonString = "{\"eventName\":\"猪肉涨价啦\",\"keyword\":\"经济\",\"userId\": "+ userDto.getId()+ "}";

        mockMvc.perform(post("/rs/add/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventDto> rsEventDtos = rsEventRepository.findAll();
        assertEquals(2, rsEventDtos.size());
        assertEquals("猪肉涨价啦", rsEventDtos.get(1).getEventName());
    }

    @Test
    void add_event_should_have_user_id() throws Exception {
        RsEvent rsEvent = new RsEvent("猪肉涨价啦", "经济");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/add/event").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }


    @Test
    void get_events_when_delete_event_index_1() throws Exception {
        int rsId = rsEventDto.getId();
        mockMvc.perform(delete("/rs/delete/"+ rsId)).andExpect(status().isOk())
                .andExpect(header().stringValues("rsId", rsId+""));
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    void get_event_when_modify_event_1() throws Exception {
        initData();
        RsEvent rsEvent = new RsEvent("重大利好", "", userDto.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(patch("/rs/modify/"+ rsEventDto.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(header().stringValues("index", rsEventDto.getId()+""));
        RsEventDto rsEventDto1 = rsEventRepository.findById(rsEventDto.getId()).get();
        assertEquals("重大利好", rsEventDto1.getEventName());
        assertEquals("无标签", rsEventDto1.getKeyword());
    }

    @Test
    void get_event_when_modify_event_2() throws Exception {
        initData();
        RsEvent rsEvent = new RsEvent("猪肉降价啦", "经济", 2);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(patch("/rs/modify/" + rsEventDto.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}

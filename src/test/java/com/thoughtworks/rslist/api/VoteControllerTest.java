package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VoteControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private RsEventRepository rsEventRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private UserRepository userRepository;
    private UserDto userDto;
    private RsEventDto rsEventDto;
    User user;

    public void addUserAndRsEventToDatabase(){
        user = new User("yurong1", "femal", 22, "a@b.com", "13277145678", 10);
        userDto = UserDto.builder().name(user.getName()).gender(user.getGender()).age(user.getAge())
                .email(user.getEmail()).phone(user.getPhone()).voteNum(user.getVoteNum()).build();
        userRepository.save(userDto);
        rsEventDto = RsEventDto.builder().userDto(userDto).eventName("第一条事件").keyword("无标签").build();
        rsEventRepository.save(rsEventDto);
    }

    @BeforeEach
    void setUp() {
        voteRepository.deleteAll();
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
        addUserAndRsEventToDatabase();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void test_vote_rs_event() throws Exception {
        Vote vote = new Vote(userDto.getId(), rsEventDto.getId(), 4, new Date());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/"+ rsEventDto.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<VoteDto> voteDtos = voteRepository.findAll();
        assertEquals(1,voteDtos.size());
    }

    @Test
    public void return_bad_request_when_rs_id_invalid() throws Exception {
        Vote vote = new Vote(userDto.getId(), rsEventDto.getId(), 4, new Date());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/"+ 5).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void return_bad_request_when_user_votenum_less_than_vote_num() throws Exception {
        Vote vote = new Vote(userDto.getId(), rsEventDto.getId(), 100, new Date());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/"+ rsEventDto.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_get_vote_record_between_start_time_and_end_time() throws Exception {
        Date date = new Date();
        Vote vote = new Vote(userDto.getId(), rsEventDto.getId(), 4, date);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/"+ rsEventDto.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        userDto.setName("yurong2");
        userRepository.save(userDto);
        date = getNewDateWithAddDay(date, -2);
        vote = new Vote(userDto.getId(), rsEventDto.getId(), 4, date);
        jsonString = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote/"+ rsEventDto.getId()).content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/vote/records")
                .param("startTime", getNewDateWithAddDay(date, 1).toString())
                .param("endTime", new Date().toString())
                .param("page", "0")
                .param("size", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)));
    }

    public Date getNewDateWithAddDay(Date date,int addDayNum){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,addDayNum);
        date=calendar.getTime();
        return date;
    }

}
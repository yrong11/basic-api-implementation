package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.exception.BadRequestException;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoteController {
    @Autowired
    VoteService voteService;

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity voteRsEvent(@PathVariable int rsEventId, @RequestBody Vote vote) throws BadRequestException {
        voteService.voteRsEvent(rsEventId, vote);
        return ResponseEntity.ok().build();

    }
}

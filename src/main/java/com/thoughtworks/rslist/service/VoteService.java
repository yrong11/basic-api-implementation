package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.exception.BadRequestException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.ldap.PagedResultsControl;
import java.util.Date;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RsEventRepository rsEventRepository;

    @Transactional
    public void voteRsEvent(Vote vote) throws BadRequestException {
        if (!userRepository.findById(vote.getUserId()).isPresent())
            throw new BadRequestException();
        UserDto userDto = userRepository.findById(vote.getUserId()).get();
        if (userDto.getVoteNum() < vote.getVoteNum())
            throw new BadRequestException();
        RsEventDto rsEventDto = rsEventRepository.findById(vote.getRsEventId()).get();
        VoteDto voteDto = VoteDto.builder().voteNum(vote.getVoteNum()).rsEventDto(rsEventDto)
                .userDto(userDto).voteTime(new Date()).build();
        userDto.setVoteNum(userDto.getVoteNum() - vote.getVoteNum());
        userRepository.save(userDto);
    }
}

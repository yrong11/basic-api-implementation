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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.ldap.PagedResultsControl;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VoteService {

    final RsEventRepository rsEventRepository;
    final UserRepository userRepository;
    final VoteRepository voteRepository;

    public VoteService(RsEventRepository rsEventRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    @Transactional
    public void voteRsEvent(int rsEventId, Vote vote) throws BadRequestException {
        Optional<RsEventDto> rsEventDto = rsEventRepository.findById(rsEventId);
        Optional<UserDto> userDto = userRepository.findById(vote.getUserId());
        if (!rsEventDto.isPresent() || !userDto.isPresent() || vote.getVoteNum() > userDto.get().getVoteNum())
            throw new BadRequestException();
        UserDto user = userDto.get();
        user.setVoteNum(user.getVoteNum() - vote.getVoteNum());
        RsEventDto rsEvent = rsEventDto.get();
        rsEvent.setVoteNum(rsEvent.getVoteNum() + vote.getVoteNum());
        VoteDto voteDto = VoteDto.builder().userDto(user).rsEventDto(rsEvent)
                .voteNum(vote.getVoteNum()).voteTime(vote.getVoteTime()).build();
        userRepository.save(user);
        rsEventRepository.save(rsEvent);
        voteRepository.save(voteDto);
    }

    public List<Vote> getVoteListAccordingTime(Date startTime, Date endTime, Integer page, Integer size) {
        List<VoteDto> voteDtos;
        if (page != null && size != null && page > 0 && size > 0){
            Pageable pageable = PageRequest.of(page - 1, size);
            voteDtos = voteRepository.findRecordAccordingVoteTime(startTime, endTime, pageable);
        }else
            voteDtos = voteRepository.findRecordAccordingVoteTime(startTime, endTime);
        List<Vote> voteList = voteDtos.stream().map(
                item -> item.convertVote()
        ).collect(Collectors.toList());
        return voteList;
    }
}

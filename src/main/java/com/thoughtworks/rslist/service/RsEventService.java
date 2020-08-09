package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.api.UserController;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.exception.RsEventIndexNotValidException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RsEventService {

    final RsEventRepository rsEventRepository;
    final UserRepository userRepository;
    final VoteRepository voteRepository;

    public RsEventService(RsEventRepository rsEventRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }


    public RsEvent getRsEvent(int rsId){
        Optional<RsEventDto> dto = rsEventRepository.findById(rsId);
        if (!dto.isPresent()){
            throw new RsEventIndexNotValidException("invalid index");
        }
        return RsEvent.builder().userId(dto.get().getUserDto().getId()).keyword(dto.get().getKeyword()).eventName(dto.get().getEventName()).build();
    }

    public List<RsEvent> getRsEventBetween(Integer page, Integer size){
        List<RsEventDto> rsEventDtos;
        if (page != null && size != null && page > 0 && size > 0){
            Pageable pageable = PageRequest.of(page - 1, size);
            rsEventDtos = rsEventRepository.findAll(pageable).getContent();
        }else
            rsEventDtos = rsEventRepository.findAll();

        return rsEventDtos.stream().map(
                item -> RsEvent.builder().eventName(item.getEventName()).keyword(item.getKeyword()).userId(item.getUserDto().getId()).build())
                .collect(Collectors.toList());

    }

    public boolean addRsEvent(RsEvent rsEvent){
        if (!userRepository.findById(rsEvent.getUserId()).isPresent())
            return false;
        UserDto userDto = userRepository.findById(rsEvent.getUserId()).get();
        RsEventDto rsEventDto = RsEventDto.builder().eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword()).userDto(userDto).build();

        rsEventRepository.save(rsEventDto);
        return true;

    }

    public boolean updateRsEvent(int rsId,RsEvent rsEvent){
        if (!rsEventRepository.findById(rsId).isPresent())
            return false;
        RsEventDto rsEventDto = rsEventRepository.findById(rsId).get();
        if (rsEventDto.getUserDto().getId() != rsEvent.getUserId())
            return false;
        if (!rsEvent.getEventName().equals(""))
            rsEventDto.setEventName(rsEvent.getEventName());
        if (!rsEvent.getKeyword().equals(""))
            rsEventDto.setKeyword(rsEvent.getKeyword());
        rsEventRepository.save(rsEventDto);
        return true;
    }

    public void deleteRsEvent(int rsId) {
        Optional<RsEventDto> dto = rsEventRepository.findById(rsId);
        if (!dto.isPresent())
            throw new RsEventIndexNotValidException();
        rsEventRepository.deleteById(rsId);
    }
}

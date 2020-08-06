package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.api.UserController;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.exception.RsEventIndexNotValidException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RsEventService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    public RsEvent getRsEvent(int index){
        if (index < 1 || index > RsController.rsList.size()){
            throw new RsEventIndexNotValidException("invalid index");
        }
        return RsController.rsList.get(index - 1);
    }

    public List<RsEvent> getRsEventBetween(int start, int end){
        if (start != -1 && end != -1)
            if (start < 1 || end >RsController.rsList.size() || end < start)
                throw new RsEventIndexNotValidException("invalid index");
            else
                return RsController.rsList.subList(start-1, end);

        return RsController.rsList;
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
}

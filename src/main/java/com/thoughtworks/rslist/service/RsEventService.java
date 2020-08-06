package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.api.UserController;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.exception.RsEventIndexNotValidException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
        RsEventDto rsEventDto = RsEventDto.builder().eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword()).userId(rsEvent.getUserId()).build();
        rsEventRepository.save(rsEventDto);
        return true;

    }

    public void modifyRsEvent(int index,RsEvent rsEvent){
        if (!rsEvent.getEventName().equals(""))
            RsController.rsList.get(index - 1).setEventName(rsEvent.getEventName());
        if (!rsEvent.getKeyword().equals(""))
            RsController.rsList.get(index - 1).setKeyword(rsEvent.getKeyword());
    }
}

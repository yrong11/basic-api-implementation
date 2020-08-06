package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.api.UserController;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.exception.RsEventIndexNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RsEventService {

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

    public void addRsEvent(RsEvent rsEvent){
        if (!UserController.userList.contains(rsEvent))
            UserController.userList.add(rsEvent.getUser());
        RsController.rsList.add(rsEvent);
    }

    public void modifyRsEvent(int index,RsEvent rsEvent){
        if (!rsEvent.getEventName().equals(""))
            RsController.rsList.get(index - 1).setEventName(rsEvent.getEventName());
        if (!rsEvent.getKeyword().equals(""))
            RsController.rsList.get(index - 1).setKeyword(rsEvent.getKeyword());
    }
}

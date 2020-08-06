package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RsEvent {
    String eventName;
    String keyword;
    @NotNull
    Integer userId;

    public interface Add{
    }

    public RsEvent(String eventName, String keyword) {
        this.eventName = eventName;
        this.keyword = keyword;
    }
    public RsEvent(String eventName, String keyword, int userId){
        this.eventName = eventName;
        this.keyword = keyword;
        this.userId = userId;
    }
    public RsEvent(){

    }

    public String getEventName() {
        return eventName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @JsonIgnore
    public int getUser() {
        return userId;
    }

    @JsonProperty
    public void setUser(int userId) {
        this.userId = userId;
    }
}


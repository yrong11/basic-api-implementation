package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {
    String eventName;
    String keyword;
    @NotNull
    Integer userId;


    public RsEvent(String eventName, String keyword) {
        this.eventName = eventName;
        this.keyword = keyword;
    }
    public RsEvent(String eventName, String keyword, int userId){
        this.eventName = eventName;
        this.keyword = keyword;
        this.userId = userId;
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


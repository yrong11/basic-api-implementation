package com.thoughtworks.rslist.domain;

import lombok.Data;

@Data
public class RsEvent {
    String eventName;
    String keyword;

    public RsEvent(String eventName, String keyword) {
        this.eventName = eventName;
        this.keyword = keyword;
    }
    public RsEvent(){

    }

    public String getEventName() {
        return eventName;
    }

    public String getKeywords() {
        return keyword;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setKeywords(String keywords) {
        this.keyword = keywords;
    }
}

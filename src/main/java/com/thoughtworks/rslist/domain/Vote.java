package com.thoughtworks.rslist.domain;

import lombok.Data;

@Data
public class Vote {
    private int userId;
    private int rsEventId;
    private int voteNum;

    public Vote(int userId, int rsEventId, int voteNum) {
        this.userId = userId;
        this.rsEventId = rsEventId;
        this.voteNum = voteNum;
    }
}

package com.thoughtworks.rslist.dto;

import com.thoughtworks.rslist.domain.Vote;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Vote")
public class VoteDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private UserDto userDto;
    @ManyToOne
    private RsEventDto rsEventDto;

    private Date voteTime;
    private int voteNum;

    public Vote convertVote() {
        Vote vote = new Vote();
        vote.setVoteNum(this.getVoteNum());
        vote.setVoteTime(this.getVoteTime());
        vote.setUserId(this.getUserDto().getId());
        vote.setRsEventId(this.getRsEventDto().getId());
        return vote;
    }
}

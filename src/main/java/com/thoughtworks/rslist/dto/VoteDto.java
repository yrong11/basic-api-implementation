package com.thoughtworks.rslist.dto;

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
}

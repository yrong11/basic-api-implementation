package com.thoughtworks.rslist.dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {
    @Id
    @Generated
    private int id;

    @ManyToOne
    private UserDto userDto;
    @ManyToOne
    private RsEventDto rsEventDto;

    private Date voteTime;
    private int voteNum;
}

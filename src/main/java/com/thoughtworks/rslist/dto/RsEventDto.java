package com.thoughtworks.rslist.dto;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "RsEvent")
public class RsEventDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String eventName;
    private String keyword;
    @Builder.Default
    private int voteNum = 0;

    @ManyToOne
    private UserDto userDto;

    @OneToMany(mappedBy = "rsEventDto")
    private List<VoteDto> voteDtos;


}

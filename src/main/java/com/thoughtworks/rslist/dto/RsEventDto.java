package com.thoughtworks.rslist.dto;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEventDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String eventName;
    private String keyword;

    @ManyToOne
    private UserDto userDto;

    @OneToMany(mappedBy = "rsEventDto")
    private List<VoteDto> voteDtos;


}

package com.thoughtworks.rslist.dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RsEventDto {
    @Id
    @Generated
    private int id;
    private String eventName;
    private String keyword;

    @ManyToOne
    private UserDto userDto;

    @OneToMany(mappedBy = "rsEventDto")
    private List<VoteDto> voteDtos;


}

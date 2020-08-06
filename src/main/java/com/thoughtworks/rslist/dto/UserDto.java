package com.thoughtworks.rslist.dto;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class UserDto {
    @Id
    @Generated
    private int id;
    private String name;
    private String gender;
    private int age;
    private String email;
    private String phone;
    private int voteNum;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "userDto")
    private List<RsEventDto> rsEventDtos;

}

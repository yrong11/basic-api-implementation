package com.thoughtworks.rslist.dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

}

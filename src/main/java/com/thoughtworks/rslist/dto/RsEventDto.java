package com.thoughtworks.rslist.dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

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

    private int userId;


}

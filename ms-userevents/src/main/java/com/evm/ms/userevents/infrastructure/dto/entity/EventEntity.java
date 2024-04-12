package com.evm.ms.userevents.infrastructure.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "event")
public class EventEntity {

    @Id
    private String id;

    private String userId;

    private String title;

    private String description;

    private String location;

    @Field("event_date_time")
    private String eventDateTime;

//    @Field("time_zone")
//    private String timeZone;

    @Field("pre_notices")
    private List<Integer> preNotices;  // in minutes

}

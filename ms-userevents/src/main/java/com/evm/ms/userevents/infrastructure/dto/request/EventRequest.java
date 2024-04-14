package com.evm.ms.userevents.infrastructure.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {

    private String id;

    private String userId;

    private String title;

    private String description;

    private String location;

    private ZonedDateTime eventDateTime;

    private List<Integer> preNotices;  // in minutes

    private Boolean triggered;

}

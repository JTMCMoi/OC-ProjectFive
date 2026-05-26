package com.openclassrooms.DTO.Response;

import lombok.Data;

@Data
public class TopicResponseDTO {

    private Integer id;
    private String name;
    private boolean subscribed;
}

package com.openclassrooms.Models;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class SubscriptionId implements Serializable {

    private Integer userId;
    private Integer topicId;
}

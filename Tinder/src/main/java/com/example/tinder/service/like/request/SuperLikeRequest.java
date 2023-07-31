package com.example.tinder.service.like.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuperLikeRequest {
    private Long likerId;
    private Long likeeId;
}

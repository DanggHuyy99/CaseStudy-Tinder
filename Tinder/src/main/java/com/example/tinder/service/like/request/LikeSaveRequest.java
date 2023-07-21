package com.example.tinder.service.like.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeSaveRequest {
    private Long likerId;

    private Long likeeId;

    private LocalDateTime likeDate;
}

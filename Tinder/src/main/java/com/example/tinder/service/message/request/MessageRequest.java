package com.example.tinder.service.message.request;

import com.example.tinder.model.Message;
import com.example.tinder.model.Photo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
    private Long senderId;

    private Long receiverId;

    private String content;

    private String senderName;

    private String photos;

    public static MessageRequest fromEntity(Message message) {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setSenderId(message.getSender().getId());
        messageRequest.setReceiverId(message.getReceiver().getId());
        messageRequest.setContent(message.getContent());
        return messageRequest;
    }
}

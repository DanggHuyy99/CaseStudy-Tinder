package com.example.tinder.controller.rest;

import com.example.tinder.model.Message;
import com.example.tinder.model.User;
import com.example.tinder.service.message.MessageService;
import com.example.tinder.service.message.request.ChatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/getChatHistory")
    public ResponseEntity<List<Message>> getChatHistory(@RequestBody ChatRequest chatRequest) {
        Long senderId = chatRequest.getSenderId();
        Long receiverId = chatRequest.getReceiverId();

        User sender = new User();
        sender.setId(senderId);

        User receiver = new User();
        receiver.setId(receiverId);

        List<Message> chatHistory = messageService.getMessagesBetweenUsers(sender, receiver);

        if (chatHistory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(chatHistory);
    }
}

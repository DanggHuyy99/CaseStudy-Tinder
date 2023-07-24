package com.example.tinder.controller.rest;

import com.example.tinder.model.Message;
import com.example.tinder.model.User;
import com.example.tinder.service.message.MessageService;
import com.example.tinder.service.message.request.MessageRequest;
import com.example.tinder.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
public class MessageRestController {
    private final MessageService messageService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/api/sendMessage") // Định nghĩa endpoint của WebSocket để nhận tin nhắn từ frontend
    public void sendMessage(@Payload MessageRequest messageRequest, Authentication authentication) {
        // Gửi và lưu tin nhắn
        User sender = userService.findByUsername(authentication.getName());
                User receiver = userService.findUserById( messageRequest.getReceiverId());
                String content = messageRequest.getContent();

        messageService.sendAndSaveMessage(sender, receiver, content);

        List<Message> chatMessages = messageService.getMessagesBetweenUsers(sender, receiver);
        messagingTemplate.convertAndSendToUser(sender.getUsername(), "/queue/messages", chatMessages);
        messagingTemplate.convertAndSendToUser(receiver.getUsername(), "/queue/messages", chatMessages);
    }

    @GetMapping("/api/messages/{receiverId}")
    public ResponseEntity<List<Message>> getMessagesBetweenUsers(@PathVariable Long receiverId, Authentication authentication) {
        User sender = userService.findByUsername(authentication.getName());
        User receiver = userService.findUserById(receiverId);

        if (receiver == null) {
            return ResponseEntity.notFound().build();
        }

        List<Message> chatMessages = messageService.getMessagesBetweenUsers(sender, receiver);
        return ResponseEntity.ok(chatMessages);
    }
}

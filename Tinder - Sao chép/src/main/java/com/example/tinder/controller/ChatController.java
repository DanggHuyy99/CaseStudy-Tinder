package com.example.tinder.controller;

import com.example.tinder.model.Message;
import com.example.tinder.model.User;
import com.example.tinder.service.message.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@AllArgsConstructor

public class ChatController {

    @Autowired
    private final MessageService messageService;

    @GetMapping("/chat")
    public String chatPage(Model model) {
        return "chat";
    }

    @GetMapping("/api/chat/getChatHistory")
    @ResponseBody
    public ResponseEntity<?> getChatHistory(@RequestParam("userId1") Long userId1,
                                                        @RequestParam("userId2") Long userId2) {
        User user1 = new User();
        user1.setId(userId1);

        User user2 = new User();
        user2.setId(userId2);

        List<Message> chatHistory = messageService.getMessagesBetweenUsers(user1, user2);

        if (chatHistory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(chatHistory);
    }
}

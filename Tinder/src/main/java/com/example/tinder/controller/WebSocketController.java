package com.example.tinder.controller;

import com.example.tinder.model.ChatMessage;
import com.example.tinder.model.Message;
import com.example.tinder.model.User;
import com.example.tinder.repository.MessageRepository;
import com.example.tinder.repository.UserRepository;
import com.example.tinder.service.like.request.SuperLikeRequest;
import com.example.tinder.service.message.MessageService;
import com.example.tinder.service.message.request.MessageRequest;
import com.example.tinder.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static java.rmi.server.LogStream.log;

@Controller
@AllArgsConstructor
@Slf4j
public class WebSocketController {
    private final UserService userService;
    private final MessageService messageService;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    @MessageMapping("/sendMessage")
    @SendTo("/topic/public") // Gửi tin nhắn tới user cụ thể
    public void handleSendMessage(MessageRequest messageRequest, Authentication authentication) {
        log.error("JSON payload không hợp lệ: " + messageRequest);
        User sender = userService.findByUsername(authentication.getName());
        User receiver = userService.findUserById(messageRequest.getReceiverId());
        String content = messageRequest.getContent();

        messageService.sendAndSaveMessage(sender, receiver, content);
        messagingTemplate.convertAndSendToUser(receiver.getUsername(), "/queue/reply", messageRequest);

        //return messageRequest;
    }
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")// Gửi tin nhắn tới user cụ thể
    public MessageRequest addUser(MessageRequest messageRequest, Authentication authentication, SimpMessageHeaderAccessor headerAccessor) {
        log.error("JSON payload không hợp lệ: " + messageRequest);
        User sender = userService.findByUsername(authentication.getName());
        User receiver = userService.findUserById(messageRequest.getReceiverId());
        String content = messageRequest.getContent();
        messageRequest.setSenderName(sender.getUserProfile().getFullName());
        if(headerAccessor.getSessionAttributes() != null){
            headerAccessor.getSessionAttributes().put("username", sender.getUsername());
        }


        messageService.sendAndSaveMessage(sender, receiver, content);

        return messageRequest;
    }

    @MessageMapping("/chat.superlike")
    @SendTo("/topic/public")
    public SuperLikeRequest superLike(SuperLikeRequest superLikeRequest, Authentication authentication){
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String likerUsername = authentication.getName();
        User liker = userService.findByUsername(likerUsername);

        Long likeeId = superLikeRequest.getLikeeId();
        User likee = userService.findUserById(likeeId);

        if (liker == null || likee == null) {
            return null;
        }

        Map<String, String> notificationData = new HashMap<>();
        notificationData.put("type", "superlike");
        notificationData.put("likerName", liker.getUserProfile().getFullName());

        messagingTemplate.convertAndSendToUser(likee.getUsername(), "/queue/notifications", notificationData);

        return superLikeRequest;
    }


    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload MessageRequest messageRequest, SimpMessageHeaderAccessor headerAccessor) {
        try {
            if (messageRequest.getSenderId() == null || messageRequest.getReceiverId() == null || messageRequest.getContent() == null) {
                log.error("JSON payload không hợp lệ: " + messageRequest);
                return;
            }

            // Kiểm tra xem các thuộc tính của đối tượng Message có đúng kiểu dữ liệu như trong JSON payload không
            User sender = userService.findUserById(messageRequest.getSenderId());
            if (sender == null) {
                log.error("Người gửi không tồn tại với ID: " + messageRequest.getSenderId());
                return;
            }
            messageRequest.setSenderName(sender.getUserProfile().getFullName());
            messageRequest.setPhotos(sender.getPhotos().get(0).getImageUrl());

            Long receiverId = messageRequest.getReceiverId();
            String content = messageRequest.getContent();

            // Kiểm tra nếu người nhận có tồn tại trong hệ thống
            Optional<User> receiverOptional = userRepository.findById(receiverId);
            if (receiverOptional.isPresent()) {
                User receiver = receiverOptional.get();

                // Gửi và lưu tin nhắn
                messageService.sendAndSaveMessage(sender, receiver, content);
            } else {
                log.error("Người nhận không tồn tại với ID: " + receiverId);
            }
        } catch (Exception e) {
            log.error("Đã có lỗi xảy ra khi xử lý tin nhắn: " + e.getMessage());
        }
    }

//    @MessageMapping("/chat.getChatHistory")
////    @SendTo("/user/queue/chatHistory")
//    public List<Message> getChatHistory(MessageRequest messageDto, Authentication authentication) {
//        Long receiverId = messageDto.getReceiverId();
//        String senderUsername = authentication.getName();
//        User sender = userRepository.findByUsernameIgnoreCase(senderUsername);
//        User receiver = userRepository.findById(receiverId).orElse(null);
//
//        if (sender != null && receiver != null) {
//            return messageService.getMessagesBetweenUsers(sender, receiver);
////            List<MessageRequest> chatHistoryDto = chatHistory.stream()
////                    .map(MessageRequest::fromEntity)
////                    .collect(Collectors.toList());
////            return chatHistoryDto;
//        }
//        return Collections.emptyList();
//    }

//    @MessageMapping("/chat.getChatHistory")
//    @SendTo("/queue/messages")
//    public List<ChatMessage> getChatHistory(MessageRequest messageDto, Authentication authentication) {
//        Long receiverId = messageDto.getReceiverId();
//        String senderUsername = authentication.getName();
//        User sender = userRepository.findByUsernameIgnoreCase(senderUsername);
//        User receiver = userRepository.findById(receiverId).orElse(null);
//
//        List<ChatMessage> chatHistory = new ArrayList<>();
//
//        if (sender != null && receiver != null) {
//            List<Message> messages = messageService.getMessagesBetweenUsers(sender, receiver);
//            for (Message message : messages) {
//                ChatMessage chatMessage = new ChatMessage();
//                chatMessage.setSender(message.getSender().getUsername());
//                chatMessage.setContent(message.getContent());
//                chatMessage.setSentTime(message.getSentAt());
//                chatHistory.add(chatMessage);
//            }
//        }
//
//        return chatHistory;
//    }
}

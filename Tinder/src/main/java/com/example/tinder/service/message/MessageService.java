package com.example.tinder.service.message;

import com.example.tinder.model.Message;
import com.example.tinder.model.User;
import com.example.tinder.repository.MessageRepository;
import com.example.tinder.service.message.request.MessageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Data
public class MessageService {
    private final SimpMessagingTemplate messagingTemplate;

    private final MessageRepository messageRepository;

    public void sendAndSaveMessage(User sender, User receiver, String content) {
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setSentAt(new Date());

        MessageRequest messageDto = new MessageRequest(sender.getId(), receiver.getId(), content);
        messagingTemplate.convertAndSendToUser(receiver.getUsername(), "/queue/messages", messageDto);

        messageRepository.save(message);
//
//        MessageRequest messageDto = new MessageRequest(sender.getId(), receiver.getId(), content);
//        messagingTemplate.convertAndSendToUser(receiver.getUsername(), "/queue/messages", messageDto);
    }

    public List<Message> getMessagesBetweenUsers(User user1, User user2) {
        return messageRepository.findBySenderAndReceiverOrReceiverAndSenderOrderBySentAt(user1, user2, user2, user1);
    }
}

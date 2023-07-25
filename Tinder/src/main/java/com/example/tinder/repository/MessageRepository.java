package com.example.tinder.repository;

import com.example.tinder.model.Message;
import com.example.tinder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndReceiverOrReceiverAndSenderOrderBySentAt(User sender, User receiver, User receiver2, User sender2);

    List<Message> findBySenderAndReceiverOrderBySentAtAsc(User sender, User receiver);
}

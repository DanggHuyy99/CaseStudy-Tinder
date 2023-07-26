package com.example.tinder.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "swipers")
public class Swiper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "swipee_id")
    private User swipee;

    private LocalDateTime swipeDate;

    private boolean isLiked;

    private boolean isSuperliked;

    private boolean isMatch;
}

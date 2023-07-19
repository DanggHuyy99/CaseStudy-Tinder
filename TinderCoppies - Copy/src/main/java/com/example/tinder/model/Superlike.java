package com.example.tinder.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "superlikes")
public class Superlike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "swiper_id")
    private User swiper;

    @ManyToOne
    @JoinColumn(name = "swipee_id")
    private User swipee;

    private LocalDateTime superlikeDate;
}

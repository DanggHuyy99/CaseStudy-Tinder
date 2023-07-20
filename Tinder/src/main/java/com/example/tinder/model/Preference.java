package com.example.tinder.model;

import com.example.tinder.model.gender.Gender;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "preferences")
public class Preference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int minAge;

    private int maxAge;

    @Enumerated(EnumType.STRING)
    private Gender gender;
}

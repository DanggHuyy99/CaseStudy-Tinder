package com.example.tinder.model;

import com.example.tinder.model.interest.Interest;
import com.example.tinder.model.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private UserProfile userProfile;

    @ManyToMany
    @JoinTable(
            name = "user_matches",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "match_id")
    )
    @JsonIgnore
    private List<User> matches;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Message> sentMessages;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Message> receivedMessages;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)

    private List<Photo> photos;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Preference preference;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Block> blocks;

    @ElementCollection(targetClass = Interest.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_interests", joinColumns = @JoinColumn(name = "user_id"))

    private Set<Interest> interests;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch =FetchType.LAZY)
    @JsonIgnore
    private Swiper swiper;

    @OneToMany(mappedBy = "liker", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Like> likesGiven;

    @OneToMany(mappedBy = "likee", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Like> likesReceived;

    private boolean VIPaccount;

    @Enumerated(EnumType.STRING)
    private Role role;
}

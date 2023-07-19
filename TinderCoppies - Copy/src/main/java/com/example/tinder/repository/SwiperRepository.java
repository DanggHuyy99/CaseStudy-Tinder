package com.example.tinder.repository;

import com.example.tinder.model.Like;
import com.example.tinder.model.Swiper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwiperRepository extends JpaRepository<Swiper, Long> {
}

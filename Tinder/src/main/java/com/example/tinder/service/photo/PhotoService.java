package com.example.tinder.service.photo;

import com.example.tinder.model.Photo;
import com.example.tinder.repository.PhotoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Data
@Transactional
public class PhotoService {
    private final PhotoRepository photoRepository;

    public List<Photo> getPhotosByUserId(Long id){
        return photoRepository.findByUserId(id);
    }

}

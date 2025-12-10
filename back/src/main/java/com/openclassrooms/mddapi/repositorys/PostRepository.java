package com.openclassrooms.mddapi.repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.models.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    List<PostEntity> findByTopicId(Long topicId);
    List<PostEntity> findByAuthorId(Long userId);
}

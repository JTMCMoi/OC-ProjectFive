package com.openclassrooms.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openclassrooms.Models.CommentEntity;
import com.openclassrooms.Models.PostEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    List<CommentEntity> findByPostOrderByCreatedAtAsc(PostEntity post);
}

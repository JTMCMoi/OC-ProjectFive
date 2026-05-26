package com.openclassrooms.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openclassrooms.Models.PostEntity;
import com.openclassrooms.Models.TopicEntity;

public interface PostRepository extends JpaRepository<PostEntity, Integer>{

    /* Tri par date de création descendante */
    List<PostEntity> findByTopicInOrderByCreatedAtDesc(List<TopicEntity> topics);
    /* Tri par date de création ascendante */
    List<PostEntity> findByTopicInOrderByCreatedAtAsc(List<TopicEntity> topics);
}

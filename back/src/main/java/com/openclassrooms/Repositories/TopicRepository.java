package com.openclassrooms.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openclassrooms.Models.TopicEntity;

public interface TopicRepository extends JpaRepository<TopicEntity, Integer> {

}

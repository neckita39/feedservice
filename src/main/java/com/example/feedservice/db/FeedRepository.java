package com.example.feedservice.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedRepository extends JpaRepository<FeedEntity, Long> {
    List<FeedEntity> findByUsername(List<String> usernames);
}

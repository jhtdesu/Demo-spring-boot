package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.Chapter;

@Repository
public interface ChapterRepository extends MongoRepository<Chapter, String> {
    // Add custom query methods if needed
}

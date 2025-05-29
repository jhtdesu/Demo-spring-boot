package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.Chapter;
import java.util.List;

@Repository
public interface ChapterRepository extends MongoRepository<Chapter, String> {
    // Add custom query methods if needed
    List<Chapter> findByBookId(String bookId);

    List<Chapter> findByBookIdOrderByCreatedAtDesc(String bookId);
}

package com.example.demo.repository;

import com.example.demo.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    // Add custom query methods if needed
}

package com.example.demo.repository;

import com.example.demo.model.BlogPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BlogPostRepository extends MongoRepository<BlogPost, String> {
    List<BlogPost> findByAuthor(String author);

    List<BlogPost> findByPublished(boolean published);

    List<BlogPost> findByTagsContaining(String tag);
}
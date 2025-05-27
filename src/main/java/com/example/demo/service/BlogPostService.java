package com.example.demo.service;

import com.example.demo.model.BlogPost;
import com.example.demo.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BlogPostService {

    @Autowired
    private BlogPostRepository blogPostRepository;

    public List<BlogPost> getAllPosts() {
        return blogPostRepository.findAll();
    }

    public List<BlogPost> getPublishedPosts() {
        return blogPostRepository.findByPublished(true);
    }

    public Optional<BlogPost> getPostById(String id) {
        return blogPostRepository.findById(id);
    }

    public List<BlogPost> getPostsByAuthor(String author) {
        return blogPostRepository.findByAuthor(author);
    }

    public List<BlogPost> getPostsByTag(String tag) {
        return blogPostRepository.findByTagsContaining(tag);
    }

    public BlogPost createPost(BlogPost post) {
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        return blogPostRepository.save(post);
    }

    public Optional<BlogPost> updatePost(String id, BlogPost updatedPost) {
        return blogPostRepository.findById(id)
                .map(existingPost -> {
                    existingPost.setTitle(updatedPost.getTitle());
                    existingPost.setContent(updatedPost.getContent());
                    existingPost.setTags(updatedPost.getTags());
                    existingPost.setPublished(updatedPost.isPublished());
                    existingPost.setUpdatedAt(LocalDateTime.now());
                    return blogPostRepository.save(existingPost);
                });
    }

    public void deletePost(String id) {
        blogPostRepository.deleteById(id);
    }
}
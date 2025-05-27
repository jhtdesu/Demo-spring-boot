package com.example.demo.controller;

import com.example.demo.model.BlogPost;
import com.example.demo.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/blog")
@CrossOrigin(origins = { "https://backend-jh-cff06dd28ef7.herokuapp.com",
        "https://frontend-jh-74d9be1b01e4.herokuapp.com" })
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;

    @GetMapping
    public List<BlogPost> getAllPosts() {
        return blogPostService.getAllPosts();
    }

    @GetMapping("/published")
    public List<BlogPost> getPublishedPosts() {
        return blogPostService.getPublishedPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPost> getPostById(@PathVariable String id) {
        return blogPostService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/author/{author}")
    public List<BlogPost> getPostsByAuthor(@PathVariable String author) {
        return blogPostService.getPostsByAuthor(author);
    }

    @GetMapping("/tag/{tag}")
    public List<BlogPost> getPostsByTag(@PathVariable String tag) {
        return blogPostService.getPostsByTag(tag);
    }

    @PostMapping
    public BlogPost createPost(@RequestBody BlogPost post) {
        return blogPostService.createPost(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPost> updatePost(@PathVariable String id, @RequestBody BlogPost post) {
        return blogPostService.updatePost(id, post)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        blogPostService.deletePost(id);
        return ResponseEntity.ok().build();
    }
}
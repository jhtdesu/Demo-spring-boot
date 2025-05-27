package com.example.demo.controller;

import com.example.demo.model.Comment;
import com.example.demo.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/post/{blogPostId}")
    public List<Comment> getCommentsByBlogPost(@PathVariable String blogPostId) {
        return commentRepository.findByBlogPostId(blogPostId);
    }

    @PostMapping
    public Comment addComment(@RequestBody Comment comment) {
        comment.setCreatedAt(java.time.LocalDateTime.now());
        return commentRepository.save(comment);
    }
}
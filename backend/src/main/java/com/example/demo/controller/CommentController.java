package com.example.demo.controller;

import com.example.demo.model.Comment;
import com.example.demo.model.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

class CommentWithProfileDTO {
    public String id;
    public String blogPostId;
    public String author;
    public String authorName;
    public String authorProfilePicture;
    public String content;
    public java.time.LocalDateTime createdAt;

    public CommentWithProfileDTO(Comment comment, String profilePicture) {
        this.id = comment.getId();
        this.blogPostId = comment.getBlogPostId();
        this.author = comment.getAuthor();
        this.authorName = comment.getAuthorName();
        this.authorProfilePicture = profilePicture;
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }
}

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "https://frontend-jh-74d9be1b01e4.herokuapp.com", allowCredentials = "true")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/post/{blogPostId}")
    public List<CommentWithProfileDTO> getCommentsByBlogPost(@PathVariable String blogPostId) {
        List<Comment> comments = commentRepository.findByBlogPostId(blogPostId);
        return comments.stream().map(comment -> {
            User user = userService.getUserByEmail(comment.getAuthor());
            return new CommentWithProfileDTO(comment, user != null ? user.getProfilePicture() : null);
        }).toList();
    }

    @PostMapping
    public Comment addComment(@RequestBody Comment comment) {
        comment.setCreatedAt(java.time.LocalDateTime.now());
        // If no profile picture is provided, try to get it from the user
        if (comment.getAuthorProfilePicture() == null) {
            User user = userService.getUserByEmail(comment.getAuthor());
            if (user != null) {
                comment.setAuthorProfilePicture(user.getProfilePicture());
            }
        }
        // If no authorName is provided, try to get it from the user
        if (comment.getAuthorName() == null) {
            User user = userService.getUserByEmail(comment.getAuthor());
            if (user != null) {
                comment.setAuthorName(user.getName());
            }
        }
        return commentRepository.save(comment);
    }
}
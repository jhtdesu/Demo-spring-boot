package com.example.demo.controller;

import com.example.demo.model.BlogPost;
import com.example.demo.model.User;
import com.example.demo.service.BlogPostService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

class BlogPostWithProfileDTO {
    public String id;
    public String title;
    public String content;
    public String author;
    public String authorName;
    public String authorProfilePicture;
    public java.time.LocalDateTime createdAt;
    public java.time.LocalDateTime updatedAt;
    public String[] tags;
    public boolean published;

    public BlogPostWithProfileDTO(BlogPost post, String profilePicture) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getAuthor();
        this.authorName = post.getAuthorName();
        this.authorProfilePicture = profilePicture;
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.tags = post.getTags();
        this.published = post.isPublished();
    }
}

@RestController
@RequestMapping("/api/blog")
@CrossOrigin(origins = "https://frontend-jh-74d9be1b01e4.herokuapp.com", allowCredentials = "true")
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;
    @Autowired
    private UserService userService;

    @GetMapping
    public List<BlogPostWithProfileDTO> getAllPosts() {
        List<BlogPost> posts = blogPostService.getAllPosts();
        return posts.stream().map(post -> {
            User user = userService.getUserByEmail(post.getAuthor());
            return new BlogPostWithProfileDTO(post, user != null ? user.getProfilePicture() : null);
        }).toList();
    }

    @GetMapping("/published")
    public List<BlogPostWithProfileDTO> getPublishedPosts() {
        List<BlogPost> posts = blogPostService.getPublishedPosts();
        return posts.stream().map(post -> {
            User user = userService.getUserByEmail(post.getAuthor());
            return new BlogPostWithProfileDTO(post, user != null ? user.getProfilePicture() : null);
        }).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostWithProfileDTO> getPostById(@PathVariable String id) {
        return blogPostService.getPostById(id)
                .map(post -> {
                    User user = userService.getUserByEmail(post.getAuthor());
                    return ResponseEntity
                            .ok(new BlogPostWithProfileDTO(post, user != null ? user.getProfilePicture() : null));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/author/{author}")
    public List<BlogPostWithProfileDTO> getPostsByAuthor(@PathVariable String author) {
        List<BlogPost> posts = blogPostService.getPostsByAuthor(author);
        return posts.stream().map(post -> {
            User user = userService.getUserByEmail(post.getAuthor());
            return new BlogPostWithProfileDTO(post, user != null ? user.getProfilePicture() : null);
        }).toList();
    }

    @GetMapping("/tag/{tag}")
    public List<BlogPostWithProfileDTO> getPostsByTag(@PathVariable String tag) {
        List<BlogPost> posts = blogPostService.getPostsByTag(tag);
        return posts.stream().map(post -> {
            User user = userService.getUserByEmail(post.getAuthor());
            return new BlogPostWithProfileDTO(post, user != null ? user.getProfilePicture() : null);
        }).toList();
    }

    @PostMapping
    public BlogPost createPost(@RequestBody BlogPost post) {
        return blogPostService.createPost(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPost> updatePost(@PathVariable String id, @RequestBody BlogPost post,
            @AuthenticationPrincipal Object principal) {
        String email = null;
        if (principal instanceof UserDetails userDetails) {
            email = userDetails.getUsername();
        } else if (principal instanceof OAuth2User oauth2User) {
            email = (String) oauth2User.getAttribute("email");
        }
        if (email == null) {
            return ResponseEntity.status(401).build();
        }
        BlogPost existingPost = blogPostService.getPostById(id).orElse(null);
        if (existingPost == null) {
            return ResponseEntity.notFound().build();
        }
        if (!existingPost.getAuthor().equals(email)) {
            return ResponseEntity.status(403).build();
        }
        return blogPostService.updatePost(id, post)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id, @AuthenticationPrincipal Object principal) {
        String email = null;
        if (principal instanceof UserDetails userDetails) {
            email = userDetails.getUsername();
        } else if (principal instanceof OAuth2User oauth2User) {
            email = (String) oauth2User.getAttribute("email");
        }
        if (email == null) {
            return ResponseEntity.status(401).build();
        }
        BlogPost existingPost = blogPostService.getPostById(id).orElse(null);
        if (existingPost == null) {
            return ResponseEntity.notFound().build();
        }
        User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        boolean isAuthor = existingPost.getAuthor().equals(email);
        boolean isModerator = user.isModerator();
        if (!isAuthor && !isModerator) {
            return ResponseEntity.status(403).build();
        }
        blogPostService.deletePost(id);
        return ResponseEntity.ok().build();
    }
}
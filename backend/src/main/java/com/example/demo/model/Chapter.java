package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import com.example.demo.model.Book;

@Document
public class Chapter {

    @Id
    private String id;
    private String title;
    private String content;
    @DBRef
    private Book book;

    // Existing constructor that takes three Strings
    public Chapter(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    // Add this no-argument constructor
    public Chapter() {
        // You can optionally initialize fields with default values here if needed
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Book getBook() {
        return this.book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
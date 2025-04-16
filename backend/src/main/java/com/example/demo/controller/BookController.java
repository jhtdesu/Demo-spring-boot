package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.model.Book;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/addBook")
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    @GetMapping("/getAllBooks")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }
}

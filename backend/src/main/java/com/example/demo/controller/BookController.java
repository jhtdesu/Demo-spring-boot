package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/addBookForm")
    public String showAddBookForm() {
        return "add-book";
    }

    @PostMapping("/addBook")
    public String addBook(@RequestParam String title, @RequestParam String author,
            @RequestParam String content, @RequestParam String chapter, Model model) {
        Book newBook = new Book(null, title, author, content, chapter);
        bookService.addBook(newBook);

        String message = "Book \"" + title + "\" by " + author + " added successfully!";
        model.addAttribute("message", message);
        return "add-book";
    }

    @ResponseBody
    @GetMapping("/getAllBooks")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @ResponseBody
    @GetMapping("/getBook/{id}")
    public Book getBook(@PathVariable String id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/editBookForm/{id}")
    public String showEditBookForm(@PathVariable String id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "edit-book";
    }

    @PostMapping("/editBook/{id}")
    public String editBook(@PathVariable String id,
            @RequestParam String title,
            @RequestParam String author,
            @RequestParam String content,
            @RequestParam String chapter,
            Model model) {
        Book updatedBook = new Book(id, title, author, content, chapter);
        bookService.updateBook(id, updatedBook);
        Book updatedBookFromDb = bookService.getBookById(id); // Re-fetch the updated book
        String message = "Book \"" + updatedBookFromDb.getTitle() + "\" updated successfully!";
        model.addAttribute("message", message);
        model.addAttribute("book", updatedBookFromDb); // Add the updated book back to the model
        return "edit-book";
    }
}
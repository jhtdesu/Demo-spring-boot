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
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.model.Chapter;
import com.example.demo.repository.ChapterRepository;
import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import com.example.demo.service.ChapterService;

import java.util.List;

@Controller // Changed to @Controller to support returning view names
public class ChapterController {

    @Autowired // Added @Autowired to inject ChapterService
    private ChapterService chapterService;

    @Autowired // Added @Autowired to inject BookService
    private BookService bookService;

    @GetMapping("/addChapterForm/{bookId}")
    public String showAddChapterForm(@PathVariable String bookId, Model model) {
        Book book = bookService.getBookById(bookId);
        if (book != null) {
            model.addAttribute("book", book); // Pass the book to the form for context
            model.addAttribute("chapter", new Chapter()); // Pass an empty Chapter object for the form to bind to
            return "add-chapter"; // The name of your Thymeleaf template for adding a chapter
        } else {
            // Handle the case where the book ID is not found
            return "error/book-not-found"; // Or redirect to an error page
        }
    }

    @PostMapping("/addChapter/{bookId}") // Updated mapping to be consistent with the form action
    public String saveNewChapter(@PathVariable String bookId, @ModelAttribute("chapter") Chapter chapter, Model model) {
        Book book = bookService.getBookById(bookId);
        if (book != null) {
            chapterService.addChapter(bookId, chapter); // Use your service to save the chapter
            String message = "Chapter '" + chapter.getTitle() + "' added successfully to '" + book.getTitle() + "'.";
            model.addAttribute("message", message);
            return "add-chapter"; // Display the success message on chapter-success.html
        } else {
            return "error/book-not-found";
        }
    }

    @ResponseBody
    @GetMapping("/getAllChapters")
    public List<Chapter> getAllChapters() {
        return chapterService.getAllChapters(); // Now using the injected instance
    }

    // You can add other chapter-related endpoints here
}
package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.Chapter;
import com.example.demo.repository.ChapterRepository;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Service
public class ChapterService {
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private BookRepository bookRepository;

    public Chapter addChapter(String bookId, Chapter chapter) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            chapter.setBook(book); // Set the reference to the existing Book
            return chapterRepository.save(chapter);
        }
        return null;
    }

    public List<Chapter> getAllChapters() {
        return chapterRepository.findAll(); // Correctly calling findAll() on the injected instance
    }

    public Chapter getChapterById(String id) {
        return chapterRepository.findById(id).orElse(null); // Correctly calling findById() on the injected instance
    }
}

package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;

@Service
public class BookService {
    private BookRepository bookRepository;

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}

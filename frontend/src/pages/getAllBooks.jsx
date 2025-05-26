import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './getAllBooks.css';

const BookList = () => {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchBooks = async () => {
      try {
        const response = await axios.get('http://localhost:8086/getAllBooks', { withCredentials: true });
        if (Array.isArray(response.data)) {
          setBooks(response.data);
          console.log('Books:', response.data);
        } else {
          console.warn('Invalid book data:', response.data);
          setBooks([]);
        }
      } catch (err) {
        console.error('Failed to load books:', err);
        setError('Failed to load books.');
      } finally {
        setLoading(false);
      }
    };

    fetchBooks();
  }, []);

  if (loading) return (
    <div className="loading-container">
      Loading books...
    </div>
  );
  
  if (error) return (
    <div className="error-container">
      {error}
    </div>
  );
  
  if (books.length === 0) return (
    <div className="no-books-container">
      No books found.
    </div>
  );

  return (
    <div className="book-list-container">
      <h1 className="book-list-title">
        Book Collection
      </h1>
      <div className="book-grid">
        {books.map(book => (
          <div key={book.id} className="book-card">
            <a 
              href={`/getAllChapters`} 
              className="book-link"
            >
              <h2 className="book-title">
                {book.title}
              </h2>
              <p className="book-author">
                By {book.author}
              </p>
            </a>
          </div>
        ))}
      </div>
    </div>
  );
};

export default BookList;
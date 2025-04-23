import React, { useState, useEffect } from 'react';
import axios from 'axios';

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

  if (loading) return <div>Loading books...</div>;
  if (error) return <div style={{ color: 'red' }}>{error}</div>;
  if (books.length === 0) return <p>No books found.</p>;

  return (
    <div>
      <h1>Book List</h1>
      <ul>
        {books.map(book => (
          <li key={book.id}>
            Title: {book.title}, Author: {book.author}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default BookList;
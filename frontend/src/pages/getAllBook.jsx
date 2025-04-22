import React, { useState, useEffect } from 'react';
import axios from 'axios';

const BookList = () => {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchBooks = async () => {
      try {
        const response = await axios.get('http://localhost:8086/getAllBooks'); // Replace with your actual backend endpoint
        if (response.data && Array.isArray(response.data)) {
          setBooks(response.data);
          console.log('Books fetched successfully:', response.data); // Log the fetched books
        } else {
          console.warn('Unexpected response format:', response.data);
          setBooks([]);
        }
        setLoading(false);
      } catch (error) {
        console.error('Error fetching books:', error);
        setError('Failed to fetch books.');
        setLoading(false);
      }
    };

    fetchBooks();
  }, []);

  if (loading) {
    return <div>Loading books...</div>;
  }

  if (error) {
    return <div style={{ color: 'red' }}>{error}</div>;
  }

  return (
    <div>
      <h1>Book List</h1>
      {books.length > 0 ? (
        <ul>
          {books.map(book => (
            <li key={book.id}>
              Title: {book.title}, Author: {book.author} {/* Adjust based on your book object structure */}
            </li>
          ))}
        </ul>
      ) : (
        <p>No books found.</p>
      )}
    </div>
  );
};

export default BookList;
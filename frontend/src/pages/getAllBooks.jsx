import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './getAllBooks.css';
import Navbar from '../components/Navbar';

const BookList = () => {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [search, setSearch] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const booksPerPage = 6; // Number of books to show per page

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

  // Filter books based on search term
  const filteredBooks = books.filter(book =>
    book.title.toLowerCase().includes(search.toLowerCase()) ||
    book.author.toLowerCase().includes(search.toLowerCase())
  );

  // Calculate pagination for filtered books
  const indexOfLastBook = currentPage * booksPerPage;
  const indexOfFirstBook = indexOfLastBook - booksPerPage;
  const currentBooks = filteredBooks.slice(indexOfFirstBook, indexOfLastBook);
  const totalPages = Math.ceil(filteredBooks.length / booksPerPage);

  // Reset to first page when search changes
  useEffect(() => {
    setCurrentPage(1);
  }, [search]);

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  if (loading) return (
    <>
      <Navbar />
      <div className="loading-container">
        <div className="loading-spinner"></div>
        <p>Loading our library collection...</p>
      </div>
    </>
  );
  
  if (error) return (
    <>
      <Navbar />
      <div className="error-container">
        <i className="error-icon">⚠️</i>
        {error}
      </div>
    </>
  );
  
  if (books.length === 0) return (
    <>
      <Navbar />
      <div className="no-books-container">
        <i className="empty-icon">📚</i>
        <p>No books found in our collection.</p>
      </div>
    </>
  );

  return (
    <>
      <Navbar />
      <div className="library-container">
        <div className="library-header">
          <h1 className="library-title">Our Library Collection</h1>
          <p className="library-subtitle">Discover our curated selection of books</p>
        </div>
        <div style={{ display: 'flex', justifyContent: 'center', marginBottom: 32 }}>
          <input
            type="text"
            placeholder="Search by title or author..."
            value={search}
            onChange={e => setSearch(e.target.value)}
            style={{
              width: 320,
              padding: '12px 18px',
              borderRadius: 8,
              border: '1px solid #ccc',
              fontSize: '1rem',
              boxShadow: '0 2px 8px rgba(44,62,80,0.04)'
            }}
          />
        </div>
        <div className="book-grid">
          {currentBooks.length === 0 ? (
            <div style={{ gridColumn: '1/-1', textAlign: 'center', color: '#888', fontStyle: 'italic' }}>
              No books match your search.
            </div>
          ) : (
            currentBooks.map(book => (
              <div key={book.id} className="book-card">
                <div className="book-cover">
                  <div className="book-spine"></div>
                  <div className="book-pages"></div>
                </div>
                <div className="book-info">
                  <h2 className="book-title">{book.title}</h2>
                  <p className="book-author">By {book.author}</p>
                  {book.publicationYear && (
                    <p className="book-year">Published: {book.publicationYear}</p>
                  )}
                  {book.description && (
                    <p className="book-description">{book.description}</p>
                  )}
                  <a 
                    href={`/getBook/${book.id}`} 
                    className="read-more-btn"
                  >
                    View Chapters
                  </a>
                </div>
              </div>
            ))
          )}
        </div>
        
        {/* Pagination Controls */}
        {totalPages > 1 && (
          <div className="pagination-controls">
            <button
              onClick={() => handlePageChange(currentPage - 1)}
              disabled={currentPage === 1}
              className="pagination-btn"
            >
              Previous
            </button>
            <div className="page-numbers">
              {[...Array(totalPages)].map((_, index) => (
                <button
                  key={index + 1}
                  onClick={() => handlePageChange(index + 1)}
                  className={`page-number ${currentPage === index + 1 ? 'active' : ''}`}
                >
                  {index + 1}
                </button>
              ))}
            </div>
            <button
              onClick={() => handlePageChange(currentPage + 1)}
              disabled={currentPage === totalPages}
              className="pagination-btn"
            >
              Next
            </button>
          </div>
        )}
      </div>
    </>
  );
};

export default BookList;
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom'; // Import useParams
import Navbar from '../components/Navbar';

const GetBookById = () => {

    const { id: bookIdFromUrl } = useParams();

    const [book, setBook] = useState(null);
    const [chapters, setChapters] = useState([]);
    const [loading, setLoading] = useState(true); // Start loading initially
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchBookAndChapters = async () => {
            // --- Check if the ID was successfully extracted from the URL ---
            if (!bookIdFromUrl) {
                console.log("No book ID found in URL parameters.");
                setError('No Book ID found in URL.'); // Inform the user
                setLoading(false); // Stop loading state
                setBook(null);    // Ensure book state is null
                setChapters([]);  // Clear previous chapters
                return;           // Stop execution
            }

            // --- Reset state for the fetch attempt ---
            setLoading(true);
            setError('');
            setBook(null); // Clear previous book data
            setChapters([]); // Clear previous chapters

            try {
                // --- Use the bookIdFromUrl obtained from useParams ---
                console.log(`Workspaceing book with ID from URL: ${bookIdFromUrl}`);
                const bookRes = await axios.get(`https://backend-jh-cff06dd28ef7.herokuapp.com/getBook/${bookIdFromUrl}`, { withCredentials: true });

                // --- Handle the response ---
                if (bookRes.data && typeof bookRes.data === 'object' && !Array.isArray(bookRes.data)) {
                    setBook(bookRes.data);
                    console.log('Book received:', bookRes.data);
                } else {
                    console.warn(`Invalid or unexpected book data received for ID ${bookIdFromUrl}:`, bookRes.data);
                    setError('Book not found or invalid data format returned.');
                    setBook(null);
                }

                // Fetch all chapters
                const chaptersRes = await axios.get('https://backend-jh-cff06dd28ef7.herokuapp.com/getAllChapters', { withCredentials: true });
                if (Array.isArray(chaptersRes.data)) {
                    // Filter chapters for this book
                    const filtered = chaptersRes.data.filter(ch => ch.book && ch.book.id === bookIdFromUrl);
                    setChapters(filtered);
                } else {
                    setChapters([]);
                }
            } catch (err) {
                // --- Handle errors ---
                console.error(`Failed to load book with ID ${bookIdFromUrl}:`, err);
                 if (err.response) {
                    setError(`Failed to load book. Server responded with status: ${err.response.status}`);
                } else if (err.request) {
                     setError('Failed to load book. Cannot reach server.');
                } else {
                    setError('An error occurred while trying to fetch the book.');
                }
                setBook(null);
                setChapters([]);
            } finally {
                // --- Stop loading state ---
                setLoading(false);
            }
        };

        fetchBookAndChapters(); // Execute the fetch function

    // --- Dependency Array ---
    // Add bookIdFromUrl to the dependency array.
    // This ensures the useEffect hook re-runs if the ID in the URL changes.
    }, [bookIdFromUrl]);

    // --- Render Logic (same as before) ---
    if (loading) {
        return (
            <>
                <Navbar />
                <div>Loading book details...</div>
            </>
        );
     }

    if (error) {
        return (
            <>
                <Navbar />
                <div style={{ color: 'red' }}>{error}</div>
            </>
        );
    }

    if (!book) {
        return (
            <>
                <Navbar />
                <p>Book details could not be loaded or the book was not found.</p>
            </>
        );
    }

    return (
        <>
            <Navbar />
            <div style={{
                minHeight: 'calc(100vh - 80px)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                background: 'none',
                marginTop: '80px',
            }}>
                <div style={{
                    width: '100%',
                    maxWidth: 800,
                    padding: '32px',
                    background: 'rgba(255,255,255,0.95)',
                    borderRadius: '18px',
                    boxShadow: '0 4px 24px rgba(44,62,80,0.10)',
                    fontFamily: 'Segoe UI, Tahoma, Geneva, Verdana, sans-serif'
                }}>
                    <h1 style={{ fontSize: '2.5rem', color: '#2c3e50', marginBottom: 24, fontWeight: 700, letterSpacing: '-1px' }}>Book Details</h1>
                    <ul style={{ listStyle: 'none', padding: 0, marginBottom: 32 }}>
                        <li style={{ marginBottom: 10 }}><span style={{ color: '#888', fontWeight: 500 }}>ID:</span> <span style={{ color: '#222' }}>{book.id}</span></li>
                        <li style={{ marginBottom: 10 }}><span style={{ color: '#888', fontWeight: 500 }}>Title:</span> <span style={{ color: '#222', fontWeight: 600 }}>{book.title}</span></li>
                        <li style={{ marginBottom: 10 }}><span style={{ color: '#888', fontWeight: 500 }}>Author:</span> <span style={{ color: '#222' }}>{book.author}</span></li>
                    </ul>
                    <h2 style={{ fontSize: '1.5rem', color: '#3498db', marginBottom: 16, fontWeight: 600 }}>Chapters</h2>
                    {chapters.length === 0 ? (
                        <p style={{ color: '#888', fontStyle: 'italic' }}>No chapters found for this book.</p>
                    ) : (
                        <ul style={{ listStyle: 'none', padding: 0, margin: 0 }}>
                            {chapters.map(chapter => (
                                <li key={chapter.id} style={{ marginBottom: 14 }}>
                                    <a href={`/getChapter/${chapter.id}`} style={{ display: 'inline-block', padding: '12px 20px', background: '#f4f8fb', borderRadius: '8px', color: '#3498db', fontWeight: 600, textDecoration: 'none', boxShadow: '0 2px 8px rgba(52,152,219,0.07)', transition: 'background 0.2s, color 0.2s' }}
                                        onMouseOver={e => { e.target.style.background = '#eaf3fa'; e.target.style.color = '#217dbb'; }}
                                        onMouseOut={e => { e.target.style.background = '#f4f8fb'; e.target.style.color = '#3498db'; }}
                                    >
                                        {chapter.title}
                                    </a>
                                </li>
                            ))}
            </ul>
                    )}
                </div>
        </div>
        </>
    );
};

export default GetBookById;
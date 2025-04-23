import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom'; // Import useParams

const GetBookById = () => {

    const { id: bookIdFromUrl } = useParams();

    const [book, setBook] = useState(null);
    const [loading, setLoading] = useState(true); // Start loading initially
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchBook = async () => {
            // --- Check if the ID was successfully extracted from the URL ---
            if (!bookIdFromUrl) {
                console.log("No book ID found in URL parameters.");
                setError('No Book ID found in URL.'); // Inform the user
                setLoading(false); // Stop loading state
                setBook(null);    // Ensure book state is null
                return;           // Stop execution
            }

            // --- Reset state for the fetch attempt ---
            setLoading(true);
            setError('');
            setBook(null); // Clear previous book data

            try {
                // --- Use the bookIdFromUrl obtained from useParams ---
                console.log(`Workspaceing book with ID from URL: ${bookIdFromUrl}`);
                const response = await axios.get(`http://localhost:8086/getBook/${bookIdFromUrl}`, { withCredentials: true });

                // --- Handle the response ---
                if (response.data && typeof response.data === 'object' && !Array.isArray(response.data)) {
                    setBook(response.data);
                    console.log('Book received:', response.data);
                } else {
                    console.warn(`Invalid or unexpected book data received for ID ${bookIdFromUrl}:`, response.data);
                    setError('Book not found or invalid data format returned.');
                    setBook(null);
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
            } finally {
                // --- Stop loading state ---
                setLoading(false);
            }
        };

        fetchBook(); // Execute the fetch function

    // --- Dependency Array ---
    // Add bookIdFromUrl to the dependency array.
    // This ensures the useEffect hook re-runs if the ID in the URL changes.
    }, [bookIdFromUrl]);

    // --- Render Logic (same as before) ---
    if (loading) {
        return <div>Loading book details...</div>;
     }

    if (error) {
        return <div style={{ color: 'red' }}>{error}</div>;
    }

    if (!book) {
         return <p>Book details could not be loaded or the book was not found.</p>;
    }

    return (
        <div>
            <h1>Book Details</h1>
            <ul>
                <li><strong>ID:</strong> {book.id}</li>
                <li><strong>Title:</strong> {book.title}</li>
                <li><strong>Author:</strong> {book.author}</li>
                <li><strong>Chapters:</strong> {book.chapter}</li>
            </ul>
        </div>
    );
};

export default GetBookById;
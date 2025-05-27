import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom'; // Import useParams
import Navbar from '../components/Navbar';

const GetChapterById = () => {
    const { id: chapterIdFromUrl } = useParams();

    const [chapter, setChapter] = useState(null);
    const [loading, setLoading] = useState(true); // Start loading initially
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchChapter = async () => {
            // --- Check if the ID was successfully extracted from the URL ---
            if (!chapterIdFromUrl) {
                console.log("No chapter ID found in URL parameters.");
                setError('No Chapter ID found in URL.'); // Inform the user
                setLoading(false); // Stop loading state
                setChapter(null);  // Ensure chapter state is null
                return;            // Stop execution
            }

            // --- Reset state for the fetch attempt ---
            setLoading(true);
            setError('');
            setChapter(null); // Clear previous chapter data

            try {
                // --- Use the chapterIdFromUrl obtained from useParams ---
                console.log(`Fetching chapter with ID from URL: ${chapterIdFromUrl}`);
                const response = await axios.get(`http://localhost:8086/getChapter/${chapterIdFromUrl}`, { withCredentials: true });

                // --- Handle the response ---
                if (response.data && typeof response.data === 'object' && !Array.isArray(response.data)) {
                    setChapter(response.data);
                    console.log('Chapter received:', response.data);
                } else {
                    console.warn(`Invalid or unexpected chapter data received for ID ${chapterIdFromUrl}:`, response.data);
                    setError('Chapter not found or invalid data format returned.');
                    setChapter(null);
                }
            } catch (err) {
                // --- Handle errors ---
                console.error(`Failed to load chapter with ID ${chapterIdFromUrl}:`, err);
                if (err.response) {
                    setError(`Failed to load chapter. Server responded with status: ${err.response.status}`);
                } else if (err.request) {
                    setError('Failed to load chapter. Cannot reach server.');
                } else {
                    setError('An error occurred while trying to fetch the chapter.');
                }
                setChapter(null);
            } finally {
                // --- Stop loading state ---
                setLoading(false);
            }
        };

        fetchChapter(); // Execute the fetch function

        // --- Dependency Array ---
        // Add chapterIdFromUrl to the dependency array.
        // This ensures the useEffect hook re-runs if the ID in the URL changes.
    }, [chapterIdFromUrl]);

    // --- Render Logic ---
    if (loading) {
        return (
            <>
                <Navbar />
                <div>Loading chapter details...</div>
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

    if (!chapter) {
        return (
            <>
                <Navbar />
                <p>Chapter details could not be loaded or the chapter was not found.</p>
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
                    background: 'rgba(255,255,255,0.97)',
                    borderRadius: '18px',
                    boxShadow: '0 4px 24px rgba(44,62,80,0.10)',
                    fontFamily: 'Segoe UI, Tahoma, Geneva, Verdana, sans-serif'
                }}>
                    <h1 style={{ fontSize: '2.2rem', color: '#2c3e50', marginBottom: 24, fontWeight: 700, letterSpacing: '-1px' }}>Chapter Details</h1>
                    <ul style={{ listStyle: 'none', padding: 0, marginBottom: 32 }}>
                        <li style={{ marginBottom: 18 }}>
                            <span style={{ color: '#888', fontWeight: 500 }}>ID:</span> <span style={{ color: '#222' }}>{chapter.id}</span>
                        </li>
                        <li style={{ marginBottom: 18 }}>
                            <span style={{ color: '#888', fontWeight: 500 }}>Title:</span> <span style={{ color: '#3498db', fontWeight: 600, fontSize: '1.2rem' }}>{chapter.title}</span>
                        </li>
                        <li>
                            <span style={{ color: '#888', fontWeight: 500 }}>Content:</span>
                            <div style={{
                                background: '#f4f8fb',
                                borderRadius: '8px',
                                padding: '18px',
                                marginTop: '8px',
                                color: '#222',
                                fontSize: '1.08rem',
                                lineHeight: 1.7,
                                boxShadow: '0 2px 8px rgba(52,152,219,0.07)',
                                wordBreak: 'break-word',
                                overflowX: 'auto'
                            }}>{chapter.content}</div>
                </li>
            </ul>
        </div>
            </div>
        </>
    );
};

export default GetChapterById;
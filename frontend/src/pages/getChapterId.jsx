import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom'; // Import useParams

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
        return <div>Loading chapter details...</div>;
    }

    if (error) {
        return <div style={{ color: 'red' }}>{error}</div>;
    }

    if (!chapter) {
        return <p>Chapter details could not be loaded or the chapter was not found.</p>;
    }

    return (
        <div style={{ overflow: 'auto', maxHeight: '80vh', padding: '10px' }}>
            <h1>Chapter Details</h1>
            <ul style={{ listStyleType: 'none', padding: 0 }}>
                <li style={{ marginBottom: '10px' }}>
                    <div style={{ wordWrap: 'break-word', maxWidth: '1200px', border: '1px solid #ccc', padding: '10px', borderRadius: '5px' }}>
                        <strong>ID:</strong> {chapter.id} <br />
                        <strong>Title:</strong> {chapter.title} <br />
                        <strong>Content:</strong> {chapter.content}
                    </div>
                </li>
            </ul>
        </div>
    );
};

export default GetChapterById;
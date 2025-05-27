import React, { useState, useEffect } from 'react';
import axios from 'axios';

const ChapterList = () => {
    const [chapters, setChapters] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchChapters = async () => {
            try {
                const response = await axios.get('http://localhost:8080/getAllChapters', { withCredentials: true });
                if (Array.isArray(response.data)) {
                    setChapters(response.data);
                    console.log('Chapters:', response.data);
                } else {
                    console.warn('Invalid chapter data:', response.data);
                    setChapters([]);
                }
            } catch (err) {
                console.error('Failed to load chapters:', err);
                setError('Failed to load chapters.');
            } finally {
                setLoading(false);
            }
        };

        fetchChapters();
    }, []);

    if (loading) return <div>Loading chapters...</div>;
    if (error) return <div style={{ color: 'red' }}>{error}</div>;
    if (chapters.length === 0) return <p>No chapters found.</p>;

    return (
        <div style={{ overflow: 'auto', maxHeight: '80vh', padding: '10px' }}>
            <h1>Chapter List</h1>
            <ul style={{ listStyleType: 'none', padding: 0 }}>
                {chapters.map(chapter => (
                    <li key={chapter.id} style={{ marginBottom: '10px' }}>
                        <div style={{ wordWrap: 'break-word', maxWidth: '1200px', border: '1px solid #ccc', padding: '10px', borderRadius: '5px' }}>
                            <strong>Title:</strong> {chapter.title} <br />
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ChapterList;
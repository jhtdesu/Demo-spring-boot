import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { blogApi } from '../../api/blogApi';
import './BlogList.css';

const BlogList = () => {
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchPosts = async () => {
            try {
                const response = await blogApi.getPublishedPosts();
                console.log('API response:', response.data);
                if (Array.isArray(response.data)) {
                    setPosts(response.data);
                } else {
                    setError('Unexpected response from server');
                    setPosts([]);
                }
                setLoading(false);
            } catch {
                setError('Failed to fetch blog posts');
                setLoading(false);
            }
        };

        fetchPosts();
    }, []);

    if (loading) return <div className="loading">Loading...</div>;
    if (error) return <div className="error">{error}</div>;

    return (
        <div className="blog-list">
            <h1>Blog Posts</h1>
            <Link to="/blog/new" className="new-post-button">Create New Post</Link>
            <div className="posts-grid">
                {posts.map((post) => (
                    <div key={post.id} className="post-card">
                        <h2>{post.title}</h2>
                        <p className="post-meta">
                            By {post.author} • {new Date(post.createdAt).toLocaleDateString()}
                        </p>
                        <p className="post-excerpt">
                            {post.content.substring(0, 150)}...
                        </p>
                        <div className="post-tags">
                            {post.tags?.map((tag, index) => (
                                <span key={index} className="tag">{tag}</span>
                            ))}
                        </div>
                        <Link to={`/blog/${post.id}`} className="read-more">
                            Read More
                        </Link>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default BlogList; 
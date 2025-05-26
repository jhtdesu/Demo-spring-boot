import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { blogApi } from '../../api/blogApi';
import './BlogPost.css';

const BlogPost = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [post, setPost] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchPost = async () => {
            try {
                const response = await blogApi.getPostById(id);
                setPost(response.data);
                setLoading(false);
            } catch {
                setError('Failed to fetch blog post');
                setLoading(false);
            }
        };

        fetchPost();
    }, [id]);

    const handleDelete = async () => {
        if (window.confirm('Are you sure you want to delete this post?')) {
            try {
                await blogApi.deletePost(id);
                navigate('/blog');
            } catch {
                setError('Failed to delete post');
            }
        }
    };

    if (loading) return <div className="loading">Loading...</div>;
    if (error) return <div className="error">{error}</div>;
    if (!post) return <div className="error">Post not found</div>;

    return (
        <div className="blog-post">
            <div className="post-header">
                <h1>{post.title}</h1>
                <div className="post-meta">
                    <span>By {post.author}</span>
                    <span>•</span>
                    <span>{new Date(post.createdAt).toLocaleDateString()}</span>
                </div>
                <div className="post-tags">
                    {post.tags?.map((tag, index) => (
                        <span key={index} className="tag">{tag}</span>
                    ))}
                </div>
            </div>
            <div className="post-content">
                {post.content}
            </div>
            <div className="post-actions">
                <button 
                    className="edit-button"
                    onClick={() => navigate(`/blog/edit/${id}`)}
                >
                    Edit Post
                </button>
                <button 
                    className="delete-button"
                    onClick={handleDelete}
                >
                    Delete Post
                </button>
            </div>
        </div>
    );
};

export default BlogPost; 
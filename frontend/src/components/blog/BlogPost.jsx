import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { blogApi } from '../../api/blogApi';
import Navbar from '../Navbar';
import { useAuth } from '../../context/AuthContext';
import './BlogPost.css';

const BlogPost = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const { user, isAuthenticated } = useAuth();
    const [post, setPost] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [comments, setComments] = useState([]);
    const [commentContent, setCommentContent] = useState('');
    const [commentError, setCommentError] = useState(null);
    const [commentLoading, setCommentLoading] = useState(false);

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
        const fetchComments = async () => {
            try {
                const res = await blogApi.getCommentsByPost(id);
                setComments(res.data);
            } catch {
                setComments([]);
            }
        };
        fetchPost();
        fetchComments();
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

    const handleCommentSubmit = async (e) => {
        e.preventDefault();
        setCommentError(null);
        setCommentLoading(true);
        try {
            await blogApi.addComment({
                blogPostId: id,
                author: user?.username || user?.name || 'Anonymous',
                content: commentContent
            });
            setCommentContent('');
            // Refresh comments
            const res = await blogApi.getCommentsByPost(id);
            setComments(res.data);
        } catch {
            setCommentError('Failed to add comment');
        } finally {
            setCommentLoading(false);
        }
    };

    if (loading) return (
        <>
            <Navbar />
            <div className="loading">Loading...</div>
        </>
    );
    
    if (error) return (
        <>
            <Navbar />
            <div className="error">{error}</div>
        </>
    );
    
    if (!post) return (
        <>
            <Navbar />
            <div className="error">Post not found</div>
        </>
    );

    return (
        <>
            <Navbar />
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
                {/* Comment Section */}
                <div className="comment-section" style={{ marginTop: 40 }}>
                    <h2 style={{ fontSize: '1.3rem', color: '#3498db', marginBottom: 16 }}>Comments</h2>
                    {comments.length === 0 ? (
                        <p style={{ color: '#888', fontStyle: 'italic' }}>No comments yet.</p>
                    ) : (
                        <ul style={{ listStyle: 'none', padding: 0 }}>
                            {comments.map(comment => (
                                <li key={comment.id} style={{ marginBottom: 18, background: '#f4f8fb', borderRadius: 8, padding: 16 }}>
                                    <div style={{ fontWeight: 600, color: '#2c3e50' }}>{comment.author}</div>
                                    <div style={{ color: '#555', margin: '6px 0 0 0' }}>{comment.content}</div>
                                    <div style={{ fontSize: '0.85rem', color: '#aaa', marginTop: 4 }}>{new Date(comment.createdAt).toLocaleString()}</div>
                                </li>
                            ))}
                        </ul>
                    )}
                    {isAuthenticated && (
                        <form onSubmit={handleCommentSubmit} style={{ marginTop: 24 }}>
                            <textarea
                                value={commentContent}
                                onChange={e => setCommentContent(e.target.value)}
                                placeholder="Add a comment..."
                                rows={3}
                                style={{ width: '100%', borderRadius: 6, border: '1px solid #ddd', padding: 10, fontSize: '1rem', resize: 'vertical' }}
                                required
                            />
                            {commentError && <div className="error" style={{ marginTop: 8 }}>{commentError}</div>}
                            <button
                                type="submit"
                                className="submit-button"
                                style={{ marginTop: 10 }}
                                disabled={commentLoading || !commentContent.trim()}
                            >
                                {commentLoading ? 'Posting...' : 'Post Comment'}
                            </button>
                        </form>
                    )}
                    {!isAuthenticated && (
                        <div style={{ color: '#888', marginTop: 16 }}>
                            Please log in to add a comment.
                        </div>
                    )}
                </div>
        </div>
        </>
    );
};

export default BlogPost; 
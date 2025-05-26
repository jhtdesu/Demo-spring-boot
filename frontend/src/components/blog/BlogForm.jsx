import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { blogApi } from '../../api/blogApi';
import './BlogForm.css';

const BlogForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        title: '',
        content: '',
        author: '',
        tags: '',
        published: true
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (id) {
            const fetchPost = async () => {
                try {
                    const response = await blogApi.getPostById(id);
                    const post = response.data;
                    setFormData({
                        ...post,
                        tags: post.tags?.join(', ') || ''
                    });
                } catch {
                    setError('Failed to fetch post');
                }
            };
            fetchPost();
        }
    }, [id]);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        try {
            const postData = {
                ...formData,
                tags: formData.tags.split(',').map(tag => tag.trim()).filter(tag => tag)
            };

            if (id) {
                await blogApi.updatePost(id, postData);
            } else {
                await blogApi.createPost(postData);
            }
            navigate('/blog');
        } catch {
            setError('Failed to save post');
            setLoading(false);
        }
    };

    return (
        <div className="blog-form">
            <h1>{id ? 'Edit Post' : 'Create New Post'}</h1>
            {error && <div className="error">{error}</div>}
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="title">Title</label>
                    <input
                        type="text"
                        id="title"
                        name="title"
                        value={formData.title}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="author">Author</label>
                    <input
                        type="text"
                        id="author"
                        name="author"
                        value={formData.author}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="content">Content</label>
                    <textarea
                        id="content"
                        name="content"
                        value={formData.content}
                        onChange={handleChange}
                        required
                        rows="10"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="tags">Tags (comma-separated)</label>
                    <input
                        type="text"
                        id="tags"
                        name="tags"
                        value={formData.tags}
                        onChange={handleChange}
                        placeholder="e.g., technology, programming, web"
                    />
                </div>

                <div className="form-group checkbox">
                    <label>
                        <input
                            type="checkbox"
                            name="published"
                            checked={formData.published}
                            onChange={handleChange}
                        />
                        Publish immediately
                    </label>
                </div>

                <div className="form-actions">
                    <button 
                        type="button" 
                        className="cancel-button"
                        onClick={() => navigate('/blog')}
                    >
                        Cancel
                    </button>
                    <button 
                        type="submit" 
                        className="submit-button"
                        disabled={loading}
                    >
                        {loading ? 'Saving...' : (id ? 'Update Post' : 'Create Post')}
                    </button>
                </div>
            </form>
        </div>
    );
};

export default BlogForm; 
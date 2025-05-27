import axios from 'axios';

const API_URL = 'https://backend-jh-cff06dd28ef7.herokuapp.com/api/blog';
const COMMENT_URL = 'https://backend-jh-cff06dd28ef7.herokuapp.com/api/comments';

const axiosInstance = axios.create({
  withCredentials: true
});

export const blogApi = {
    getAllPosts: () => axiosInstance.get(API_URL),
    getPublishedPosts: () => axiosInstance.get(`${API_URL}/published`),
    getPostById: (id) => axiosInstance.get(`${API_URL}/${id}`),
    getPostsByAuthor: (author) => axiosInstance.get(`${API_URL}/author/${author}`),
    getPostsByTag: (tag) => axiosInstance.get(`${API_URL}/tag/${tag}`),
    createPost: (post) => axiosInstance.post(API_URL, post),
    updatePost: (id, post) => axiosInstance.put(`${API_URL}/${id}`, post),
    deletePost: (id) => axiosInstance.delete(`${API_URL}/${id}`),
    // Comment API
    getCommentsByPost: (blogPostId) => axiosInstance.get(`${COMMENT_URL}/post/${blogPostId}`),
    addComment: (comment) => axiosInstance.post(COMMENT_URL, comment)
}; 
import axios from 'axios';

const API_URL = 'http://localhost:8086/api/blog';

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
    deletePost: (id) => axiosInstance.delete(`${API_URL}/${id}`)
}; 
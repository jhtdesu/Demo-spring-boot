import React from 'react';
import { Routes, Route, Link } from 'react-router-dom';
import BlogList from './components/blog/BlogList';
import BlogPost from './components/blog/BlogPost';
import BlogForm from './components/blog/BlogForm';
import UserProfile from './pages/UserProfile';
import './App.css';

function App() {
  return (
    <div className="app">
      <nav className="navbar">
        <div className="nav-content">
          <Link to="/blog" className="nav-logo">My Blog</Link>
          <div className="nav-links">
            <Link to="/blog" className="nav-link">Blog</Link>
            <Link to="/blog/new" className="nav-link">New Post</Link>
          </div>
        </div>
      </nav>

      <main className="main-content">
        <Routes>
          <Route path="/" element={<BlogList />} />
          <Route path="new" element={<BlogForm />} />
          <Route path="edit/:id" element={<BlogForm />} />
          <Route path=":id" element={<BlogPost />} />
          <Route path="profile" element={<UserProfile />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;
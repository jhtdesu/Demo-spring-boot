import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import '../styles/navbar.css';

const Navbar = () => {
  const { isAuthenticated, logout } = useAuth();

  const handleLogout = async () => {
    await logout();
  };

  return (
    <nav className="navbar">
      <div className="navbar-logo">
        <Link to="/" className="navbar-brand">📖 BookVerse</Link>
      </div>
      <div className="navbar-links">
        <Link to="/" className="nav-link">Trang chủ</Link>
        <Link to="/getAllBooks" className="nav-link">Tất cả sách</Link>
        <Link to="/blog" className="nav-link">Blog</Link>
        {isAuthenticated ? (
          <button onClick={handleLogout} className="nav-link logout-btn" title="Logout" style={{ fontSize: '1.3rem', background: 'none', border: 'none', padding: 0 }}>
            <span role="img" aria-label="logout">🔓</span>
          </button>
        ) : (
          <Link to="/login" className="nav-link" title="Login" style={{ fontSize: '1.3rem' }}>
            <span role="img" aria-label="login">🔒</span>
          </Link>
        )}
      </div>
    </nav>
  );
};

export default Navbar; 
import React from 'react';
import '../styles/home.css';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Navbar from '../components/Navbar';

const Home = () => {
  const { isAuthenticated } = useAuth();

  return (
    <>
      <Navbar />
      <div className="hero-section">
        <div className="overlay" />
        <div className="hero-content">
          <h1 className="hero-title">
            Chào mừng đến với <span className="hero-highlight">BookVerse</span>
          </h1>
          <p className="hero-subtitle">
            Khám phá kho sách khổng lồ với vô vàn câu chuyện chờ bạn mở ra!
          </p>

          <div className="hero-buttons">
            {!isAuthenticated && (
              <Link to="/login" className="hero-btn login-btn">Đăng nhập</Link>
            )}
            <Link to="/getAllBooks" className="hero-btn books-btn">Tất cả sách</Link>
            <Link to="/blog" className="hero-btn blog-btn">Blog</Link>
          </div>
        </div>
      </div>
    </>
  );
};

export default Home;

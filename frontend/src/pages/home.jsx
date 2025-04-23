import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/home.css';

const Home = () => {
  const navigate = useNavigate();
  const isAuth = Boolean(localStorage.getItem('token'));

  return (
    <div className="home-wrapper">
      <nav className="navbar">
        <div className="navbar-brand">📖 BookVerse</div>
        <ul className="nav-links">
          <li><Link to="/getAllBooks">Tất cả sách</Link></li>
          {isAuth ? (
            <li>
              <button
                onClick={() => { localStorage.removeItem('token'); navigate('/login'); }}
                className="btn-logout"
              >
                Đăng xuất
              </button>
            </li>
          ) : (
            <li>
              <Link to="/login" className="btn-login-nav">Đăng nhập</Link>
            </li>
          )}
        </ul>
      </nav>

      <main className="home-content" style={{ textAlign: 'center' }}>
        <h1>Chào mừng đến với BookVerse</h1>
        <p>
          Nơi hội tụ những câu chuyện tuyệt vời. Hãy khám phá kho sách khổng lồ của chúng tôi!
        </p>
        {isAuth && (
          <button className="btn-primary" onClick={() => navigate('/books')}>
            Xem tất cả sách
          </button>
        )}
      </main>
    </div>
  );
};

export default Home;

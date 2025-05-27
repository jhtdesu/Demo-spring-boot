import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import '../styles/login.css';

const Login = () => {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    const formData = new FormData(e.target);
    const credentials = {
      email: formData.get('email'),
      password: formData.get('password')
    };

    try {
      const success = await login(credentials);
      if (success) {
        navigate('/home');
      } else {
        setError('Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.');
      }
    } catch (err) {
      setError('Lỗi kết nối tới server');
    }
  };

  return (
    <div className="book-login-wrapper">
      <div className="book-login-box">
        <h1 className="book-title">📖 BookVerse</h1>
        <p className="book-subtitle">Nơi những câu chữ lên tiếng</p>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit} className="login-form">
          <input type="email" name="email" placeholder="Email" required />
          <input type="password" name="password" placeholder="Mật khẩu" required />
          <button type="submit" className="btn-login">Đăng nhập</button>
        </form>

        <div className="divider">hoặc</div>

        <a href="http://localhost:8080/oauth2/authorization/google" className="btn-google">
          Đăng nhập với Google
        </a>

        <div className="text-center" style={{ marginTop: '1rem' }}>
          Chưa có tài khoản?&nbsp;<Link to="/register">Đăng ký</Link>
        </div>
      </div>
    </div>
  );
};

export default Login;

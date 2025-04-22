import React from 'react';
import './Login.css'; // Tự động lấy file CSS cùng thư mục

const Login = () => {
  return (
    <div className="book-login-wrapper">
      <div className="book-login-box">
        <h1 className="book-title">📖 BookVerse</h1>
        <p className="book-subtitle">Nơi những câu chữ lên tiếng</p>

        <form method="POST" action="http://localhost:8086/login" className="login-form">
          <input type="text" name="username" placeholder="Tên đăng nhập" required />
          <input type="password" name="password" placeholder="Mật khẩu" required />
          <button type="submit" className="btn-login">Đăng nhập</button>
        </form>

        <div className="divider">hoặc</div>

        <a href="http://localhost:8086/oauth2/authorization/google" className="btn-google">
          Đăng nhập với Google
        </a>
      </div>
    </div>
  );
};

export default Login;

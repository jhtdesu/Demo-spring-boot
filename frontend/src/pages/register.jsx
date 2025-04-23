import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/register.css';

const Register = () => {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    name: '',
    email: '',
    password: '',
    confirmPassword: ''
  });
  const [error, setError] = useState('');

  const handleChange = e => {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setError('');

    // Kiểm tra mật khẩu match
    if (form.password !== form.confirmPassword) {
      setError('Mật khẩu không khớp');
      return;
    }

    try {
      const res = await fetch('http://localhost:8086/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({
          name: form.name,
          email: form.email,
          password: form.password
        })
      });

      if (res.ok) {
        // Nếu backend trả 200, redirect về front-end login
        navigate('/login');
      } else {
        // đọc message lỗi từ backend
        const text = await res.text();
        setError(text || 'Đăng ký thất bại');
      }
    } catch (err) {
      console.error(err);
      setError('Lỗi kết nối tới server');
    }
  };

  return (
    <div className="book-login-wrapper">
      <div className="book-login-box">
        <h1 className="book-title">📖 BookVerse</h1>
        <p className="book-subtitle">Chào mừng bạn đến với BookVerse</p>

        {error && <div className="error-msg">{error}</div>}

        <form onSubmit={handleSubmit} className="login-form">
          <input
            type="text"
            name="name"
            placeholder="Họ và tên"
            value={form.name}
            onChange={handleChange}
            required
          />
          <input
            type="email"
            name="email"
            placeholder="Email"
            value={form.email}
            onChange={handleChange}
            required
          />
          <input
            type="password"
            name="password"
            placeholder="Mật khẩu"
            value={form.password}
            onChange={handleChange}
            required
          />
          <input
            type="password"
            name="confirmPassword"
            placeholder="Nhập lại mật khẩu"
            value={form.confirmPassword}
            onChange={handleChange}
            required
          />
          <button type="submit" className="btn-login">
            Đăng ký
          </button>
        </form>

        <div className="divider">hoặc</div>

        <div className="text-center">
          Bạn đã có tài khoản?&nbsp;
          <Link to="/login">Đăng nhập</Link>
        </div>
      </div>
    </div>
  );
};

export default Register;

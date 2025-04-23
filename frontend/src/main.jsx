import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './index.css';

import App from './App.jsx';
import Login from './pages/Login.jsx'; // Tạo file Login.jsx như mình hướng dẫn ở trên
import BookList from './pages/getAllBooks.jsx';
import ChapterList from './pages/getAllChapters.jsx';
import Register from './pages/register.jsx';
import Home from './pages/home.jsx'; // Tạo file Home.jsx như mình hướng dẫn ở trên

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/home" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/getAllBooks" element={<BookList />} />
        <Route path="/getAllChapters" element={<ChapterList/>}/>
        {/* Thêm các route khác nếu cần */}
      </Routes>
    </BrowserRouter>
  </StrictMode>
);

import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import './index.css';
import { AuthProvider } from './context/AuthContext';

import App from './App.jsx';
import Login from './pages/login.jsx'; // Tạo file Login.jsx như mình hướng dẫn ở trên
import BookList from './pages/getAllBooks.jsx';
import ChapterList from './pages/getAllChapters.jsx';
import Register from './pages/register.jsx';
import Home from './pages/home.jsx'; // Tạo file Home.jsx như mình hướng dẫn ở trên
import GetBookById from './pages/getBookId.jsx';
import GetChapterById from './pages/getChapterId.jsx';

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Navigate to="/home" replace/>} /> {/* Redirect từ "/" đến "/home" */}
          <Route path="/home" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/getAllBooks" element={<BookList />} />
          <Route path="/getAllChapters" element={<ChapterList/>}/>
          <Route path="/getBook/:id" element={<GetBookById />} />
          <Route path="/getChapter/:id" element={<GetChapterById/>}/>
          {/* Mount the blog app at /blog/* */}
          <Route path="/blog/*" element={<App />} />
          {/* Thêm các route khác nếu cần */}
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  </StrictMode>
);

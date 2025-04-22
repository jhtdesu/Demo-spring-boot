import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './index.css';

import App from './App.jsx';
import Login from './pages/Login.jsx'; // Tạo file Login.jsx như mình hướng dẫn ở trên
import BookList from './pages/getAllBook.jsx';
import ChapterList from './pages/getAllChapters.jsx';

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<App />} />
        <Route path="/login" element={<Login />} />
        <Route path="/getAllBooks" element={<BookList />} />
        <Route path="/getAllChapters" element={<ChapterList/>}/>
        {/* Thêm các route khác nếu cần */}
      </Routes>
    </BrowserRouter>
  </StrictMode>
);

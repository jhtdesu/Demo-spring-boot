import React, { createContext, useState, useContext, useEffect } from 'react';
import axios from '../api/axiosConfig';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(null);

  useEffect(() => {
    // Check if user is authenticated on mount
    checkAuthStatus();
  }, []);

  const checkAuthStatus = async () => {
    try {
      const response = await axios.get('/user', { withCredentials: true });
      if (response.data) {
        setIsAuthenticated(true);
        setUser(response.data);
      }
    } catch (error) {
      setIsAuthenticated(false);
      setUser(null);
    }
  };

  const login = async (credentials) => {
    try {
      await axios.post('/api/auth/login', credentials, { withCredentials: true });
      // After login, fetch user profile using JWT cookie
      try {
        const meRes = await axios.get('/api/users/me', { withCredentials: true });
        if (meRes.data && meRes.data.id) {
          localStorage.setItem('userId', meRes.data.id);
        }
        if (meRes.data && meRes.data.email) {
          localStorage.setItem('userEmail', meRes.data.email);
        }
        setIsAuthenticated(true);
        setUser(meRes.data);
      } catch (e) {
        localStorage.removeItem('userId');
        localStorage.removeItem('userEmail');
        setIsAuthenticated(false);
        setUser(null);
      }
      return true;
    } catch (error) {
      setIsAuthenticated(false);
      setUser(null);
      localStorage.removeItem('userId');
      localStorage.removeItem('userEmail');
      return false;
    }
  };

  const logout = async () => {
    try {
      await axios.post('/logout', {}, { withCredentials: true });
      setIsAuthenticated(false);
      setUser(null);
      localStorage.removeItem('userId');
      localStorage.removeItem('userEmail');
    } catch (error) {
      console.error('Logout failed:', error);
    }
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}; 
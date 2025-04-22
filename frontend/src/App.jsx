import React, { useState } from 'react';
import axios from 'axios';

const LoginScreen = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleLogin = async (event) => {
    event.preventDefault();
    setError('');

    try {
      const response = await axios.post('http://localhost:8080/login', {
        username,
        password,
      });

      // Assuming your backend returns some kind of token or session information
      const authToken = response.data.token; // Adjust based on your backend response
      localStorage.setItem('authToken', authToken); // Store the token (example)

      // Redirect to a protected page (e.g., where you fetch users)
      window.location.href = '/users'; // Adjust the route as needed

    } catch (error) {
      console.error('Login failed:', error);
      if (error.response && error.response.data && error.response.data.message) {
        setError(error.response.data.message);
      } else {
        setError('Login failed. Please check your credentials.');
      }
    }
  };

  return (
    <div>
      <h1>Login</h1>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <form onSubmit={handleLogin}>
        <div>
          <label htmlFor="username">Username:</label>
          <input
            type="text"
            id="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>
        <div>
          <label htmlFor="password">Password:</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit">Login</button>
      </form>
      {/* If you have OAuth2 buttons, you would add them here */}
      <button onClick={() => window.location.href = 'http://localhost:8080/oauth2/authorization/google'}>Login with Google</button>
    </div>
  );
};

export default LoginScreen;
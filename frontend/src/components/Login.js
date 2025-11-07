import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';

function Login({ setIsLoggedIn, setUsername }) {
  const [usernameInput, setUsernameInput] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [isSuccess, setIsSuccess] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault(); // prevent page reload
    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: usernameInput, password }),
      });
      const text = await response.text();

      if (text.includes('successful')) {
        setIsSuccess(true);
        setMessage('Login successful!');
        localStorage.setItem('isLoggedIn', 'true');
        localStorage.setItem('username', usernameInput);
        setIsLoggedIn(true);
        setUsername(usernameInput);
        setTimeout(() => {
            navigate('/');
        }, 1500);
      } else {
        setIsSuccess(false);
        setMessage('Invalid username or password.');
      }
    } catch (error) {
      setMessage('Login failed. Please try again.');
    }
  };

  return (
    <div className="login-page">
      <div className="login-form">
        <h2>Login</h2>
        <form onSubmit={handleLogin}>
          <div>
            <label>UserName:</label>
            <input
              type="text"
              value={usernameInput}
              onChange={(e) => setUsernameInput(e.target.value)}
            />
          </div>
          <div>
            <label>Password:</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>
          <button type="submit">Login</button>
        </form>
        <p>
          New User?{' '}
          <Link to="/register">Register Here</Link>
        </p>
        {message && (
            <p className={isSuccess ? 'success' : 'error'}>{message}</p>
        )}
      </div>
    </div>
  );
}

export default Login;

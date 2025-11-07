import React, { useEffect, useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';

function Register() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [email, setEmail] = useState('');
  const [birthday, setBirthday] = useState('');
  const [message, setMessage] = useState('');
  const [isSuccess, setIsSuccess] = useState(false);
  const [existingUsernames, setExistingUsernames] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUsernames = async () => {
      try {
        const res = await fetch('http://localhost:8080/api/auth/usernames');
        if (res.ok) {
          const data = await res.json();
          setExistingUsernames(Array.isArray(data) ? data : []);
        }
      } catch (err) {
        // silently ignore; backend may not be running yet
      }
    };
    fetchUsernames();
  }, []);

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      // check username uniqueness client-side for better UX
      if (existingUsernames.includes(username)) {
        setIsSuccess(false);
        setMessage('Username is already taken. Please choose another.');
        return;
      }

      const response = await fetch('http://localhost:8080/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password, roles: 'USER', email, birthday }),
      });
      const text = await response.text();

      if (text.includes('successfully')) {
        setIsSuccess(true);
        setMessage('Registration successful! Redirecting to login...');
        setTimeout(() => navigate('/login'), 1500);
      } else {
        setIsSuccess(false);
        setMessage(text || 'Registration failed. Please try again.');
      }
    } catch (error) {
      setIsSuccess(false);
      setMessage('Something went wrong. Please try again.');
    }
  };

  return (
    <div className="login-page">
      <div className="login-form">
        <h2>Register</h2>
        <form onSubmit={handleRegister}>
          <div>
            <label>UserName:</label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </div>
          <div>
            <label>Password:</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <div>
            <label>Email:</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          <div>
            <label>Date of Birth:</label>
            <input
              type="date"
              value={birthday}
              onChange={(e) => setBirthday(e.target.value)}
            />
          </div>
          <button type="submit">Register</button>
        </form>

        <p>
          Already have an account? <Link to="/login">Login Here</Link>
        </p>

        {message && (
          <p className={isSuccess ? 'success' : 'error'}>{message}</p>
        )}
      </div>
    </div>
  );
}

export default Register;

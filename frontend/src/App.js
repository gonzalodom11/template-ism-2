import React, { useState, useEffect } from 'react'; 
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import AskQuestion from './components/AskQuestion';
import QuestionsList from './components/QuestionsList';
import './App.css';
import Login from './components/Login'; 
import Register from './components/Register'; 
import UserProfile from './components/UserProfile';

function Navbar({ isLoggedIn, handleLogout }) {
  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-logo">Q&A App</Link>
        <div className="nav-menu">
          <Link to="/" className="nav-link">Home</Link>
          {isLoggedIn && (
            <Link to="/ask" className="nav-link">Ask Question</Link>
          )}
            <Link to="/questions" className="nav-link">All Questions</Link>
          {isLoggedIn && (
            <Link to="/profile" className="nav-link">Profile</Link>
          )}
          {!isLoggedIn ? (
            <Link to="/login" className="nav-link">Login</Link>
          ) : (
            <button onClick={handleLogout} className="nav-link logout-button">Logout</button>
          )}
        </div>
      </div>
    </nav>
  );
}

function ProtectedRoute({ isLoggedIn, children }) {
  if (isLoggedIn) return children;
  return (
    <div className="page">
      <h2>Login Required</h2>
      <p>You need to be logged in to access this page.</p>
      <Link to="/login" className="cta-button">Go to Login</Link>
    </div>
  );
}

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [username, setUsername] = useState('');
  
    useEffect(() => {
      const loggedIn = localStorage.getItem('isLoggedIn') === 'true';
      const storedUsername = localStorage.getItem('username') || '';
      setIsLoggedIn(loggedIn);
      setUsername(storedUsername);
    }, []);
  
    const handleLogout = () => {
      localStorage.removeItem('isLoggedIn');
      localStorage.removeItem('username');
      setIsLoggedIn(false);
      setUsername('');
    };
  
  return (
    <Router>
      <div className="App">
      
        <Navbar isLoggedIn={isLoggedIn} handleLogout={handleLogout} />

        <div className="content-container">
          <Routes>
            <Route path="/" element={
              <div className="home-page">
                <h1>Welcome to the Q&A Platform</h1>
                <p>Ask questions and get answers from the community</p>
                <div className='but-container'>
                {isLoggedIn ? (
                  <Link to="/ask" className="cta-button">Ask a Question</Link>
                ) : (
                  <Link to="/login" className="cta-button">Login to Ask</Link>
                )}
                <Link to="/questions" className="cta-button secondary">View All Questions</Link>
                </div>
              </div>
            } />
            <Route
              path="/ask"
              element={
                <ProtectedRoute isLoggedIn={isLoggedIn}>
                  <AskQuestion username={username} />
                </ProtectedRoute>
              }
            />
            <Route path="/questions" element={<QuestionsList username={username} />} />
              <Route path="/login" element={<Login setIsLoggedIn={setIsLoggedIn} setUsername={setUsername} />} />
              <Route path="/register" element={<Register />} /> 
              <Route path="/profile" element={<UserProfile username={username} />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;

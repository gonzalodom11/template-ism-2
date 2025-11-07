import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

function AnswerModal({ open, onClose, question, username }) {
  const [answers, setAnswers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [content, setContent] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const hasUsername = Boolean(username);

  useEffect(() => {
    if (!open || !question) return;
    const loadAnswers = async () => {
      setLoading(true);
      setError(null);
      try {
        const res = await fetch(`/api/questions/${question.id}/answers`);
        if (!res.ok) {
          throw new Error(`Failed to load answers: ${res.status}`);
        }
        const data = await res.json();
        setAnswers(Array.isArray(data) ? data : []);
      } catch (e) {
        setError(e.message);
      } finally {
        setLoading(false);
      }
    };
    loadAnswers();
  }, [open, question]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!content.trim()) {
      setError('Answer content is required');
      return;
    }
    if (!hasUsername) {
      setError('Unable to determine username. Please login again.');
      return;
    }
    setSubmitting(true);
    setError(null);
    try {
      const res = await fetch(`/api/questions/${question.id}/answers`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ author: username, content }),
      });
      if (!res.ok) {
        const text = await res.text();
        throw new Error(text || `Failed to submit answer: ${res.status}`);
      }
      const { answer } = await res.json();
      setAnswers((prev) => [answer, ...prev]);
      setContent('');
    } catch (e) {
      setError(e.message);
    } finally {
      setSubmitting(false);
    }
  };

  if (!open) return null;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h3>Answers for: {question.title || `(ID ${question.id})`}</h3>
          <button className="close-button" onClick={onClose}>×</button>
        </div>

        <div className="modal-body">
          {loading ? (
            <p>Loading answers…</p>
          ) : error ? (
            <p className="error">{error}</p>
          ) : (
            <div>
              {answers.length === 0 ? (
                <p>No answers yet.</p>
              ) : (
                <ul className="answers-list">
                  {answers.map((a) => (
                    <li key={a.id} className="answer-item">
                      <div className="answer-content">{a.content}</div>
                      <div className="answer-meta">
                        <span>By {a.author || 'Anonymous'}</span>
                        {a.createdAt && (<span> • {new Date(a.createdAt).toLocaleString()}</span>)}
                        <span> • ID: {a.id}</span>
                      </div>
                    </li>
                  ))}
                </ul>
              )}
            </div>
          )}
        </div>

        <div className="modal-footer">
          {localStorage.getItem('isLoggedIn') === 'true' ? (
            <form className="answer-form" onSubmit={handleSubmit}>
              {hasUsername && (
                <div className="answer-meta">Answering as {username}</div>
              )}
              <textarea
                placeholder="Write your answer…"
                value={content}
                onChange={(e) => setContent(e.target.value)}
                required
              />
              <button type="submit" className="submit-button" disabled={submitting || !hasUsername}>
                {submitting ? 'Submitting…' : 'Submit Answer'}
              </button>
            </form>
          ) : (
            <div>
              <p>You must be logged in to submit an answer.</p>
              <Link to="/login" className="cta-button">Login</Link>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default AnswerModal;
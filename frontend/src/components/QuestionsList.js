import React, { useEffect, useState } from 'react';
import AnswerModal from './AnswerModal';
import '../App.css'; // optional for styling if you use a CSS file

function QuestionsList({ username }) {
  const [questions, setQuestions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [activeQuestion, setActiveQuestion] = useState(null);

  useEffect(() => {
    const fetchQuestions = async () => {
      try {
        const res = await fetch('/api/questions');
        if (!res.ok) throw new Error(`Failed to load questions: ${res.status}`);
        const data = await res.json();

        // fallback if API doesn't include "answered"
        const enriched = data.map(q => ({
          ...q,
          answered: q.answered ?? false
        }));

        setQuestions(enriched);
      } catch (e) {
        setError(e.message);
      } finally {
        setLoading(false);
      }
    };
    fetchQuestions();
  }, []);

  if (loading) {
    return (
      <div className="page">
        <h2>All Questions</h2>
        <p>Loading…</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="page">
        <h2>All Questions</h2>
        <p className="error">{error}</p>
      </div>
    );
  }

  return (
    <div className="page">
      <h2>All Questions</h2>
      {questions.length === 0 ? (
        <p>No questions found.</p>
      ) : (
        <div className="questions-list">
          {questions.map((q) => (
            <div key={q.id} className="question-card">
              <div className="question-header">
                <h3 className="question-title">{q.title || '(No title)'}</h3>
                {q.category && q.category.id && (
                  <span className="question-category">{q.category.id}</span>
                )}
              </div>

              <p className="question-content">{q.content || '(No content)'}</p>

              <div className="question-meta">
                <span>Author: {q.author || 'Unknown'}</span>
                {q.createdAt && (
                  <span> • Asked: {new Date(q.createdAt).toLocaleString()}</span>
                )}
              </div>

              <div className="question-footer">
                <span
                  className={`status-badge ${
                    q.answered ? 'answered' : 'not-answered'
                  }`}
                >
                  {q.answered ? 'Answered' : 'Not answered'}
                </span>
                <button
                  className="answer-button"
                  onClick={() => {
                    setActiveQuestion(q);
                    setModalOpen(true);
                  }}
                >
                  Answer / View Answers
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      <AnswerModal
        open={modalOpen}
        onClose={() => setModalOpen(false)}
        question={activeQuestion}
        username={username}
      />
    </div>
  );
}

export default QuestionsList;

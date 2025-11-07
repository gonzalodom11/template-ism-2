import React, { useState } from 'react';
import './AskQuestion.css';

function AskQuestion({ username }) {
    const [formData, setFormData] = useState({
        title: '',
        content: '',
    });
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [submitMessage, setSubmitMessage] = useState('');
    const [submitStatus, setSubmitStatus] = useState('');
    const hasUsername = Boolean(username);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);
        setSubmitMessage('');
        setSubmitStatus('');

        if (!hasUsername) {
            setSubmitMessage('Unable to determine username. Please login again.');
            setSubmitStatus('error');
            return;
        }

        const questionData = {
            ...formData,
            author: username,
        };

        try {
            const response = await fetch('http://localhost:8080/api/questions', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(questionData),
            });

            if (response.ok) {
                setFormData({ title: '', content: '' });
                setSubmitMessage('Question submitted successfully!');
                setSubmitStatus('success');
            } else {
                const errorData = await response.json();
                setSubmitMessage(`Error: ${errorData.message || 'Failed to submit question'}`);
                setSubmitStatus('error');
            }
        } catch (error) {
            setSubmitMessage(`Error: ${error.message || 'Something went wrong'}`);
            setSubmitStatus('error');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="ask-question-container">
            <h2>Ask a Question</h2>
            {hasUsername && (
                <p className="question-meta">Posting as {username}</p>
            )}
            <form onSubmit={handleSubmit} className="question-form">
                <div className="form-group">
                    <label htmlFor="title">Question Title:</label>
                    <input
                        type="text"
                        id="title"
                        name="title"
                        value={formData.title}
                        onChange={handleChange}
                        required
                        placeholder="Enter a descriptive title"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="content">Question Details:</label>
                    <textarea
                        id="content"
                        name="content"
                        value={formData.content}
                        onChange={handleChange}
                        required
                        placeholder="Provide details about your question"
                        rows="6"
                    />
                </div>
                <button type="submit" disabled={isSubmitting || !hasUsername} className="submit-button">
                    {isSubmitting ? 'Submitting...' : 'Submit Question'}
                </button>
                {submitMessage && (
                    <div className={`submit-message ${submitStatus}`}>
                        {submitMessage}
                    </div>
                )}
            </form>
        </div>
    );
}

export default AskQuestion;
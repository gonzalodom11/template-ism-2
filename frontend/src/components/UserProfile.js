import React, { useEffect, useState } from 'react';

function UserProfile({ username: propUsername }) {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [activeTab, setActiveTab] = useState('answers');
  const username = propUsername || localStorage.getItem('username') || '';

  useEffect(() => {
    if (!username) return;
    const load = async () => {
      setLoading(true);
      setError(null);
      try {
        const res = await fetch(`/api/users/${encodeURIComponent(username)}/stats`);
        if (!res.ok) {
          const text = await res.text();
          throw new Error(text || `Failed to load stats: ${res.status}`);
        }
        const json = await res.json();
        setData(json);
      } catch (e) {
        setError(e.message);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, [username]);

  const achievements = {
    answers: [
      {
        threshold: 1,
        image: 'https://i.ibb.co/jk1q5wj3/brave-screenshot-iconscout-com-1.png',
        title: 'First Answer',
        unlockedText: 'You unlocked this achievement by providing 1 answer',
        lockedText: 'Provide 1 answer to unlock this achievement'
      },
      {
        threshold: 5,
        image: 'https://i.ibb.co/VYybLDMV/brave-screenshot-iconscout-com-5.png',
        title: '5 Answers',
        unlockedText: 'You unlocked this achievement by providing 5 answers',
        lockedText: 'Provide 5 answers to unlock this achievement'
      },
      {
        threshold: 10,
        image: 'https://i.ibb.co/Kxx3cmp0/brave-screenshot-iconscout-com-6.png',
        title: '10 Answers',
        unlockedText: 'You unlocked this achievement by providing 10 answers',
        lockedText: 'Provide 10 answers to unlock this achievement'
      },
      {
        threshold: 50,
        image: 'https://i.ibb.co/kgvxbL9w/brave-screenshot-iconscout-com-8.png',
        title: '50 Answers',
        unlockedText: 'You unlocked this achievement by providing 50 answers',
        lockedText: 'Provide 50 answers to unlock this achievement'
      },
      {
        threshold: 100,
        image: 'https://i.ibb.co/XxY46CfL/brave-screenshot-iconscout-com-12.png',
        title: '100 Answers',
        unlockedText: 'You unlocked this achievement by providing 100 answers',
        lockedText: 'Provide 100 answers to unlock this achievement'
      }
    ],
    questions: [
      {
        threshold: 1,
        image: 'https://i.ibb.co/Tqb0bkgq/brave-screenshot-iconscout-com-2.png',
        title: 'First Question',
        unlockedText: 'You unlocked this achievement by asking 1 question',
        lockedText: 'Ask 1 question to unlock this achievement'
      },
      {
        threshold: 5,
        image: 'https://i.ibb.co/35yFCFmj/brave-screenshot-iconscout-com-10.png',
        title: '5 Questions',
        unlockedText: 'You unlocked this achievement by asking 5 questions',
        lockedText: 'Ask 5 questions to unlock this achievement'
      },
      {
        threshold: 10,
        image: 'https://i.ibb.co/gFg6bHJ7/brave-screenshot-iconscout-com-11.png',
        title: '10 Questions',
        unlockedText: 'You unlocked this achievement by asking 10 questions',
        lockedText: 'Ask 10 questions to unlock this achievement'
      },
      {
        threshold: 50,
        image: 'https://i.ibb.co/YTq4McvQ/brave-screenshot-iconscout-com-13.png',
        title: '50 Questions',
        unlockedText: 'You unlocked this achievement by asking 50 questions',
        lockedText: 'Ask 50 questions to unlock this achievement'
      },
      {
        threshold: 100,
        image: 'https://i.ibb.co/xqDvsC8b/brave-screenshot-iconscout-com-14.png',
        title: '100 Questions',
        unlockedText: 'You unlocked this achievement by asking 100 questions',
        lockedText: 'Ask 100 questions to unlock this achievement'
      }
    ],
    streak: [
      {
        threshold: 2,
        image: 'https://i.ibb.co/VYyWRf2N/brave-screenshot-iconscout-com-3.png',
        title: '2 Day Streak',
        unlockedText: 'You unlocked this achievement with a 2 day streak',
        lockedText: 'Reach a 2 day streak to unlock this achievement'
      },
      {
        threshold: 10,
        image: 'https://i.ibb.co/zWT24yrn/brave-screenshot-iconscout-com-4.png',
        title: '10 Day Streak',
        unlockedText: 'You unlocked this achievement with a 10 day streak',
        lockedText: 'Reach a 10 day streak to unlock this achievement'
      },
      {
        threshold: 25,
        image: 'https://i.ibb.co/HT48hhLB/brave-screenshot-iconscout-com-7.png',
        title: '25 Day Streak',
        unlockedText: 'You unlocked this achievement with a 25 day streak',
        lockedText: 'Reach a 25 day streak to unlock this achievement'
      },
      {
        threshold: 50,
        image: 'https://i.ibb.co/b5GtybfZ/brave-screenshot-iconscout-com-9.png',
        title: '50 Day Streak',
        unlockedText: 'You unlocked this achievement with a 50 day streak',
        lockedText: 'Reach a 50 day streak to unlock this achievement'
      },
      {
        threshold: 100,
        image: 'https://i.ibb.co/1Jmxq8cQ/brave-screenshot-iconscout-com.png',
        title: '100 Day Streak',
        unlockedText: 'You unlocked this achievement with a 100 day streak',
        lockedText: 'Reach a 100 day streak to unlock this achievement'
      }
    ]
  };

  const getCurrentValue = () => {
    if (!data?.stats) return 0;
    switch (activeTab) {
      case 'answers':
        return data.stats.answers ?? 0;
      case 'questions':
        return data.stats.questions ?? 0;
      case 'streak':
        return data.stats.streak ?? 0;
      default:
        return 0;
    }
  };

  if (!username) {
    return (
      <div className="profile-page">
        <h2>User Profile</h2>
        <p>Please login to view your profile.</p>
      </div>
    );
  }

  return (
    <div className="profile-page">
      <h2>User Profile</h2>
      {loading && <p>Loading...</p>}
      {error && <p className="error">{error}</p>}
      {data && data.success && (
        <>
          <div className="profile-card">
            <div className="profile-row"><strong>Username:</strong> {data.user?.username}</div>
            <div className="profile-row"><strong>Email:</strong> {data.user?.email || '—'}</div>
            <div className="profile-row"><strong>Total Questions:</strong> {data.stats?.questions ?? 0}</div>
            <div className="profile-row"><strong>Total Answers:</strong> {data.stats?.answers ?? 0}</div>
            <div className="profile-row"><strong>Day Streak:</strong> {data.stats?.streak ?? 0} days</div>
          </div>

          <div className="achievements-section">
            <h3>Achievements</h3>
            <div className="achievement-tabs">
              <button
                className={`achievement-tab ${activeTab === 'answers' ? 'active' : ''}`}
                onClick={() => setActiveTab('answers')}
              >
                Answers
              </button>
              <button
                className={`achievement-tab ${activeTab === 'questions' ? 'active' : ''}`}
                onClick={() => setActiveTab('questions')}
              >
                Questions
              </button>
              <button
                className={`achievement-tab ${activeTab === 'streak' ? 'active' : ''}`}
                onClick={() => setActiveTab('streak')}
              >
                Streak
              </button>
            </div>

            <div className="achievements-list">
              {achievements[activeTab].map((achievement, index) => {
                const currentValue = getCurrentValue();
                const isUnlocked = currentValue >= achievement.threshold;
                
                return (
                  <div key={index} className={`achievement-item ${isUnlocked ? 'unlocked' : 'locked'}`}>
                    <img
                      src={achievement.image}
                      alt={achievement.title}
                      className="achievement-image"
                    />
                    <div className="achievement-text">
                      <h4>{achievement.title}</h4>
                      <p>{isUnlocked ? achievement.unlockedText : achievement.lockedText}</p>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        </>
      )}
    </div>
  );
}

export default UserProfile;
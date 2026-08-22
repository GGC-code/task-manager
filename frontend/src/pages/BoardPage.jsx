import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getBoards } from '../api/boards';

function BoardPage() {
  const [boards, setBoards] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  function handleLogout() {
    localStorage.removeItem('token');
    navigate('/login');
  }

  useEffect(() => {
    async function loadBoards() {
      try {
        const data = await getBoards();
        setBoards(data);
      } catch {
        setError('Impossibile caricare le bacheche.');
      } finally {
        setLoading(false);
      }
    }
    loadBoards();
  }, []);

  if (loading) {
    return <p className="status-msg">Caricamento in corso...</p>;
  }

  if (error) {
    return <p className="status-msg error">{error}</p>;
  }

  return (
    <div className="page">
      <div className="app-bar">
        <h1>Le mie bacheche</h1>
        <button className="btn-ghost" onClick={handleLogout}>Logout</button>
      </div>

      {boards.length === 0 ? (
        <div className="empty-state">Non hai ancora nessuna bacheca.</div>
      ) : (
        <ul className="board-grid">
          {boards.map((board) => (
            <li
              key={board.id}
              className="board-card"
              onClick={() => navigate(`/boards/${board.id}`)}
            >
              <div className="board-title">{board.title}</div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default BoardPage;
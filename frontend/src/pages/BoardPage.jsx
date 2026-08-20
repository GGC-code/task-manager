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
      } catch (err) {
        setError('Impossibile caricare le bacheche.');
      } finally {
        setLoading(false);
      }
    }
    loadBoards();
  }, []);

  if (loading) {
    return <p>Caricamento in corso...</p>;
  }

  if (error) {
    return <p style={{ color: 'red' }}>{error}</p>;
  }

  return (
    <div>
            <h1>Le mie bacheche</h1>
      <button onClick={handleLogout}>Logout</button>
      {boards.length === 0 ? (
        <p>Non hai ancora nessuna bacheca.</p>
      ) : (
        <ul>
          {boards.map((board) => (
            <li
              key={board.id}
              onClick={() => navigate(`/boards/${board.id}`)}
              style={{ cursor: 'pointer', textDecoration: 'underline', color: 'blue' }}
            >
              {board.title}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default BoardPage;
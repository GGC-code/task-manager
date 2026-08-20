import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getBoardDetail } from '../api/columns';

function BoardDetailPage() {
  // Legge l'id della bacheca dal percorso, es. /boards/42 -> boardId = "42".
  const { boardId } = useParams();

  const [columns, setColumns] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    async function loadDetail() {
      try {
        const data = await getBoardDetail(boardId);
        setColumns(data);
      } catch (err) {
        setError('Impossibile caricare il dettaglio della bacheca.');
      } finally {
        setLoading(false);
      }
    }
    loadDetail();
  }, [boardId]);

  if (loading) {
    return <p>Caricamento in corso...</p>;
  }

  if (error) {
    return <p style={{ color: 'red' }}>{error}</p>;
  }

  return (
    <div>
      <button onClick={() => navigate('/boards')}>Torna alle bacheche</button>
      <h1>Dettaglio bacheca</h1>

      {columns.length === 0 ? (
        <p>Questa bacheca non ha ancora colonne.</p>
      ) : (
        <div style={{ display: 'flex', gap: '1rem', alignItems: 'flex-start' }}>
          {columns.map((column) => (
            <div
              key={column.id}
              style={{ border: '1px solid #ccc', borderRadius: '4px', padding: '0.5rem', minWidth: '200px' }}
            >
              <h2>{column.name}</h2>

              {column.tasks.length === 0 ? (
                <p style={{ color: '#888' }}>Nessun task.</p>
              ) : (
                <ul style={{ listStyle: 'none', padding: 0 }}>
                  {column.tasks.map((task) => (
                    <li
                      key={task.id}
                      style={{ border: '1px solid #ddd', borderRadius: '4px', padding: '0.5rem', marginBottom: '0.5rem' }}
                    >
                      <strong>{task.title}</strong>
                      {task.description && <p style={{ margin: '0.25rem 0 0' }}>{task.description}</p>}
                      {task.assigneeUsername && (
                        <p style={{ margin: '0.25rem 0 0', fontSize: '0.85rem', color: '#555' }}>
                          Assegnato a: {task.assigneeUsername}
                        </p>
                      )}
                    </li>
                  ))}
                </ul>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default BoardDetailPage;
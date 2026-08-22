import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getBoardDetail } from '../api/columns';

// Associa il nome della colonna al relativo stato del flusso Kanban,
// così l'intestazione mostra il colore corretto (da fare, in corso, fatto).
function statusClass(name) {
  const n = (name || '').trim().toLowerCase();
  if (['in corso', 'in progress', 'doing'].includes(n)) return 'is-progress';
  if (['fatto', 'completato', 'done'].includes(n)) return 'is-done';
  return 'is-todo';
}

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
      } catch {
        setError('Impossibile caricare il dettaglio della bacheca.');
      } finally {
        setLoading(false);
      }
    }
    loadDetail();
  }, [boardId]);

  if (loading) {
    return <p className="status-msg">Caricamento in corso...</p>;
  }

  if (error) {
    return <p className="status-msg error">{error}</p>;
  }

  return (
    <div className="page">
      <div className="app-bar">
        <button className="btn-ghost" onClick={() => navigate('/boards')}>
          &larr; Torna alle bacheche
        </button>
        <h1>Dettaglio bacheca</h1>
        <span className="spacer" />
      </div>

      {columns.length === 0 ? (
        <div className="empty-state">Questa bacheca non ha ancora colonne.</div>
      ) : (
        <div className="board-columns">
          {columns.map((column) => (
            <section key={column.id} className={`column ${statusClass(column.name)}`}>
              <header className="column-header">
                <span className="dot" />
                <h2>{column.name}</h2>
                <span className="count">{column.tasks.length}</span>
              </header>

              {column.tasks.length === 0 ? (
                <p className="column-empty">Nessun task</p>
              ) : (
                <ul className="task-list">
                  {column.tasks.map((task) => (
                    <li key={task.id} className="task-card">
                      <p className="task-title">{task.title}</p>
                      {task.description && <p className="task-desc">{task.description}</p>}
                      {task.assigneeUsername && (
                        <span className="task-assignee">Assegnato a {task.assigneeUsername}</span>
                      )}
                    </li>
                  ))}
                </ul>
              )}
            </section>
          ))}
        </div>
      )}
    </div>
  );
}

export default BoardDetailPage;
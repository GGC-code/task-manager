// Modulo API per colonne e task.
// Isola le chiamate di rete relative alla vista di dettaglio di una bacheca,
// così i componenti React restano concentrati sulla presentazione.

// Recupera le colonne di una bacheca, ordinate per position dal back-end.
// Endpoint: GET /api/boards/{boardId}/columns
export async function getColumns(boardId) {
  const token = localStorage.getItem("token");

  const response = await fetch(`/api/boards/${boardId}/columns`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    throw new Error(`Errore nel caricamento delle colonne (HTTP ${response.status})`);
  }

  return response.json();
}

// Recupera i task di una singola colonna, ordinati per position dal back-end.
// Endpoint: GET /api/columns/{columnId}/tasks
export async function getTasks(columnId) {
  const token = localStorage.getItem("token");

  const response = await fetch(`/api/columns/${columnId}/tasks`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    throw new Error(`Errore nel caricamento dei task (HTTP ${response.status})`);
  }

  return response.json();
}

// Orchestrazione della vista di dettaglio.
// Recupera le colonne di una bacheca e, per ciascuna, i suoi task,
// restituendo una struttura pronta da disegnare: un array di colonne,
// dove ogni colonna porta dentro di sé il proprio array "tasks".
export async function getBoardDetail(boardId) {
  // 1) Prima richiesta: le colonne della bacheca (già ordinate per position).
  const columns = await getColumns(boardId);

  // 2) N richieste in parallelo: i task di ogni colonna.
  //    Lanciamo tutte le chiamate insieme e attendiamo il gruppo completo,
  //    perché le colonne sono indipendenti e non c'è motivo di attenderle in serie.
  const tasksPerColumn = await Promise.all(
    columns.map((column) => getTasks(column.id))
  );

  // 3) Cuciamo insieme: a ogni colonna associamo il suo array di task.
  //    Promise.all conserva l'ordine, quindi tasksPerColumn[i] appartiene a columns[i].
  return columns.map((column, index) => ({
    ...column,
    tasks: tasksPerColumn[index],
  }));
}
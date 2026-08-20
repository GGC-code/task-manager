const BASE_URL = '/api';

export async function getBoards() {
  const token = localStorage.getItem('token');

  const response = await fetch(`${BASE_URL}/boards`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    throw new Error('Impossibile caricare le bacheche');
  }

  return response.json();
}
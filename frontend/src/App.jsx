import { useEffect, useState } from 'react'
import './App.css'

function App() {
  // "stato" del componente: il messaggio da mostrare a schermo.
  const [statoApi, setStatoApi] = useState('Verifica del back-end in corso...')

  // useEffect con [] esegue il codice UNA volta, appena il componente compare:
  // il momento giusto per fare la prima chiamata al server.
  useEffect(() => {
    fetch('/api/ping') // URL relativo: parte verso la 5173 e Vite lo inoltra alla 8080
      .then((risposta) => {
        if (!risposta.ok) {
          throw new Error('Risposta non valida (' + risposta.status + ')')
        }
        return risposta.json()
      })
      .then((dati) => {
        setStatoApi('Back-end raggiungibile: ' + dati.status + ' — ' + dati.service)
      })
      .catch((errore) => {
        setStatoApi('Errore nel contattare il back-end: ' + errore.message)
      })
  }, [])

  return (
    <div style={{ padding: '2rem', fontFamily: 'sans-serif' }}>
      <h1>Task Manager</h1>
      <p>{statoApi}</p>
    </div>
  )
}

export default App
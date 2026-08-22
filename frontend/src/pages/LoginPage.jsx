import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../api/auth';

function LoginPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  async function handleSubmit(event) {
    event.preventDefault();
    setError('');
    try {
      const data = await login(username, password);
      localStorage.setItem('token', data.token);
      navigate('/boards');
    } catch {
      setError('Credenziali non valide. Riprova.');
    }
  }

  return (
    <div className="login-page">
      <div className="login-card">
        <h1>Task Manager</h1>
        <p className="login-subtitle">Accedi per gestire le tue bacheche</p>

        <form className="form" onSubmit={handleSubmit}>
          <div className="field">
            <label htmlFor="username">Username</label>
            <input
              id="username"
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
          </div>

          <div className="field">
            <label htmlFor="password">Password</label>
            <input
              id="password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>

          <button type="submit" className="btn-primary">Accedi</button>

          {error && <p className="form-error">{error}</p>}
        </form>
      </div>
    </div>
  );
}

export default LoginPage;
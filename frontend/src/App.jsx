import { Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import BoardPage from './pages/BoardPage';
import BoardDetailPage from './pages/BoardDetailPage';
import ProtectedRoute from './components/ProtectedRoute';

function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/login" replace />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/boards" element={<ProtectedRoute><BoardPage /></ProtectedRoute>} />
      <Route path="/boards/:boardId" element={<ProtectedRoute><BoardDetailPage /></ProtectedRoute>} />
    </Routes>
  );
}

export default App;
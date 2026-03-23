import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { useState } from 'react';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import type { User } from './types';

function App() {
  const [user, setUser] = useState<User | null>(null);

  return (
    <Router>
      <Routes>
        {/* Route de Login */}
        <Route path="/login" element={<Login onLogin={setUser} />} />

        {/* Route protégée : si pas de user, redirection vers login */}
        <Route 
          path="/dashboard" 
          element={user ? <Dashboard user={user} /> : <Navigate to="/login" />} 
        />

        {/* Redirection par défaut */}
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
  );
}

export default App;
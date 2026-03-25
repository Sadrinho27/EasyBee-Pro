import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { type JSX } from 'react';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import { Toaster } from 'react-hot-toast';
import { AuthProvider, useAuth } from './contexts/AuthContext';

const ProtectedRoute = ({ children }: { children: JSX.Element }) => {
  const { user } = useAuth();
  return user ? children : <Navigate to="/login" />;
};

function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />

      <Route
        path="/dashboard"
        element={
          <ProtectedRoute>
            <Dashboard />
          </ProtectedRoute>
        }
      />

      <Route path="*" element={<Navigate to="/login" />} />
    </Routes>
  );
}

function App() {
  return (
    <AuthProvider>
      <Router>
        <AppRoutes />
        <Toaster position="bottom-right" />
      </Router>
    </AuthProvider>
  );
}

export default App;
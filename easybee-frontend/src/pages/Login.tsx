import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import type { User } from '../types';

interface LoginProps {
  onLogin: (user: User) => void;
}

const Login: React.FC<LoginProps> = ({ onLogin }) => {
  const [identifiant, setIdentifiant] = useState('');
  const [motDePasse, setMotDePasse] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    try {
      const response = await axios.post('http://localhost:8080/api/auth/login', {
        identifiant,
        motDePasse
      });
      onLogin(response.data);
      navigate('/dashboard');
    } catch (err) {
      setError("Identifiants incorrects. Vérifiez votre ruche ! 🐝");
    }
  };

  return (
    <div className="min-h-screen bg-slate-50 flex items-center justify-center p-6">
      <div className="w-full max-w-md bg-white rounded-2xl shadow-xl p-8 border border-slate-100">
        <div className="text-center mb-10">
          <h1 className="text-4xl font-black text-slate-800 tracking-tight">
            EasyBee <span className="text-yellow-500">Pro</span>
          </h1>
          <p className="text-slate-500 mt-2 font-medium">Gestion de stock intelligente</p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-5">
          <div>
            <label className="block text-sm font-bold text-slate-700 mb-1">Identifiant</label>
            <input 
              type="text"
              className="w-full px-4 py-3 rounded-xl border border-slate-200 focus:ring-2 focus:ring-yellow-400 focus:border-transparent outline-none transition-all bg-slate-50"
              value={identifiant}
              onChange={(e) => setIdentifiant(e.target.value)}
              placeholder="ex: adminVendeur"
            />
          </div>
          <div>
            <label className="block text-sm font-bold text-slate-700 mb-1">Mot de passe</label>
            <input 
              type="password"
              className="w-full px-4 py-3 rounded-xl border border-slate-200 focus:ring-2 focus:ring-yellow-400 focus:border-transparent outline-none transition-all bg-slate-50"
              value={motDePasse}
              onChange={(e) => setMotDePasse(e.target.value)}
              placeholder="••••••••"
            />
          </div>
          
          {error && <p className="text-red-500 text-sm font-medium text-center">{error}</p>}

          <button 
            type="submit"
            className="w-full py-4 bg-yellow-500 hover:bg-yellow-600 text-white font-bold rounded-xl shadow-lg shadow-yellow-200 transition-all active:scale-95"
          >
            Se connecter
          </button>
        </form>
      </div>
    </div>
  );
};

export default Login;
import { useState } from 'react';
import Catalogue from '../components/Catalogue';
import Livraisons from '../components/Livraisons';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';

const Dashboard = () => {
    // État pour savoir quelle vue afficher
    const [view, setView] = useState<'menu' | 'catalogue' | 'livraisons'>('menu');
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    return (
        <div className="min-h-screen bg-slate-50">
            {/* Navbar */}
            <header className="bg-white border-b border-slate-200 px-8 py-4 flex justify-between items-center sticky top-0 z-10 shadow-sm">
                <div className="flex items-center gap-2 cursor-pointer" onClick={() => setView('menu')}>
                    <h2 className="text-2xl font-black text-slate-800 tracking-tight">EasyBee <span className="text-yellow-500">Pro</span></h2>
                </div>

                <div className="flex items-center gap-4">
                    <div className="text-right hidden sm:block">
                        <p className="text-sm font-bold text-slate-900">{user!.prenomSalarie} {user!.nomSalarie}</p>
                        <p className="text-xs text-slate-400 font-semibold uppercase tracking-widest">{user!.categorie.nom}</p>
                    </div>
                    <button
                        onClick={() => {
                            logout();
                            navigate('/login');
                        }}
                        className="w-10 h-10 flex items-center justify-center rounded-full bg-slate-100 text-slate-500 hover:bg-red-50 hover:text-red-500 transition-all"
                    >
                        ❌
                    </button>
                </div>
            </header>

            <main className="max-w-7xl mx-auto p-8">
                {view === 'menu' ? (
                    /* VUE : MENU PRINCIPAL */
                    <>
                        <div className="mb-10">
                            <h3 className="text-3xl font-black text-slate-800">Bienvenue dans la ruche ! 🐝</h3>
                            <p className="text-slate-500 mt-1 font-medium">Tableau de bord de gestion EasyBee.</p>
                        </div>

                        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
                            {/* Carte Catalogue */}
                            <div
                                onClick={() => setView('catalogue')}
                                className="bg-white p-8 rounded-3xl shadow-sm border border-slate-100 hover:shadow-xl hover:border-yellow-200 transition-all cursor-pointer group"
                            >
                                <div className="w-14 h-14 bg-yellow-100 rounded-2xl flex items-center justify-center text-2xl mb-6 group-hover:scale-110 group-hover:rotate-3 transition-transform">
                                    📦
                                </div>
                                <h4 className="text-xl font-bold text-slate-800">Catalogue</h4>
                                <p className="text-slate-500 text-sm mt-2 leading-relaxed">Consulter les prix et l'état des stocks en temps réel.</p>
                            </div>

                            {/* Carte Spéciale Préparateur */}
                            {user!.categorie.nom === 'preparateur' && (
                                <div
                                    onClick={() => setView('livraisons')}
                                    className="bg-slate-900 p-8 rounded-3xl shadow-xl text-white hover:bg-slate-800 transition-all cursor-pointer"
                                >
                                    <div className="w-14 h-14 bg-white/10 rounded-2xl flex items-center justify-center text-2xl mb-6">
                                        🚚
                                    </div>
                                    <h4 className="text-xl font-bold text-white">Livraisons</h4>
                                    <p className="text-slate-400 text-sm mt-2 leading-relaxed">Valider les arrivages de marchandises au dépôt.</p>
                                </div>
                            )}
                        </div>
                    </>
                ) : (
                    <div className="animate-in slide-in-from-bottom-4 duration-500">
                        <button
                            onClick={() => setView('menu')}
                            className="mb-6 flex items-center gap-2 text-slate-500 hover:text-yellow-600 font-bold transition-colors"
                        >
                            ⬅️ Retour au menu principal
                        </button>

                        {/* Affiche soit le catalogue, soit les livraisons */}
                        {view === 'catalogue' ? <Catalogue /> : <Livraisons />}
                    </div>
                )}
            </main>
        </div>
    );
};

export default Dashboard;
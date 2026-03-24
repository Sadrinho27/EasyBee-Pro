import { useEffect, useState } from 'react';
import type { Commande } from '../types';
import axios from 'axios';

const Livraisons = () => {
    const [commandes, setCommandes] = useState<Commande[]>([]);

    const fetchCommandes = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/commandes');
            // On ne garde que celles qui sont "en attente"
            const enAttente = response.data.filter((c: Commande) => c.statutCommande === "en attente");
            setCommandes(enAttente);
        } catch (error) {
            console.error("Erreur commandes:", error);
        }
    };

    useEffect(() => { fetchCommandes(); }, []);

    const validerLivraison = async (id: number) => {
        try {
            // Appel à ton super bouton PUT du Backend
            await axios.put(`http://localhost:8080/api/commandes/${id}/valider`);
            // Mise à jour visuelle immédiate : on retire la commande de la liste
            setCommandes(prev => prev.filter(c => c.id !== id));
            alert("Livraison validée ! Le stock a été mis à jour. 🐝");
        } catch (error) {
            alert("Erreur lors de la validation.");
        }
    };

    return (
        <div className="bg-white rounded-2xl shadow-sm border border-slate-200 overflow-hidden">
            <div className="p-6 border-b border-slate-100 bg-slate-50/50">
                <h3 className="text-lg font-bold text-slate-800">Commandes en attente de réception</h3>
            </div>
            <div className="p-6">
                {commandes.length === 0 ? (
                    <p className="text-slate-500 text-center py-10 italic">Aucune commande à réceptionner pour le moment. 🎉</p>
                ) : (
                    <div className="space-y-4">
                        {commandes.map(c => (
                            <div key={c.id} className="flex items-center justify-between p-4 rounded-xl border border-slate-100 bg-slate-50 hover:border-yellow-200 transition-all">
                                <div>
                                    <p className="font-bold text-slate-800">{c.nomCommande}</p>
                                    <span className="bg-yellow-100 text-yellow-800 text-xs px-2 py-0.5 rounded-full">
                                        x{c.quantite}
                                    </span>
                                    <p className="text-xs text-slate-500">Commandée le : {new Date(c.dateCommande).toLocaleDateString()}</p>
                                </div>
                                <button
                                    onClick={() => validerLivraison(c.id)}
                                    className="px-4 py-2 bg-green-600 hover:bg-green-700 text-white text-sm font-bold rounded-lg transition-colors shadow-md shadow-green-100"
                                >
                                    Valider la réception
                                </button>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default Livraisons;
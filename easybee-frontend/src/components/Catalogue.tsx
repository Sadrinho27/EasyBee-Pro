import { useEffect, useState } from 'react';
import toast, { Toaster } from 'react-hot-toast';
import type { Produit } from '../types';
import axios from 'axios';

const Catalogue = () => {
    const [produits, setProduits] = useState<Produit[]>([]);
    const [search, setSearch] = useState('');

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [produitSelectionne, setProduitSelectionne] = useState<Produit | null>(null);
    const [quantite, setQuantite] = useState<number>(10);
    const [isSubmitting, setIsSubmitting] = useState(false); // Pour bloquer le bouton pendant le chargement

    useEffect(() => {
        const fetchProduits = async () => {
            try {
                const response = await fetch('http://localhost:8080/api/produits');
                const data = await response.json();
                setProduits(data);
            } catch (error) {
                console.error("Erreur lors de la récupération des produits:", error);
            }
        };
        fetchProduits();
    }, []);

    const produitsFiltrés = produits.filter(p =>
        p.designation.toLowerCase().includes(search.toLowerCase())
    );

    const handleClickCommander = (produit: Produit) => {
        setProduitSelectionne(produit);
        setQuantite(10); // Valeur par défaut
        setIsModalOpen(true);
    };

    const validerCommande = async () => {
        if (!produitSelectionne || quantite <= 0) return;

        setIsSubmitting(true);

        try {
            await axios.post('http://localhost:8080/api/commandes', {
                nomCommande: produitSelectionne.designation,
                quantite: quantite,
                statutCommande: "en attente",
                dateCommande: new Date().toISOString(),
                categorieSalarie: { id: 1 }
            });

            setIsModalOpen(false);
            setIsSubmitting(false);

            toast.success(`${quantite} x ${produitSelectionne.designation} commandés ! 🐝`);
        } catch (error) {
            toast.error("❌ Erreur lors de l'envoi de la commande.");
            setIsSubmitting(false);
        }
    };

    return (
        <div className="bg-white rounded-2xl shadow-sm border border-slate-200 overflow-hidden animate-in fade-in duration-500 relative">

            {/* Barre de recherche */}
            <div className="p-4 border-b border-slate-100 bg-slate-50/50 flex justify-between items-center">
                <div className="relative w-full md:w-72">
                    <span className="absolute inset-y-0 left-0 pl-3 flex items-center text-slate-400">🔍</span>
                    <input
                        type="text"
                        placeholder="Rechercher un produit..."
                        className="w-full pl-10 pr-4 py-2 rounded-lg border border-slate-200 focus:ring-2 focus:ring-yellow-400 focus:border-transparent outline-none transition-all"
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                    />
                </div>
                <div className="text-sm text-slate-500 font-medium">
                    {produitsFiltrés.length} produit(s) trouvé(s)
                </div>
            </div>

            {/* Tableau */}
            <div className="overflow-x-auto">
                <table className="w-full text-left border-collapse">
                    <thead>
                        <tr className="bg-slate-50 text-slate-500 uppercase text-xs font-bold tracking-wider">
                            <th className="px-6 py-4">Désignation</th>
                            <th className="px-6 py-4">Prix Unitaire</th>
                            <th className="px-6 py-4">Stock Magasin</th>
                            <th className="px-6 py-4 text-right">Actions</th>
                        </tr>
                    </thead>
                    <tbody className="divide-y divide-slate-100">
                        {produitsFiltrés.map(produit => (
                            <tr className="hover:bg-slate-50/50 transition-colors" key={produit.id}>
                                <td className="px-6 py-4 font-semibold text-slate-800">{produit.designation}</td>
                                <td className="px-6 py-4 text-slate-600 font-medium">{produit.prix.toFixed(2)} €</td>
                                <td className="px-6 py-4">
                                    <div className="flex flex-col">
                                        <span className={`inline-flex items-center w-fit px-3 py-1 rounded-full text-xs font-bold border ${produit.stockMagasin < produit.stockMinimum
                                            ? 'bg-red-50 text-red-700 border-red-200 animate-pulse'
                                            : 'bg-emerald-50 text-emerald-700 border-emerald-200'
                                            }`}>
                                            {produit.stockMagasin < produit.stockMinimum ? '🚨' : '✅'} {produit.stockMagasin} en stock
                                        </span>

                                        {produit.stockMagasin < produit.stockMinimum && (
                                            <span className="text-[10px] text-red-500 font-bold mt-1 uppercase tracking-tighter">
                                                Seuil critique ({produit.stockMinimum})
                                            </span>
                                        )}
                                    </div>
                                </td>
                                <td className="px-6 py-4 text-right">
                                    <button
                                        onClick={() => handleClickCommander(produit)}
                                        className="px-3 py-1 bg-yellow-500 hover:bg-yellow-600 text-white rounded-md font-bold text-xs transition-all shadow-sm active:scale-95"
                                    >
                                        Commander
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

            {/* Modale pour passer une commande */}
            {isModalOpen && produitSelectionne && (
                <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/50 backdrop-blur-sm transition-opacity">
                    <div className="bg-white rounded-2xl shadow-2xl p-6 w-full max-w-md transform transition-all animate-in zoom-in-95 duration-200">

                        <div className="flex justify-between items-center mb-4">
                            <h3 className="text-xl font-bold text-slate-800">
                                Commander un produit
                            </h3>
                            <button onClick={() => setIsModalOpen(false)} className="text-slate-400 hover:text-slate-600">
                                ✖
                            </button>
                        </div>

                        <div className="mb-6 p-4 bg-slate-50 rounded-xl border border-slate-100">
                            <p className="font-semibold text-slate-700">{produitSelectionne.designation}</p>
                            <p className="text-sm text-slate-500 mt-1">Stock actuel : {produitSelectionne.stockMagasin}</p>
                        </div>

                        <div className="mb-6">
                            <label className="block text-sm font-bold text-slate-700 mb-2">
                                Quantité à commander :
                            </label>
                            <input
                                type="number"
                                min="1"
                                value={quantite}
                                onChange={(e) => setQuantite(parseInt(e.target.value) || 0)}
                                className="w-full px-4 py-3 rounded-xl border border-slate-200 focus:ring-2 focus:ring-yellow-400 focus:border-transparent outline-none transition-all text-lg font-medium"
                                autoFocus
                            />
                        </div>

                        <div className="flex justify-end gap-3 mt-6">
                            <button
                                onClick={() => setIsModalOpen(false)}
                                disabled={isSubmitting}
                                className="px-5 py-2.5 text-slate-600 bg-slate-100 hover:bg-slate-200 rounded-xl font-bold transition-colors"
                            >
                                Annuler
                            </button>
                            <button
                                onClick={validerCommande}
                                disabled={isSubmitting || quantite <= 0}
                                className="px-5 py-2.5 bg-yellow-500 hover:bg-yellow-600 text-white rounded-xl font-bold transition-all disabled:opacity-50 disabled:cursor-not-allowed flex items-center gap-2 shadow-sm"
                            >
                                {isSubmitting ? 'Envoi en cours...' : 'Confirmer la commande'}
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {/* Le gestionnaire de notifications */}
            <Toaster position="bottom-right" reverseOrder={false} />
        </div>
    );
};

export default Catalogue;
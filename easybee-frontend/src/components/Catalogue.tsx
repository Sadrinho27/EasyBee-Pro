import React, { useEffect, useState } from 'react';
import type { Produit } from '../types';
import axios from 'axios';

const Catalogue = () => {
    const [produits, setProduits] = useState<Produit[]>([]);
    const [search, setSearch] = useState('');

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

    // Logique de filtrage pour la barre de recherche
    const produitsFiltrés = produits.filter(p =>
        p.designation.toLowerCase().includes(search.toLowerCase())
    );

    const passerCommande = async (produit: Produit) => {
        const quantiteSaisie = window.prompt(`Combien d'unités de "${produit.designation}" voulez-vous commander ?`, "10");

        // On convertit le texte en vrai nombre entier
        const quantite = parseInt(quantiteSaisie || "0", 10);

        if (!quantite || isNaN(quantite) || quantite <= 0) {
            alert("Commande annulée ou quantité invalide.");
            return;
        }

        try {
            await axios.post('http://localhost:8080/api/commandes', {
                nomCommande: produit.designation, // Le nom reste propre
                quantite: quantite,               // ⬅️ On envoie la vraie valeur à la BDD !
                statutCommande: "en attente",
                dateCommande: new Date().toISOString(),
                categorieSalarie: { id: 1 }
            });
            alert(`Succès : ${quantite} x ${produit.designation} commandés ! 🐝`);
        } catch (error) {
            alert("Erreur lors de l'envoi de la commande.");
        }
    };

    return (
        <div className="bg-white rounded-2xl shadow-sm border border-slate-200 overflow-hidden animate-in fade-in duration-500">
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
                                        onClick={() => passerCommande(produit)}
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
        </div>
    );
};

export default Catalogue;
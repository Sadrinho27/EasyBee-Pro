export interface User {
  id: number;
  nomSalarie: string;
  prenomSalarie: string;
  identifiant: string;
  categorie: {
    id: number;
    nom: string;
  };
}

export interface Produit {
  id: number;
  designation: string;
  prix: number;
  stockMagasin: number;
  stockMinimum: number;
}

export interface Commande {
  id: number;
  dateCommande: string;
  statutCommande: string;
  nomCommande: string; // C'est le nom du produit dans ta base
  categorieSalarie: {
    nom: string;
  };
}
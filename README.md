# 🐝 EasyBee Pro

EasyBee Pro est une application web Fullstack de gestion interne (ERP/SaaS) conçue pour optimiser la chaîne logistique et les ventes d'une exploitation apicole. L'application fait office de pont de communication en temps réel entre la boutique physique, l'entrepôt de stockage et le bureau de direction.

---

## 🚀 Fonctionnalités Clés & Parcours Utilisateurs (RBAC)

L'application intègre un système de contrôle d'accès basé sur les rôles (**Role-Based Access Control**) afin de sécuriser et d'adapter l'interface aux besoins réels de chaque employé :

*   **🏪 Espace Vendeur (Boutique) :** Accès au catalogue de produits, suivi de l'état du *Stock Magasin* en temps réel, et déclenchement d'alertes visuelles lorsque les seuils critiques sont atteints. Possibilité de passer des commandes d'approvisionnement directement à l'entrepôt.
*   **🏭 Espace Préparateur (Entrepôt) :** Tableau de bord logistique listant les commandes en attente de préparation. Validation des expéditions avec mise à jour automatique et atomique des stocks (Diminution du *Stock Entrepôt* / Augmentation du *Stock Magasin*).
*   **💼 Espace Gérant (Administration) :** Interface d'administration avancée. Accès à la gestion des salariés (Ressources Humaines) et au **CRUD complet des produits** (Ajout, modification des prix, gestion des seuils, suppression) connecté à l'API.

---

## 🛠️ Architecture Technique & Stack

Le projet a été pensé selon les standards modernes de l'industrie pour garantir performance, isolation des composants et scalabilité.

### 💻 Frontend
*   **React (TypeScript)** : Structure modulaire et typage fort pour un code robuste.
*   **React Router DOM** : Implémentation d'un layout persistant et de **routes imbriquées (Nested Routes)** avec gestion des routes protégées selon le rôle.
*   **Tailwind CSS** : Interface utilisateur moderne et épurée (style SaaS), dotée d'un **Mode Sombre centralisé** géré via un `ThemeContext` React et persistant via `localStorage`.
*   **State Management** : Gestion globale de la session utilisateur via un `AuthContext` sécurisé.

### ⚙️ Backend
*   **Java Spring Boot** : API REST structurée selon le pattern Controller-Service-Repository.
*   **Spring Data JPA / Hibernate** : Mapping de la base de données relationnelle et requêtes optimisées.
*   **MySQL** : Base de données relationnelle stockant les produits, les salariés et les catégories de rôles.

### 🐋 DevOps & Déploiement
*   **Docker & Docker Compose** : Orchestration multiconteneur pour isoler le backend, le frontend et la base de données.
*   **Nginx** : Serveur web configuré en Reverse Proxy pour servir le build React et gérer proprement le routage virtuel Single Page Application (`try_files`).
*   **Spring Boot Buildpacks** : Utilisation de `spring-boot:build-image` pour générer des images Docker d'API hautement optimisées et légères.
*   **CI/CD (GitHub Actions)** : Pipeline d'intégration continue automatisant les builds et la validation du code à chaque commit.

---

## 📂 Structure du Projet

```text
easybee-pro/
├── easybee-backend/     # API Java Spring Boot
│   ├── src/
│   │   ├── controller/  # Points d'entrée de l'API (Endpoints REST)
│   │   ├── service/     # Logique métier et validation des données
│   │   ├── entity/      # Modèles de données JPA (Produit, Salarie...)
│   │   └── repository/  # Interfaces d'accès à la base de données
│   └── pom.xml
├── easybee-frontend/    # Interface Utilisateur React
│   ├── src/
│   │   ├── components/  # Composants d'interface (Catalogue, Administration...)
│   │   ├── contexts/    # Contextes globaux (AuthContext, ThemeContext)
│   │   ├── pages/       # Layout principal et squelette de l'application
│   │   └── types/       # Déclarations des interfaces TypeScript
│   └── Dockerfile       # Build multi-stage avec Nginx
└── docker-compose.yml   # Orchestration globale du projet
```

---

## ⚙️ Installation et Lancement en Local

### Prérequis

* Docker et Docker Compose installés.
* Java 17+ et Maven (optionnel, pour le développement sans conteneur).

### Démarrage Rapide

1. **Cloner le dépôt :**

```bash
   git clone [https://github.com/Sadrinho27/EasyBee-Pro.git](https://github.com/Sadrinho27/EasyBee-Pro.git)
   cd easybee-pro
```

2. **Générer l'image Docker du Backend avec Spring Boot Buildpacks :**

```bash
   cd easybee-backend
   ./mvnw spring-boot:build-image -DskipTests
   cd ..
```

3. **Lancer l'ensemble de l'infrastructure via Docker Compose :**

```bash
   docker compose up -d
```

L'application est maintenant disponible aux adresses suivantes :

* **Frontend :** `http://localhost:3000`
* **API Backend :** `http://localhost:8080/api`

---

## 📈 Évolutions Futures

* Intégration de Spring Security avec jetons JWT pour l'authentification et la sécurisation des endpoints au niveau du backend.
* Mise en place de graphiques analytiques (Recharts) sur le tableau de bord du gérant pour suivre l'évolution financière des ventes de miel.

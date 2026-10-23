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

## ⚙️ Installation locale

### 📋 Prérequis
| Outil | Version | Rôle |
|---|---|---|
| **JDK** | 17 minimum (21 recommandé) | Exécuter le backend Spring Boot |
| **MySQL** | 8.x (ou XAMPP / MariaDB) | Base de données |
| **Node.js** | 22 LTS | Frontend React (facultatif pour le backend seul) |

> Maven n'est pas nécessaire : le projet embarque le wrapper `mvnw`.

### 1. Cloner le projet
```bash
git clone https://github.com/Sadrinho27/EasyBee-Pro.git
cd EasyBee-Pro
```

### 2. Créer la base de données
Importer le script `easybee-backend/sql/easybee.sql`, par exemple via **phpMyAdmin → Importer**, ou en ligne de commande :
```bash
mysql -u root -p < easybee-backend/sql/easybee.sql
```
Le script crée la base `easybee`, toutes les tables, ainsi que les catégories de salariés et les produits de démonstration.

### 3. Configurer la connexion
Vérifier les identifiants dans `easybee-backend/src/main/resources/application.properties` :
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/easybee?serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
```
> Par défaut, la configuration correspond à XAMPP (utilisateur `root`, sans mot de passe). Adapter si besoin.

### 4. Lancer le backend
```bash
cd easybee-backend
./mvnw spring-boot:run        # Windows (PowerShell) : .\mvnw.cmd spring-boot:run
```
L'API est disponible sur **http://localhost:8080**. Test rapide : http://localhost:8080/api/produits

### 5. Lancer les tests
```bash
./mvnw test
```
Les tests unitaires (JUnit 5 + Mockito) couvrent la logique métier des services et ne nécessitent **pas** de base de données.

### 6. Lancer le frontend (facultatif)
```bash
cd easybee-frontend
npm install
npm run dev
```
L'interface est disponible sur **http://localhost:5173**.

### 🐋 Avec Docker
```bash
docker compose up -d
```
> ⚠️ Le `docker-compose.yml` crée une base nommée `easybee` (mot de passe `root`). Pour y importer le script, utiliser phpMyAdmin sur **http://localhost:8081**.

---

## 🔌 Endpoints de l'API

| Ressource | Méthode | URL | Description |
|---|---|---|---|
| Produits | `GET` | `/api/produits` | Liste des produits |
| | `GET` | `/api/produits/{id}` | Détail d'un produit |
| | `GET` | `/api/produits/alerte-stock` | Produits sous le stock minimum |
| | `POST` / `PUT` / `DELETE` | `/api/produits[/{id}]` | Création, modification, suppression |
| Salariés | `GET` | `/api/salaries[/{id}]` | Liste ou détail |
| | `POST` / `PUT` / `DELETE` | `/api/salaries[/{id}]` | CRUD (mot de passe haché avec BCrypt) |
| Commandes | `GET` | `/api/commandes[/{id}]` | Liste ou détail |
| | `POST` | `/api/commandes` | Création (statut « en attente ») |
| | `PUT` | `/api/commandes/{id}/valider` | Validation : transfert du stock entrepôt vers le magasin |
| Auth | `POST` | `/api/auth/login` | Connexion |

**Format des erreurs** (commun à toute l'API) :
```json
{ "status": 404, "message": "Produit introuvable avec l'ID 99999", "date": "2026-10-23T14:32:10" }
```
Codes utilisés : `400` données invalides · `401` identifiants incorrects · `404` ressource introuvable · `409` conflit (identifiant déjà pris, commande déjà validée, stock insuffisant).

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

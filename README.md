# MDD - Monde de Dév
## Application Full-Stack

Ce projet est une application complète de gestion d'utilisateurs avec authentification JWT, développée avec Spring Boot (backend) et Angular (frontend).

### Installation

```bash
cd back

# Avec Maven Wrapper (Windows)
.\mvnw.cmd clean install

# Démarrage
.\mvnw.cmd spring-boot:run
```

### Configuration

Modifiez `application.properties` pour votre base de données :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mdd_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=votre_mot_de_passe

jwt.secret=VotreCléSecrèteDe256BitsMinimum
jwt.expiration=86400000
```

## 💻 Frontend - Angular

### Technologies
- **Angular 14**
- **Angular Material 14**
- **RxJS 7**
- **TypeScript 4.7**

### Installation

```bash
cd front

# Installation des dépendances
npm install

# Démarrage
npm start
```

L'application sera accessible sur `http://localhost:4200`

### Pages

- **Home (`/`)** : Page d'accueil avec liens login/register
- **Login (`/login`)** : Connexion utilisateur
- **Register (`/register`)** : Inscription utilisateur

### Services

#### AuthService
Gère l'authentification :
- Login/Register/Logout
- Stockage du token JWT
- Observables pour l'état de connexion
- Validation du token

#### AuthInterceptor
Ajoute automatiquement le token JWT aux requêtes HTTP

### Guards

- **AuthGuard** : Protège les routes nécessitant une authentification
- **UnauthGuard** : Empêche l'accès aux pages login/register si déjà connecté

---

## 🚀 Démarrage rapide

### 1. Base de données MySQL

```sql
-- La base de données sera créée automatiquement au premier lancement
-- Assurez-vous que MySQL est démarré sur le port 3306
```

### 2. Backend

```bash
cd back
.\mvnw.cmd spring-boot:run
```

Le backend démarrera sur `http://localhost:8080`

### 3. Frontend

```bash
cd front
npm install
npm start
```

Le frontend démarrera sur `http://localhost:4200`

### 4. Tester l'application

1. Accédez à `http://localhost:4200`
2. Cliquez sur "S'inscrire"
3. Créez un compte avec :
   - Email valide
   - Nom d'utilisateur (3-50 caractères)
   - Mot de passe (min. 6 caractères)
4. Vous serez automatiquement connecté
5. Testez la déconnexion

---

## ✨ Fonctionnalités

### Gestion des utilisateurs

✅ **Inscription**
- Validation email unique
- Validation username unique (3-50 caractères)
- Mot de passe sécurisé (min. 6 caractères, hashé avec BCrypt)

✅ **Connexion**
- Avec email OU nom d'utilisateur
- Génération de token JWT (validité 24h)
- Persistence de la session

✅ **Profil utilisateur**
- Consultation du profil
- Modification email/username/password
- Timestamps de création/modification

✅ **Déconnexion**
- Suppression du token côté client

### Sécurité

🔒 **Backend**
- Authentification JWT stateless
- Mots de passe hashés avec BCrypt
- Validation des entrées utilisateur
- Protection CSRF désactivée (JWT)
- CORS configuré pour le frontend

🔒 **Frontend**
- Token stocké dans localStorage
- Vérification automatique de la validité du token
- Déconnexion automatique si token invalide
- Guards pour protéger les routes
- Intercepteur HTTP pour ajouter le token


## 📦 Versions des dépendances principales

### Backend
- Spring Boot: 3.2.2
- Java: 17
- MySQL Connector: Latest (via Spring Boot parent)
- JWT (jjwt): 0.12.3
- Lombok: 1.18.30

### Frontend
- Angular: 14.1.0
- Angular Material: 14.2.5
- TypeScript: 4.7.2
- RxJS: 7.5.0

---

## 📄 Licence

© 2026 - David Cardigos

---



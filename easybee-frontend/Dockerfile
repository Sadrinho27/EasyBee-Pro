# ÉTAPE 1 : Build de l'application
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# ÉTAPE 2 : Serveur de production (Nginx)
FROM nginx:stable-alpine
# On copie le résultat du build (dossier dist) dans le dossier public d'Nginx
COPY --from=build /app/dist /usr/share/nginx/html
# On expose le port 80 (port par défaut d'Nginx)
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
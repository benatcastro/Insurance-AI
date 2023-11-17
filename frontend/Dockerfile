FROM node:18-alpine
LABEL authors="becastro"

WORKDIR /app

COPY . ./
COPY package.json ./
COPY package-lock.json ./
RUN npm install --force
RUN npm run build

USER node:node
CMD ["node","build/index.js"]

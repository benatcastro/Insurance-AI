FROM node:21-alpine as DEV
LABEL authors="becastro"

WORKDIR /home/app

COPY  ./package.json .
RUN npm install

USER node:node
CMD npm run dev || tail -f

FROM node:21-alpine as BUILD
COPY --from=DEV /app/build ./bui
CMD ["node", "./index.js"]

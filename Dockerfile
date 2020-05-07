FROM node:12.16.3-alpine3.11

WORKDIR /usr/src/app

COPY package.json yarn.lock ./
RUN yarn

COPY . .

ENTRYPOINT ["node", "index.js"]

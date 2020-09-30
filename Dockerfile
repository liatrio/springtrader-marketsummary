FROM node:12.18.4-alpine

WORKDIR /usr/src/app

COPY package.json yarn.lock ./
RUN yarn --production

COPY . .

ENTRYPOINT ["node", "index.js"]

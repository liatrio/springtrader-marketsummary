FROM node:12.18.1-alpine3.9

WORKDIR /usr/src/app

COPY package.json yarn.lock ./
RUN yarn --production

COPY . .

ENTRYPOINT ["node", "index.js"]

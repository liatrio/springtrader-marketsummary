FROM node:12.16.3

WORKDIR /usr/src/app

COPY package.json yarn.lock ./
RUN yarn --production

COPY . .

ENTRYPOINT ["node", "index.js"]

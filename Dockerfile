# base image
FROM node:9.5

# set working directory
RUN mkdir /opt/project/client
WORKDIR /opt/project/client

ENV PATH /opt/project/client/node_modules/.bin:$PATH

ADD package.json /opt/project/client/package.json
RUN npm install --silent
RUN npm install react-scripts@1.1.1 -g --silent
RUN npm install -g serve

CMD ["npm", "start"]

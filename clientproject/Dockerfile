FROM node:20-alpine
WORKDIR /app

# copy package files
COPY package*.json ./

# install dependencies
RUN npm install

# copy toàn bộ source
COPY . .

# expose port Vite dev
EXPOSE 80

# chạy dev server
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]

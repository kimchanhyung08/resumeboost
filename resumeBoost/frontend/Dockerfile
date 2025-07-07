# React 앱 빌드
FROM node:22.13.1 AS build

WORKDIR /app

COPY /package*.json ./
RUN npm install
COPY /. .
RUN npm run build

# Nginx로 서빙
FROM nginx:alpine

COPY --from=build /app/build /usr/share/nginx/html

# Nginx 설정 파일 복사
COPY /nginx/nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]

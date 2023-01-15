# **Pa'ca API**
Rest API para la gestión de operaciones de Pa'Ca


## **Run**

Correr toda la API usando docker-compose
```bash
cd pa-ca
./mvnw package
cd ../ 
docker-compose up --build
```

Correr la DB usando solo docker-compose y la API de manera local
```bash
docker-compose up postgres
cd pa-ca
./mvnw spring-boot:run
```
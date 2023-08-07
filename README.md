# **Pa'ca API**

Rest API para la gestión de operaciones de Pa'Ca


## Requisitos.
1. Java 17 : [Enlace a la pagina de Oracle](https://www.oracle.com/java/technologies/downloads/#java17)
2. Maven: Se puede instalar (En dsitribuciones basadas en debian) usando el comando: `sudo apt install maven`
3. Docker

## **Run**

Correr toda la API usando docker-compose

```bash
cd pa-ca
./mvnw package [-Dmaven.test.skip]
cd ../
docker-compose up --build
```

Correr la DB usando solo docker-compose y la API de manera local

```bash
docker-compose up postgres
cd pa-ca
./mvnw spring-boot:run
```

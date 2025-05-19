# lab-sofka
Laboratorio de microservicios con eventos Kafka

# INSTRUCCIONES DE CONFIGURACIÓN Y EJECUCIÓN

## INDICACIONES GENERALES
El proyecto ha sido compilado y probado en un sistema operativo Mac OS con JDK 17.

### La base datos que se usa es PostgreSQL, una imagen en docker.

## REQUISITOS
1. Descargar e instalar Docker (para Mac/Windows/Linux).
2. Instalar JDK 17 (local).
3. Microservicios creados con Spring Boot versión 3.4.3. (local)
4. Instalar y configurar Maven. (local)

## CLONAR REPOSITORIO
```bash
git clone https://github.com/edacamo/test-sofka-acarchipulla.git
```

## PRUEBAS UNITARIAS E INTEGRALES

### Para revisar la prueba unitaria (en `microservice-persons`):
```bash
cd microservice-persons
mvn clean install
```

###  Para revisarla prueba integral (en `microservice-accounts`):
```bash
cd microservice-accounts
mvn clean install
```

## EJECUTAR DOCKER-COMPOSE PARA DESPLEGAR LOS CONTENEDORES EN DOCKER

 Ejecutar el siguiente comando para construir y desplegar los contenedores:
```bash
docker-compose -f docker-compose-sofka.yml up --build
```

## LOGS EN CONTENEDORES
### Para monitorear los logs de los contenedores, ejecutar los siguientes comandos:
```bash
docker logs -f kafka
docker logs -f zookeeper
docker logs -f microservice-persons
docker logs -f microservice-accounts
```

## Rutas documentación con Swagger
### Las urls para acceder a la documentación de los microservicios son:

* Microservicio Persons
```bash
http://localhost:8001/swagger
```

* Microservicio Accounts
```bash
http://localhost:8002/swagger
```
# iBank

## Credenciales por defecto
La base de datos incluye un usuario creado por defecto y otros datos para poder usarla 
tan pronto como se configure.

* usuario: demo / password: demo

## Caracteristicas
* Frontend 100% responsive (web/mobile)
* Autenticacion y Autorizacion (Spring Web Security)
* Base de datos postgresql (Spring JPA)

## Servicios
* La API puede ser accedida desde localhost:8888
* El servidor de base de datos en localhost:5432 (Postgress)
* La interfaz web se accede desde localhost:8080

## Dockerizar Servidor PostgreSQL
```
cd ./sql
docker build -t pgserver .
docker run --name pgserver -e POSTGRES_PASSWORD=123456 -d pgserver
```

## Modificar el backend para usar el servidor sql
Modifica el fichero application.properties y cambia la linea por
la de tu servidor postgreSQL en docker (puedes usar ip addr)
```
spring.datasource.url=jdbc:postgresql://<SERVIDOR_POSTGRESQL_DOCKER>:5432/fitnesskcalc
```

## Iniciar servidor REST (backend)
```
git clone https://github.com/damianS7/ibank-backend.git
cd fitnesskcalck-backend
./mvnw package
java -jar target/*.jar
```
# Etapa 1: Compilación de la aplicación utilizando Maven
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copiar el archivo de configuración de dependencias
COPY pom.xml .

# Descargar las dependencias en caché (optimiza los tiempos del pipeline)
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar el archivo ejecutable .jar sin correr las pruebas unitarias aún
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Crear la imagen ligera final para ejecución en la nube
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el archivo .jar compilado desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto por defecto en el que corre Spring Boot
EXPOSE 8080

# Comando para ejecutar el microservicio
ENTRYPOINT ["java", "-jar", "app.jar"]
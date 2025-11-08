# Stage 1: Build da Aplicação
# Usa a imagem do Maven para compilar o código Java
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
# Copia o pom.xml e o código fonte
COPY pom.xml .
COPY src ./src
# Compila o projeto e gera o JAR
RUN mvn clean package -DskipTests

# Stage 2: Criação da Imagem Final
# Usa uma imagem JRE (Java Runtime Environment) menor para a execução
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copia o JAR gerado no estágio de build
COPY --from=build /app/target/*.jar app.jar

# CORREÇÃO: Adiciona permissão de execução ao arquivo JAR
# Isso resolve o erro "Permission denied" ao tentar executar o JAR
RUN chmod +x app.jar

# Expõe a porta que o Spring Boot usa (padrão 8080)
EXPOSE 8080
# Define o ponto de entrada para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
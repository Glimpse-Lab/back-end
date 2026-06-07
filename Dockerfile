FROM amazoncorretto:21-alpine AS build
WORKDIR /app
COPY gpm . 
RUN sh mvnw clean package -DskipTests

FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

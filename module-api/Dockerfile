FROM openjdk:11-jre

COPY build/libs/module-api-1.0.0.jar app.jar

ENTRYPOINT ["java", "-DSpring.profiles.active=prod", "-jar", "app.jar"]
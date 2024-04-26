FROM gradle:8.5.0-jdk17

COPY . /app/

WORKDIR /app

RUN gradle build

CMD ["java", "-jar", "/app/build/libs/rock.jar"]
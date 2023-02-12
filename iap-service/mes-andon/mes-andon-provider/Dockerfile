FROM java:8
EXPOSE 8080

VOLUME /tmp
ADD target/*.jar /app.jar
RUN bash -C 'touch /app. jar'
ENTRYPOINT ["java" ,"-jar","/app.jar","--spring.profiles.active=prod"]

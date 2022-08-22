FROM openjdk:8-jdk-alpine
RUN apk add --no-cache tzdata
EXPOSE 8080
ADD target/orders-0.0.1-SNAPSHOT.jar orders.jar
#RUN echo "Asia/Kuala_Lumpur" > /etc/timezone
RUN apk add --update bash && rm -rf /var/cache/apk/*
ENV TZ Australia/Sydney

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/orders.jar"]

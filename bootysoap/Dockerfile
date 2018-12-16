FROM openjdk:8-jdk-alpine
ENV DEPLOYMENT_DIR /app/bootysoap
RUN mkdir -p ${DEPLOYMENT_DIR}
COPY ./target/bootysoap.jar ${DEPLOYMENT_DIR}
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar ${DEPLOYMENT_DIR}/bootysoap.jar
EXPOSE 9080
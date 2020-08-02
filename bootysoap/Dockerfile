FROM openjdk:11-jre-slim-buster
ENV DEPLOYMENT_DIR /app/bootysoap
RUN mkdir -p ${DEPLOYMENT_DIR}
COPY ./target/bootysoap.jar ${DEPLOYMENT_DIR}
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar ${DEPLOYMENT_DIR}/bootysoap.jar
EXPOSE 9080

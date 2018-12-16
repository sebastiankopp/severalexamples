#!/bin/sh
groupId=de.sebastiankopp.severalexamples
artifactId=bootysoap
mvn clean package && docker build -t ${groupId}/${artifactId} .
docker rm -f ${artifactId} || true && docker run -d -p 9080:9080 --name ${artifactId} ${groupId}/${artifactId}

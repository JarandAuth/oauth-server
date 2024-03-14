#!/bin/bash

APP=oauth-server
IMAGE=$APP:local
NET=jarand-net

docker build -t $IMAGE .

docker rm -f -v $APP

docker network create $NET || true

docker run -dt --name $APP -p 8080:8080 --network $NET \
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:29091 \
  -e KAFKA_PARTITIONS=2 \
  -e KAFKA_REPLICAS=1 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/$APP-dev-db \
  -e SPRING_DATASOURCE_USERNAME=$APP-dev-dbo \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  $IMAGE

#!/bin/bash

APP=oauth-server
IMAGE=ghcr.io/jarandauth/oauth-server:main
NET=jarand-net

docker rm -f -v $APP

docker network create $NET || true

docker run -dt --name $APP -p 8080:8080 --network $NET --pull=always --restart unless-stopped \
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=node01.haugerud.network:9091,node02.haugerud.network:9091,node03.haugerud.network:9091 \
  -e KAFKA_PARTITIONS=10 \
  -e KAFKA_REPLICAS=3 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://oauth-server-dev-db:5432/$APP-dev-db \
  -e SPRING_DATASOURCE_USERNAME=$APP-dev-dbo \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  -e SPRINGDOC_SWAGGER_UI_URL=/v0/oauth-server/v3/api-docs \
  -e SPRINGDOC_SWAGGER_UI_CONFIG_URL=/v0/oauth-server/v3/api-docs/swagger-config \
  $IMAGE

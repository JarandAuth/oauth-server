#!/bin/bash

APP=oauth-server
NET=jarand-net

docker rm -f -v postgres

docker network create $NET || true

docker run -dt --name postgres -p 5432:5432 --network $NET \
  -e POSTGRES_DB=$APP-dev-db \
  -e POSTGRES_USER=$APP-dev-dbo \
  -e POSTGRES_PASSWORD=postgres \
  postgres:15

#!/bin/bash

APP=oauth-server
NET=jarand-net

docker rm -f -v $APP-dev-db

docker network create $NET || true

docker run -dt --name $APP-dev-db -p 5432:5432 --network $NET --pull=always --restart unless-stopped \
  --mount type=bind,source="$(pwd)"/Storage/Internal/Postgres/OAuthServerDevDb,target=/var/lib/postgresql/data \
  -e POSTGRES_DB=$APP-dev-db \
  -e POSTGRES_USER=$APP-dev-dbo \
  -e POSTGRES_PASSWORD=postgres \
  postgres:15

CREATE TABLE app
(
    id            UUID PRIMARY KEY,
    name          VARCHAR        NOT NULL,
    client_id     VARCHAR UNIQUE NOT NULL,
    client_secret VARCHAR        NOT NULL,
    owner         VARCHAR        NOT NULL,
    created       VARCHAR        NOT NULL
);

CREATE TABLE app_redirect_uri
(
    redirect_uri VARCHAR NOT NULL,
    app_id       UUID    NOT NULL
);

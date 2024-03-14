CREATE TABLE account
(
    id       UUID PRIMARY KEY,
    email    VARCHAR UNIQUE NOT NULL,
    password VARCHAR        NOT NULL,
    created  VARCHAR        NOT NULL
);

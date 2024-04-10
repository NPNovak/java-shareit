CREATE TABLE IF NOT EXISTS users
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    email   VARCHAR(50)  NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS requests
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    description  VARCHAR(255)                NOT NULL,
    requester_id INTEGER                     NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    created      timestamp without time zone NOT NULL

);

CREATE TABLE IF NOT EXISTS items
(
    id      INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    description  VARCHAR(200) NOT NULL,
    is_available BOOLEAN      NOT NULL,
    owner_id     INTEGER      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    request_id   INTEGER REFERENCES requests (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start_date timestamp without time zone NOT NULL,
    end_date   timestamp without time zone NOT NULL,
    item_id    INTEGER                     NOT NULL REFERENCES items (id) ON DELETE CASCADE,
    booker_id  INTEGER                     NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    status     VARCHAR(10)                 NOT NULL
);

CREATE TABLE IF NOT EXISTS comments
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text       VARCHAR(255)                NOT NULL,
    item_id    INTEGER                     NOT NULL REFERENCES items (id) ON DELETE CASCADE,
    author_id  INTEGER                     NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    created    timestamp without time zone NOT NULL
);
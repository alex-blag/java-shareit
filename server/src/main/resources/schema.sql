DROP TABLE IF EXISTS users CASCADE;

DROP TABLE IF EXISTS requests CASCADE;

DROP TABLE IF EXISTS items CASCADE;

DROP TABLE IF EXISTS bookings CASCADE;

DROP TABLE IF EXISTS comments CASCADE;

CREATE TABLE users (
    id          BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    name        CHARACTER VARYING(100) NOT NULL,
    email       CHARACTER VARYING(100) NOT NULL,

    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uc_users_email UNIQUE (email)
);

CREATE TABLE requests (
    id              BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    description     CHARACTER VARYING(200) NOT NULL,
    requester_id    BIGINT NOT NULL,
    created         TIMESTAMP NOT NULL,

    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT fk_requests_users FOREIGN KEY (requester_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE items (
    id          BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    name        CHARACTER VARYING(100) NOT NULL,
    description CHARACTER VARYING(200) NOT NULL,
    available   BOOLEAN NOT NULL,
    owner_id    BIGINT NOT NULL,
    request_id  BIGINT,

    CONSTRAINT pk_items PRIMARY KEY (id),
    CONSTRAINT fk_items_users FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_items_requests FOREIGN KEY (request_id) REFERENCES requests(id) ON DELETE CASCADE
);

CREATE TABLE bookings (
    id          BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    start_date  TIMESTAMP NOT NULL,
    end_date    TIMESTAMP NOT NULL,
    item_id     BIGINT NOT NULL,
    booker_id   BIGINT NOT NULL,
    status      CHARACTER VARYING(10) NOT NULL,

    CONSTRAINT pk_bookings PRIMARY KEY (id),
    CONSTRAINT fk_bookings_items FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
    CONSTRAINT fk_bookings_users FOREIGN KEY (booker_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE comments (
    id          BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    text        CHARACTER VARYING(200) NOT NULL,
    item_id     BIGINT NOT NULL,
    author_id   BIGINT NOT NULL,
    created     TIMESTAMP NOT NULL,

    CONSTRAINT pk_comments PRIMARY KEY (id),
    CONSTRAINT fk_comments_items FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_users FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);

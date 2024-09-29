DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS booking;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(512)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    owner_id    BIGINT                                  NOT NULL,
    name        VARCHAR(255)                            NOT NULL,
    description VARCHAR(512)                            NOT NULL,
    CONSTRAINT pk_item PRIMARY KEY (id),
    CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_items_owner_id ON items (owner_id);

CREATE TABLE IF NOT EXISTS booking
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    item_id   BIGINT                                  NOT NULL,
    booker_id BIGINT                                  NOT NULL,
    status    INTEGER                                 NOT NULL DEFAULT 1,
    start_at  TIMESTAMP                               NOT NULL,
    end_at    TIMESTAMP                               NOT NULL,
    CONSTRAINT pk_booking PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    item_id BIGINT                                  NOT NULL REFERENCES items (id),
    user_id BIGINT                                  NOT NULL REFERENCES users (id),
    comment VARCHAR(512)                            NOT NULL,
    created TIMESTAMP DEFAULT now(),
    CONSTRAINT pk_comment PRIMARY KEY (id),
    CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_item FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE
);
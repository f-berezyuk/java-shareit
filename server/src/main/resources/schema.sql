DROP TABLE IF EXISTS answers CASCADE;
DROP TABLE IF EXISTS item_requests CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS booking CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(512)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_users_email ON users (email);

CREATE TABLE IF NOT EXISTS items
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    owner_id    BIGINT                                  NOT NULL,
    name        VARCHAR(255)                            NOT NULL,
    description VARCHAR(512)                            NOT NULL,
    available   boolean DEFAULT FALSE,
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

CREATE INDEX IF NOT EXISTS idx_booking_booker_item_status_end_at ON Booking (booker_id, item_id, status, end_at);
CREATE INDEX IF NOT EXISTS idx_booking_booker ON Booking (booker_id);
CREATE INDEX IF NOT EXISTS idx_booking_item ON Booking (item_id);
CREATE INDEX IF NOT EXISTS idx_booking_end_at ON Booking (end_at);
CREATE INDEX IF NOT EXISTS idx_booking_start_at ON Booking (start_at);

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

CREATE TABLE IF NOT EXISTS item_requests
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id BIGINT                                  NOT NULL REFERENCES users (id),
    description VARCHAR(512)                            NOT NULL,
    created TIMESTAMP DEFAULT now(),
    CONSTRAINT pk_request PRIMARY KEY (id),
    CONSTRAINT fk_request_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS answers
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    item_id BIGINT                                  NOT NULL REFERENCES items (id),
    item_request_id BIGINT                                  NOT NULL REFERENCES item_requests (id),
    CONSTRAINT pk_answers PRIMARY KEY (id),
    CONSTRAINT fk_answers_request FOREIGN KEY (item_request_id) REFERENCES item_requests (id) ON DELETE CASCADE,
    CONSTRAINT fk_answers_item FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE
);
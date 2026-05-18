CREATE SEQUENCE IF NOT EXISTS check_point_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS checkpoint_sessions_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE check_point
(
    id                    BIGINT NOT NULL,
    checkpoint_session_id BIGINT NOT NULL,
    group_user_id         BIGINT NOT NULL,
    impediment            TEXT,
    presence              INTEGER,
    stars                 INTEGER,
    comment               TEXT,
    CONSTRAINT pk_check_point PRIMARY KEY (id)
);

CREATE TABLE checkpoint_sessions
(
    id               BIGINT  NOT NULL,
    group_id         BIGINT  NOT NULL,
    group_user_id    BIGINT  NOT NULL,
    created_date     date,
    start_time       time WITHOUT TIME ZONE,
    duration_minutes INTEGER NOT NULL,
    name             VARCHAR(255),
    CONSTRAINT pk_checkpoint_sessions PRIMARY KEY (id)
);

ALTER TABLE check_point
    ADD CONSTRAINT FK_CHECK_POINT_ON_CHECKPOINT_SESSION FOREIGN KEY (checkpoint_session_id) REFERENCES checkpoint_sessions (id);
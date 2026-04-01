CREATE SEQUENCE IF NOT EXISTS groups_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE features
(
    key         VARCHAR(200) NOT NULL,
    description VARCHAR(255),
    CONSTRAINT pk_features PRIMARY KEY (key)
);

CREATE TABLE group_feature
(
    group_feature_key VARCHAR(200) NOT NULL,
    group_id          INTEGER      NOT NULL,
    CONSTRAINT pk_group_feature PRIMARY KEY (group_feature_key, group_id)
);

CREATE TABLE groups
(
    id                    INTEGER NOT NULL,
    name                  VARCHAR(255),
    background_preference INTEGER,
    icon_preference       INTEGER,
    CONSTRAINT pk_groups PRIMARY KEY (id)
);

ALTER TABLE group_feature
    ADD CONSTRAINT fk_grofea_on_group FOREIGN KEY (group_id) REFERENCES groups (id);

ALTER TABLE group_feature
    ADD CONSTRAINT fk_grofea_on_group_feature FOREIGN KEY (group_feature_key) REFERENCES features (key);
CREATE SEQUENCE IF NOT EXISTS groups_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE features
(
    key         VARCHAR(200) NOT NULL,
    description VARCHAR(255),
    CONSTRAINT pk_features PRIMARY KEY (key)
);

CREATE TABLE group_feature
(
    group_feature_key VARCHAR(200) NOT NULL,
    group_id          INTEGER      NOT NULL,
    CONSTRAINT pk_group_feature PRIMARY KEY (group_feature_key, group_id)
);

CREATE TABLE groups
(
    id                    INTEGER NOT NULL,
    name                  VARCHAR(255),
    background_preference INTEGER,
    icon_preference       INTEGER,
    CONSTRAINT pk_groups PRIMARY KEY (id)
);

ALTER TABLE group_feature
    ADD CONSTRAINT fk_grofea_on_group FOREIGN KEY (group_id) REFERENCES groups (id);

ALTER TABLE group_feature
    ADD CONSTRAINT fk_grofea_on_group_feature FOREIGN KEY (group_feature_key) REFERENCES features (key);
CREATE SEQUENCE IF NOT EXISTS groups_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE check_in
(
    user_id          INTEGER                     NOT NULL,
    group_id         INTEGER                     NOT NULL,
    date             DATE NOT NULL,
    obstacle_comment VARCHAR(255),
    presence         INTEGER,
    presence_comment VARCHAR(255),
    checkin_stars    INTEGER,
    checkin_comment  VARCHAR(255),
    checkup_stars    INTEGER,
    checkup_comment  VARCHAR(255),
    checkout_stars   INTEGER,
    checkout_comment VARCHAR(255),
    CONSTRAINT pk_checkin PRIMARY KEY (user_id, group_id, date)
);

CREATE TABLE features
(
    key         VARCHAR(200) NOT NULL,
    description VARCHAR(255),
    CONSTRAINT pk_features PRIMARY KEY (key)
);

CREATE TABLE group_feature
(
    group_feature_key VARCHAR(200) NOT NULL,
    group_id          INTEGER      NOT NULL,
    CONSTRAINT pk_group_feature PRIMARY KEY (group_feature_key, group_id)
);

CREATE TABLE groups
(
    id                    INTEGER NOT NULL,
    name                  VARCHAR(255),
    background_preference INTEGER,
    icon_preference       INTEGER,
    CONSTRAINT pk_groups PRIMARY KEY (id)
);

CREATE TABLE checkpoint_sessions
(
    checkpointId          INTEGER NOT NULL,
    userId                INTEGER NOT NULL,
    startdate             DATE NOT NULL,
    starttime             TIME NOT NULL,
    CONSTRAINT pk_groups PRIMARY KEY (checkpointId)
);

ALTER TABLE check_in
    ADD CONSTRAINT FK_CHECKIN_ON_GROUP FOREIGN KEY (group_id) REFERENCES groups (id);

ALTER TABLE group_feature
    ADD CONSTRAINT fk_grofea_on_group FOREIGN KEY (group_id) REFERENCES groups (id);

ALTER TABLE group_feature
    ADD CONSTRAINT fk_grofea_on_group_feature FOREIGN KEY (group_feature_key) REFERENCES features (key);
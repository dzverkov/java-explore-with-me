DROP TABLE IF EXISTS PUBLIC.EVENTS CASCADE;
DROP TABLE IF EXISTS PUBLIC.REQUESTS CASCADE;
DROP TABLE IF EXISTS PUBLIC.COMPILATIONS CASCADE;
DROP TABLE IF EXISTS PUBLIC.USERS;
DROP TABLE IF EXISTS PUBLIC.CATEGORIES;
DROP TABLE IF EXISTS PUBLIC.LOCATIONS;
DROP TABLE IF EXISTS PUBLIC.COMPILATION_EVENT;

-----------------------------------------------

CREATE TABLE PUBLIC.USERS
(
    ID    BIGINT GENERATED BY DEFAULT AS IDENTITY,
    EMAIL VARCHAR(50)  NOT NULL,
    NAME  VARCHAR(100) NOT NULL,
    CONSTRAINT USERS_PK
        PRIMARY KEY (ID),
    CONSTRAINT USERS_EMAIL_UK
        UNIQUE (EMAIL)
);

CREATE TABLE PUBLIC.CATEGORIES
(
    ID   BIGINT GENERATED BY DEFAULT AS IDENTITY,
    NAME VARCHAR(50) NOT NULL,
    CONSTRAINT CATEGORIES_PK
        PRIMARY KEY (ID),
    CONSTRAINT CATEGORIES_NAME_UK
        UNIQUE (NAME)
);

CREATE TABLE PUBLIC.LOCATIONS
(
    ID  BIGINT GENERATED BY DEFAULT AS IDENTITY,
    LAT FLOAT,
    LON FLOAT,
    CONSTRAINT LOCATIONS_PK
        PRIMARY KEY (ID)
);

CREATE TABLE PUBLIC.EVENTS
(
    ID                 BIGINT GENERATED BY DEFAULT AS IDENTITY,
    ANNOTATION         VARCHAR(1024),
    CATEGORY_ID        BIGINT,
    CREATED_ON         TIMESTAMP WITHOUT TIME ZONE,
    DESCRIPTION        VARCHAR(1024),
    EVENT_DATE         TIMESTAMP WITHOUT TIME ZONE,
    INITIATOR_ID       BIGINT,
    LOCATION_ID        BIGINT,
    PAID               BOOLEAN,
    PARTICIPANT_LIMIT  INTEGER,
    PUBLISHED_ON       TIMESTAMP WITHOUT TIME ZONE,
    REQUEST_MODERATION BOOLEAN,
    STATE              VARCHAR(50),
    TITLE              VARCHAR(255),
    CONSTRAINT EVENTS_PK
        PRIMARY KEY (ID),
    CONSTRAINT EVENTS_ON_CATEGORY_FK
        FOREIGN KEY (CATEGORY_ID) REFERENCES PUBLIC.CATEGORIES (ID),
    CONSTRAINT EVENTS_ON_INITIATOR_FK
        FOREIGN KEY (INITIATOR_ID) REFERENCES PUBLIC.USERS (ID),
    CONSTRAINT EVENTS_ON_LOCATION_FK
        FOREIGN KEY (LOCATION_ID) REFERENCES PUBLIC.LOCATIONS (ID)
);

CREATE TABLE PUBLIC.REQUESTS
(
    ID           BIGINT GENERATED BY DEFAULT AS IDENTITY,
    CREATED      TIMESTAMP WITHOUT TIME ZONE,
    EVENT_ID     BIGINT,
    REQUESTER_ID BIGINT,
    STATUS       VARCHAR(50),
    CONSTRAINT REQUEST_PK
        PRIMARY KEY (ID),
    CONSTRAINT REQUEST_ON_EVENT_FK
        FOREIGN KEY (EVENT_ID) REFERENCES PUBLIC.EVENTS (ID),
    CONSTRAINT REQUEST_ON_REQUESTER_FK
        FOREIGN KEY (REQUESTER_ID) REFERENCES PUBLIC.USERS (ID)
);

CREATE TABLE PUBLIC.COMPILATIONS
(
    ID     BIGINT GENERATED BY DEFAULT AS IDENTITY,
    PINNED BOOLEAN      NOT NULL,
    TITLE  VARCHAR(255) NOT NULL,
    CONSTRAINT COMPILATIONS_PK
        PRIMARY KEY (ID)
);

CREATE TABLE PUBLIC.COMPILATION_EVENT
(
    COMPILATION_ID BIGINT NOT NULL,
    EVENT_ID       BIGINT NOT NULL,
    CONSTRAINT COMPILATION_EVENT_PK
        PRIMARY KEY (COMPILATION_ID, EVENT_ID),
    CONSTRAINT EVENT_COMPILATION_FK
        FOREIGN KEY (COMPILATION_ID) REFERENCES PUBLIC.COMPILATIONS (ID),
    CONSTRAINT COMPILATION_EVENT_FK
        FOREIGN KEY (EVENT_ID) REFERENCES PUBLIC.EVENTS (ID)
);
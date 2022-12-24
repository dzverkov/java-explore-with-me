DROP TABLE PUBLIC.ENDPOINT_HIT;

CREATE TABLE PUBLIC.ENDPOINT_HIT(
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY,
    APP	CHARACTER VARYING(50) NOT NULL,
    URI	CHARACTER VARYING(255) NOT NULL,
    IP	CHARACTER VARYING(50) NOT NULL,
    CREATED	TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT ENDPOINT_HIT_PK
        PRIMARY KEY (ID)
);
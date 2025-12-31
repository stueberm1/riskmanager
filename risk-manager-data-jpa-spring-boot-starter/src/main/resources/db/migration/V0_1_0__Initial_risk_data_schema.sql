CREATE TABLE IF NOT EXIST risk_data (
    id                          BIGSERIAL,
    risk_id                     VARCHAR(30)     NOT NULL,
    severity                    VARCHAR(15)     NOT NULL,
    probability_of_occurence    VARCHAR(15)     NOT NULL,
    description                 VARCHAR(50)     NOT NULL,
    details                     TEXT,
    contingency_planning        TEXT,
    mitigation_strategy         TEXT
);
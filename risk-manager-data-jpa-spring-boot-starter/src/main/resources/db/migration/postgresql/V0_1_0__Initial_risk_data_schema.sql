CREATE TABLE IF NOT EXISTS risk_data (
    risk_id                     VARCHAR(30)     NOT NULL    PRIMARY KEY,
    severity                    VARCHAR(15)     NOT NULL,
    probability_of_occurence    VARCHAR(15)     NOT NULL,
    description                 VARCHAR(50)     NOT NULL,
    details                     TEXT,
    contingency_planning        TEXT,
    mitigation_strategy         TEXT
);
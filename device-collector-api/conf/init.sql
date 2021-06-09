\c db;
    CREATE TABLE IF NOT EXISTS stats (
        customer VARCHAR ( 50 ) NOT NULL,
        content VARCHAR ( 50 ) NOT NULL,
        dateTime TIMESTAMP NOT NULL,
        cdn INT NOT NULL ,
        p2p INT NOT NULL,
        sessions INT NOT NULL,
        CONSTRAINT customer_content PRIMARY KEY (customer,content)
    );
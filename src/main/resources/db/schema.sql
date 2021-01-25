CREATE TABLE users
  (
     id       BIGINT GENERATED BY DEFAULT AS IDENTITY,
     email_id VARCHAR(255),
     name     VARCHAR(255),
     PRIMARY KEY (id)
  );

CREATE TABLE vaccinaton_centres
  (
     id       INTEGER GENERATED BY DEFAULT AS IDENTITY,
     capacity BIGINT NOT NULL,
     name     VARCHAR(255),
     PRIMARY KEY (id)
  );

CREATE TABLE vaccines
  (
     id           INTEGER GENERATED BY DEFAULT AS IDENTITY,
     manufacturer VARCHAR(255),
     name         VARCHAR(255),
     PRIMARY KEY (id)
  );

CREATE TABLE time_slots
  (
     id         BIGINT GENERATED BY DEFAULT AS IDENTITY,
     capacity   BIGINT DEFAULT 0,
     DATE       DATE,
     end_time   TIME,
     start_time TIME,
     centre_id  INTEGER,
     PRIMARY KEY (id),
     FOREIGN KEY (centre_id) REFERENCES vaccinaton_centres
  );

CREATE TABLE vaccine_inventory
  (
     centre_id  INTEGER NOT NULL,
     vaccine_id INTEGER NOT NULL,
     PRIMARY KEY (centre_id, vaccine_id),
     FOREIGN KEY (vaccine_id) REFERENCES vaccines,
     FOREIGN KEY (centre_id) REFERENCES vaccinaton_centres
  );

CREATE TABLE appointments
  (
     slot_id       BIGINT NOT NULL,
     user_id       BIGINT NOT NULL,     
     is_vaccinated BOOLEAN DEFAULT false,
     payment_method VARCHAR(255),
     vaccine_id    INTEGER,
     PRIMARY KEY (slot_id, user_id),
     FOREIGN KEY (slot_id) REFERENCES time_slots,
     FOREIGN KEY (user_id) REFERENCES users,
     FOREIGN KEY (vaccine_id) REFERENCES vaccines
  ); 
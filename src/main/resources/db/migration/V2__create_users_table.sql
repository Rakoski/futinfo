CREATE TABLE users (
   id SERIAL PRIMARY KEY,
   name VARCHAR(255) NOT NULL,
   email VARCHAR(255) NOT NULL,
   cpf VARCHAR(255),
   password VARCHAR(255) NOT NULL,
   bio VARCHAR(500),
   phone VARCHAR(255),
   avatar_url VARCHAR(255),
   email_confirmed_at TIMESTAMP,
   external_customer_id VARCHAR(255),
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP,
   deleted_at TIMESTAMP,
   address_id INTEGER,

   CONSTRAINT fk_address_id FOREIGN KEY (address_id) REFERENCES addresses(id)
);

CREATE UNIQUE INDEX index_unique_email
    ON users (email)
    WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX index_unique_cpf
    ON users (cpf)
    WHERE cpf IS NOT NULL
    AND deleted_at IS NULL;
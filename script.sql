show databases;

CREATE TABLE property (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          owner_id BIGINT NOT NULL,
                          name VARCHAR(100),
                          description TEXT,
                          property_type VARCHAR(50),
                          price DECIMAL(10, 2),
                          available BOOLEAN
);

INSERT INTO property (owner_id, name, description, property_type, price, available)
VALUES
    (1, 'Apto 101', 'Amplio apartamento ubicado en el primer piso - Nuevo Paraiso', 'Apartamento', 500.000, TRUE),
    (2, 'Apto 102', 'Amplio apartamento ubicado en el primer piso - Nuevo Paraiso', 'Apartamento', 500.000, TRUE),
    (3, 'Apto 201', 'Amplio apartamento ubicado en el segundo piso - Nuevo Paraiso', 'Apartamento', 400.000, TRUE);

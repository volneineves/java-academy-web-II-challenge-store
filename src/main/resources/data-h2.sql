MERGE INTO departments (id, name, description, created_at, updated_at) KEY(id) VALUES
    ('96c53c2f-8a85-46ad-9065-55a5b1c1b9e4', 'ADM', 'Administration', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('9c6a5de5-1049-4cc2-8671-281bbfab8b2a', 'SALES', 'Sales Department', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

MERGE INTO adresses (id, street, number, city, state, zip, complement)
    KEY(id) VALUES
    ('a1d1a1f1-8b3d-5c4a-8a1c-e59a47fc11da', 'Rua das Flores', '100', 'São Paulo', 'SP', '01310-000', 'Apto 202'),
    ('a2d2a2f2-8b4d-6c5a-8c2d-e69b48fc12db', 'Avenida Paulista', '2001', 'São Paulo', 'SP', '01311-300', 'Bloco B'),
    ('a3d3a3f3-8b5d-6d5a-8d3d-e79c49fc13dc', 'Rua Oscar Freire', '400', 'São Paulo', 'SP', '01426-001', 'Sala 3');

MERGE INTO users (id, is_active, name, email, password, birthdate, address_id, created_at, updated_at, user_type)
    KEY(id) VALUES
    ('e1b1d1f1-7a3d-5c4a-8bfc-e59a47fc65db', true, 'Admin Name', 'admin@example.com', '$2a$12$1/26/WVROEf8qnWNuOAUteQ9vQgBrd2ob0b4S99S7lAzTyvclvaKy', '1990-01-01', 'a1d1a1f1-8b3d-5c4a-8a1c-e59a47fc11da', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'EMPLOYEE'),
    ('e2b2d2f2-7b4d-6c5a-8cfc-e69b48fc76dc', true, 'Manager Name', 'volneinevesfh@gmail.com', '$2a$12$1/26/WVROEf8qnWNuOAUteQ9vQgBrd2ob0b4S99S7lAzTyvclvaKy', '1992-01-01', 'a2d2a2f2-8b4d-6c5a-8c2d-e69b48fc12db', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'EMPLOYEE'),
    ('c1d1e1f1-8a4d-5c6a-8b7c-d9a58bf87edc', true, 'Volnei', 'volneinevesfilho@hotmail.com', '$2a$12$1/26/WVROEf8qnWNuOAUteQ9vQgBrd2ob0b4S99S7lAzTyvclvaKy', '1985-01-01', 'a3d3a3f3-8b5d-6d5a-8d3d-e79c49fc13dc', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'CUSTOMER');

MERGE INTO employees (id, salary, role, department_id) KEY(id) VALUES
    ('e1b1d1f1-7a3d-5c4a-8bfc-e59a47fc65db', 5000.00, 'ADMIN', '96c53c2f-8a85-46ad-9065-55a5b1c1b9e4'),
    ('e2b2d2f2-7b4d-6c5a-8cfc-e69b48fc76dc', 6000.00, 'MANAGER', '9c6a5de5-1049-4cc2-8671-281bbfab8b2a');

MERGE INTO customers (id, cpf) KEY(id) VALUES
    ('c1d1e1f1-8a4d-5c6a-8b7c-d9a58bf87edc', '082.654.580-70'); -- CPF: FAKE

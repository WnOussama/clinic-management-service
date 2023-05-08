INSERT INTO CM_USERS(id, civility, first_name, last_name, email, phone_number, password, creation_date, modified_date)
VALUES (1, 'MR', 'John', 'Doe', 'john.doe@mail.com', '01122334455', '$2a$10$PRlKa/dbKFsBT4IuIbCPKOvOx7GZDjLDi0uLCe9Mgc13QO8OkF37W', '2023-02-21 10:50:54.000000', '2023-02-21 10:50:54.000000');
INSERT INTO CM_PATIENTS(id, user_id)
VALUES (1, 1);
INSERT INTO CM_USERS(id, civility, first_name, last_name, email, phone_number, password, creation_date, modified_date)
VALUES (2, 'MRS', 'Jessie', 'Doe', 'jessie.doe@mail.com', '01122334455', '$2a$10$PRlKa/dbKFsBT4IuIbCPKOvOx7GZDjLDi0uLCe9Mgc13QO8OkF37W', '2023-04-06 03:16:54.000000', '2023-04-06 03:16:54.000000');
INSERT INTO CM_DOCTORS(id, user_id, rule_id, speciality_id, address)
VALUES (1, 2, 1, 1, '23 Rue des Petits Champs, 75001 Paris, France');
INSERT INTO CM_USERS(id, civility, first_name, last_name, email, phone_number, password, creation_date, modified_date)
VALUES (3, 'MR', 'Ramsey', 'Foden', 'ramsey.foden@mail.com', '01122334455', '$2a$10$PRlKa/dbKFsBT4IuIbCPKOvOx7GZDjLDi0uLCe9Mgc13QO8OkF37W', '2023-04-23 20:55:54.000000', '2023-04-23 20:55:54.000000');
INSERT INTO CM_DOCTORS(id, user_id, rule_id, speciality_id, address)
VALUES (2, 3, 1, 51, '05 Rue Pierre Soulat, 78260 Acheres, France');
INSERT INTO CM_AVAILABILITIES(id, doctor_id, start_date, end_date, creation_date, modified_date)
VALUES (1, 1, '2023-04-24 09:00:00.000000', '2023-04-24 18:00:00.000000', '2023-04-23 20:55:54.000000', '2023-04-23 20:55:54.000000');
INSERT INTO CM_APPOINTMENTS(id, patient_id, availability_id, appointment_date, creation_date, modified_date)
VALUES (1, 1, 1, '2023-04-24 09:00:00.000000', '2023-04-23 20:55:54.000000', '2023-04-23 20:55:54.000000');
INSERT INTO CM_SPECIALITIES(id, name, appointment_duration, appointment_fee)
 VALUES (1, 'Médecin généraliste', 15, 50),
  (51, 'Orthopédiste', 30, 60),
  (101, 'Ophtalmologue', 30, 60),
  (151, 'Pédiatre', 45, 80),
  (201, 'Dentiste', 60, 100);

INSERT INTO CM_RULES(id, start_hour, end_hour, start_break_hour, end_break_hour)
 VALUES (1, 9, 18, 12, 14);
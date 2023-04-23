INSERT INTO CM_SPECIALITIES(id, name, appointment_duration)
 VALUES (NEXTVAL('CM_SPECIALITIES_SEQ'), 'Médecin généraliste', 15),
  (NEXTVAL('CM_SPECIALITIES_SEQ'), 'Orthopédiste', 30),
  (NEXTVAL('CM_SPECIALITIES_SEQ'), 'Ophtalmologue', 30),
  (NEXTVAL('CM_SPECIALITIES_SEQ'), 'Pédiatre', 45),
  (NEXTVAL('CM_SPECIALITIES_SEQ'), 'Dentiste', 60);

INSERT INTO CM_RULES(id, start_hour, end_hour, start_break_hour, end_break_hour)
 VALUES (1, 9, 18, 12, 14);
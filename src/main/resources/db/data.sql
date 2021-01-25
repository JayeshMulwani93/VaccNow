INSERT INTO vaccinaton_centres(id, capacity, name) VALUES ('1', '2', 'Dr.D.Y.Patil Hospital');
INSERT INTO vaccinaton_centres(id, capacity, name) VALUES ('2', '3', 'YCM');

INSERT INTO vaccines(id, manufacturer, name) VALUES ('1', 'Serum Institute', 'Covishield');
INSERT INTO vaccines(id, manufacturer, name) VALUES ('2', 'Bharat Biotech', 'Covaxin');

INSERT INTO vaccine_inventory(centre_id, vaccine_id) VALUES ('1','1');
INSERT INTO vaccine_inventory(centre_id, vaccine_id) VALUES ('1','2');
INSERT INTO vaccine_inventory(centre_id, vaccine_id) VALUES ('2','1');
INSERT INTO vaccine_inventory(centre_id, vaccine_id) VALUES ('2','2');

INSERT INTO users(id, email_id, name) VALUES (1,'jmm@gmail.com','J M');
INSERT INTO users(id, email_id, name) VALUES (2,'ab@gmail.com','A B');
INSERT INTO users(id, email_id, name) VALUES (3,'cd@gmail.com','C D');
INSERT INTO users(id, email_id, name) VALUES (4,'ef@gmail.com','E F');
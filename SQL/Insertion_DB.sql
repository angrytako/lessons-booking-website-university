begin;

INSERT INTO `corso`(`materia`) VALUES ('RetiI');
INSERT INTO `corso`(`materia`) VALUES ('Fisica');
INSERT INTO `corso`(`materia`) VALUES ('Analisi');
INSERT INTO `corso`(`materia`) VALUES ('Inglese');
INSERT INTO `corso`(`materia`) VALUES ('Sicurezza');
INSERT INTO `corso`(`materia`) VALUES ('Basi_di_dati');
INSERT INTO `corso`(`materia`) VALUES ('ProgrammazioneI');
INSERT INTO `corso`(`materia`) VALUES ('ProgrammazioneII');
INSERT INTO `corso`(`materia`) VALUES ('ProgrammazioneIII');
INSERT INTO `corso`(`materia`) VALUES ('Architettura_degli_elaboratori');
INSERT INTO `corso`(`materia`) VALUES ('Interazione_uomo_macchina_e_tecnologie_web');

INSERT INTO `docente`(nome,cognome) VALUES ('Viviana','Bono');
INSERT INTO `docente`(nome,cognome) VALUES ('Ciro','Cattuto');
INSERT INTO `docente`(nome,cognome) VALUES ('Felice','Cardone');
INSERT INTO `docente`(nome,cognome) VALUES ('Marco','Aldinucci');
INSERT INTO `docente`(nome,cognome) VALUES ('Giancarlo','Ruffo');
INSERT INTO `docente`(nome,cognome) VALUES ('Roberta','Sirovich');
INSERT INTO `docente`(nome,cognome) VALUES ('Liliana','Ardissono');
INSERT INTO `docente`(nome,cognome) VALUES ('Alessandro','Mazzei');
INSERT INTO `docente`(nome,cognome) VALUES ('Rossano','Schifanella');

INSERT INTO `insegnamento`(corso,docente) VALUES ('Sicurezza',5);
INSERT INTO `insegnamento`(corso,docente) VALUES ('Basi_di_dati',2);
INSERT INTO `insegnamento`(corso,docente) VALUES ('ProgrammazioneI',3);
INSERT INTO `insegnamento`(corso,docente) VALUES ('ProgrammazioneI',5);
INSERT INTO `insegnamento`(corso,docente) VALUES ('ProgrammazioneI',8);
INSERT INTO `insegnamento`(corso,docente) VALUES ('ProgrammazioneII',2);
INSERT INTO `insegnamento`(corso,docente) VALUES ('ProgrammazioneII',9);
INSERT INTO `insegnamento`(corso,docente) VALUES ('ProgrammazioneIII',7);
INSERT INTO `insegnamento`(corso,docente) VALUES ('ProgrammazioneIII',9);
INSERT INTO `insegnamento`(corso,docente) VALUES ('Architettura_degli_elaboratori',2);
INSERT INTO `insegnamento`(corso,docente) VALUES ('Architettura_degli_elaboratori',4);

INSERT INTO `utente`(username,password,ruolo) VALUES ('ToneTuga','superpwd','amministratore');
INSERT INTO `utente`(username,password,ruolo) VALUES ('Lorenzo','loripwd','cliente');
INSERT INTO `utente`(username,password,ruolo) VALUES ('Anna','ninapwd','cliente');

INSERT INTO `prenotazione`(corso,docente,utente,stato,giorno,orario)
VALUES ('Sicurezza',5,'ToneTuga','attiva',1,1);
INSERT INTO `prenotazione`(corso,docente,utente,stato,giorno,orario)
VALUES ('Sicurezza',5,'ToneTuga','attiva',3,1);
INSERT INTO `prenotazione`(corso,docente,utente,stato,giorno,orario)
VALUES ('Sicurezza',5,'ToneTuga','attiva',1,0);
INSERT INTO `prenotazione`(corso,docente,utente,stato,giorno,orario)
VALUES ('Basi_di_dati',2,'Lorenzo','effettuata',0,0);
INSERT INTO `prenotazione`(corso,docente,utente,stato,giorno,orario)
VALUES ('Architettura_degli_elaboratori',2,'Lorenzo','cancellata',0,0);
INSERT INTO `prenotazione`(corso,docente,utente,stato,giorno,orario)
VALUES ('Sicurezza',5,'Anna','cancellata',1,0);
INSERT INTO `prenotazione`(corso,docente,utente,stato,giorno,orario)
VALUES ('ProgrammazioneIII',7,'Anna','cancellata',1,0);

commit;

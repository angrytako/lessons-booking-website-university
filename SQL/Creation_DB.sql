begin;

CREATE TABLE corso (
    materia varchar(32) NOT NULL,
    rimosso boolean NOT NULL DEFAULT FALSE,
    PRIMARY KEY (materia)
     );

CREATE TABLE docente (
    id int NOT NULL AUTO_INCREMENT,
    nome varchar(32) NOT NULL,
    cognome varchar(32) NOT NULL,
    rimosso boolean NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
    );

CREATE TABLE insegnamento (
    corso varchar(32) NOT NULL,
    docente int NOT NULL,
    rimosso boolean NOT NULL DEFAULT FALSE,
    FOREIGN KEY (corso) REFERENCES corso(materia),
    FOREIGN KEY (docente) REFERENCES docente(id),
    PRIMARY KEY (corso,docente)
     );

CREATE TABLE utente (
    username varchar(32) NOT NULL,
    password varchar(32) NOT NULL,
    ruolo ENUM('cliente','amministratore') NOT NULL,
    PRIMARY KEY (username)
     );

CREATE TABLE prenotazione (
    corso varchar(32) NOT NULL,
    docente int NOT NULL,
    utente varchar(32) NOT NULL,
    stato ENUM('attiva','effettuata','cancellata') DEFAULT 'attiva',
    giorno int NOT NULL,
    orario int NOT NULL,
    check(giorno>=0 and giorno <=4),
    check(orario>=0 and orario <=3),
    FOREIGN KEY (corso,docente) REFERENCES insegnamento(corso,docente),
    FOREIGN KEY (utente) REFERENCES utente(username),
    PRIMARY KEY (corso,utente,giorno,orario)
     );

commit;

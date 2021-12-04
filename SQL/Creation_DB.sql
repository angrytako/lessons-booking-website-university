begin;

CREATE TABLE corso (
    materia VARCHAR(32) NOT NULL,
    PRIMARY KEY (materia)
     );

CREATE TABLE docente (
    id int NOT NULL AUTO_INCREMENT,
    nome VARCHAR(32) NOT NULL,
    cognome VARCHAR(32) NOT NULL,
    PRIMARY KEY (id)
    );

CREATE TABLE insegnamento (
    docente int NOT NULL,
    corso VARCHAR(32) NOT NULL,
    FOREIGN KEY (docente) REFERENCES docente(id),
    FOREIGN KEY (corso) REFERENCES corso(materia),
    PRIMARY KEY (docente,corso)
     );


CREATE TABLE utente (
    username VARCHAR(32) NOT NULL,
    password VARCHAR(32) NOT NULL,
    ruolo ENUM('cliente','amministratore') not null ,
    PRIMARY KEY (username)
     );



CREATE TABLE prenotazione (
    corso VARCHAR(32) NOT NULL,
    docente int NOT NULL,
    utente VARCHAR(32) NOT NULL,
    stato ENUM('attiva','effettuata','cancellata'),
    giorno int not null,
    orario int not null,
    check(giorno>=0 and giorno <=4),
    check(orario>=0 and orario <=3),
    FOREIGN KEY (corso) REFERENCES insegnamento(corso),
    FOREIGN KEY (docente) REFERENCES insegnamento(docente),
    FOREIGN KEY (utente) REFERENCES utente(username),
    PRIMARY KEY (corso,utente,giorno,orario)
     );

commit;

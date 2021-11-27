Create table ripetizioni;
use ripetizioni;


CREATE TABLE corso ( 
    titolo VARCHAR(32) NOT NULL,
    PRIMARY KEY (titolo)
     ) ENGINE = InnoDB;

CREATE TABLE Professore ( 
    nome VARCHAR(32) NOT NULL,
    cognome VARCHAR(32) NOT NULL,
    id int NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id)
    ) ENGINE = InnoDB;

CREATE TABLE Insegnamenti (
    Professore int NOT NULL,
    corso VARCHAR(32) NOT NULL,
    FOREIGN KEY (Professore) REFERENCES Professore(id),
    FOREIGN KEY (corso) REFERENCES corso(titolo),
    PRIMARY KEY (Professore,corso)
     ) ENGINE = InnoDB;


CREATE TABLE utente ( 
    username VARCHAR(32) NOT NULL,
    password VARCHAR(32) NOT NULL,
    Ruolo ENUM('cliente','amministratore') not null ,
    PRIMARY KEY (username)
     ) ENGINE = InnoDB;



CREATE TABLE ripetizioni ( 
    Professore int NOT NULL,
    corso VARCHAR(32) NOT NULL,
    utente VARCHAR(32) NOT NULL,
    stato ENUM('attiva','effettuata','cancellata'),
    Giorno int  not null,
    Orario int not null,
    check(giorno>=0 and giorno <=4),
    check(Orario>=0 and Orario <=2),
    FOREIGN KEY (Professore) REFERENCES Professore(id),
    FOREIGN KEY (utente) REFERENCES utente(username),
    FOREIGN KEY (corso) REFERENCES corso(titolo),
    PRIMARY KEY (utente,giorno,orario)
     ) ENGINE = InnoDB;








    INSERT INTO `corso`(`titolo`) VALUES ('progI');
    INSERT INTO `corso`(`titolo`) VALUES ('analisi');
    INSERT INTO `corso`(`titolo`) VALUES ('Database');
    INSERT INTO `corso`(`titolo`) VALUES ('Inglese');
    INSERT INTO `corso`(`titolo`) VALUES ('sicurezza');
    INSERT INTO `corso`(`titolo`) VALUES ('progIII');

    INSERT INTO `Professore`(nome,cognome) VALUES ('roberta','sirovich');
    INSERT INTO `Professore`(nome,cognome) VALUES ('Viviana','Bono');
    INSERT INTO `Professore`(nome,cognome) VALUES ('Liliana','Ardissono');
    INSERT INTO `Professore`(nome,cognome) VALUES ('Rossano','Schifanella');

    INSERT INTO `Insegnamenti`(Professore,corso) VALUES (1,'analisi');
    INSERT INTO `Insegnamenti`(Professore,corso) VALUES (2,'Database');
    INSERT INTO `Insegnamenti`(Professore,corso) VALUES (3,'progI');
    INSERT INTO `Insegnamenti`(Professore,corso) VALUES (3,'progIII');


    INSERT INTO `utente`(username,password,ruolo) VALUES ('ToneTuga',MD5('password'),'cliente');
    INSERT INTO `utente`(username,password,ruolo) VALUES ('Lorenzo',MD5('pupu'),'cliente');
    INSERT INTO `utente`(username,password,ruolo) VALUES ('Anna',MD5('cacca'),'cliente');
    INSERT INTO `utente`(username,password,ruolo) VALUES ('Admin',MD5('superpwd'),'amministratore');


    INSERT INTO `ripetizioni`(Professore,corso,utente,stato ,Giorno,Orario) 
    VALUES (1,'analisi','ToneTuga','attiva' ,1,1);    
    INSERT INTO `ripetizioni`(Professore,corso,utente,stato ,Giorno,Orario) 
    VALUES (1,'analisi','ToneTuga','attiva' ,3,1);    
    INSERT INTO `ripetizioni`(Professore,corso,utente,stato ,Giorno,Orario) 
    VALUES (1,'analisi','ToneTuga','attiva' ,1,0);
    INSERT INTO `ripetizioni`(Professore,corso,utente,stato ,Giorno,Orario) 
    VALUES (2,'database','lorenzo','effettuata' ,0,0);
    INSERT INTO `ripetizioni`(Professore,corso,utente,stato ,Giorno,Orario) 
    VALUES (3,'analisi','anna','cancellata' ,1,0);


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







    INSERT INTO `corso`(`titolo`) VALUES ('aaa');

    DELETE FROM corso WHERE titolo='aaa';

    INSERT INTO `utente`(account,password,ruolo) VALUES ('ToneTuga','1234','cliente');
    INSERT INTO `Professore`(nome,cognome,utente) VALUES ('enrico','chiesa','ToneTuga');



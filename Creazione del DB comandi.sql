
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
    FOREIGN KEY (Professore) REFERENCES Professore(utente),
    FOREIGN KEY (corso) REFERENCES corso(titolo),
    PRIMARY KEY (Professore,corso)
     ) ENGINE = InnoDB;

CREATE TABLE utente ( 
    account VARCHAR(32) NOT NULL,
    password VARCHAR(32) NOT NULL,
    Ruolo VARCHAR(32) NOT NULL,
    PRIMARY KEY (account)
     ) ENGINE = InnoDB;







CREATE TABLE ripetizioni ( 
    Professore VARCHAR(32) NOT NULL,
    corso VARCHAR(32) NOT NULL,
    utente VARCHAR(32) NOT NULL,
    data DATE NOT NULL,
    FOREIGN KEY (Professore) REFERENCES Professore(utente),
    FOREIGN KEY (utente) REFERENCES utente(account),
    FOREIGN KEY (corso) REFERENCES corso(titolo),
    PRIMARY KEY (Professore,corso,utente)
     ) ENGINE = InnoDB;







    INSERT INTO `corso`(`titolo`) VALUES ('aaa');

    DELETE FROM corso WHERE titolo='aaa';

    INSERT INTO `utente`(account,password,ruolo) VALUES ('ToneTuga','1234','cliente');
    INSERT INTO `Professore`(nome,cognome,utente) VALUES ('enrico','chiesa','ToneTuga');


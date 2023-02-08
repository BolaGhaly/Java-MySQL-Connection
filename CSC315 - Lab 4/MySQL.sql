CREATE DATABASE feedback;
USE feedback;

CREATE USER 'sqluser'@'localhost' IDENTIFIED BY 'sqlUserpw1.';

grant all privileges on feedback.* to 'sqluser'@'localhost';

CREATE TABLE comments (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(30) NOT NULL,
    email VARCHAR(30) NOT NULL,
    comment VARCHAR(400) NOT NULL,
    `timestamp` DATETIME NOT NULL,
    PRIMARY KEY (ID)
);
-- ------------------------------ Midterm: Question 2 ------------------------------
CREATE DATABASE flubber;
USE flubber;

-- Entities
CREATE TABLE professor (
	Professor_ID Integer AUTO_INCREMENT PRIMARY KEY,
	Professor_Name varchar(255),
	Field varchar(255),
	College varchar(255),
	PhD_Date Date
);

CREATE TABLE flub (
	Flub_ID Integer AUTO_INCREMENT PRIMARY KEY,
	Content varchar(255),
	Purpose varchar(255),
	Moment varchar(255),
	Inventor Integer,
	FOREIGN KEY (Inventor) REFERENCES Professor (Professor_ID)
);

CREATE TABLE bounce (
	Bounce_ID Integer AUTO_INCREMENT PRIMARY KEY,
	Professor_ID Integer,
	Flub_ID Integer,
	FOREIGN KEY (Professor_ID) REFERENCES Professor (Professor_ID),
	FOREIGN KEY (Flub_ID) REFERENCES Flub (Flub_ID)
);

CREATE TABLE citation (
	Citation_ID Integer AUTO_INCREMENT PRIMARY KEY,
	Professor_ID Integer,
	Flub_ID Integer,
	FOREIGN KEY (Professor_ID) REFERENCES Professor (Professor_ID),
	FOREIGN KEY (Flub_ID) REFERENCES Flub (Flub_ID)
);

-- Relationships
CREATE TABLE Professor_Creates_Flub (
	Professor_ID Integer,
	Flub_ID Integer,
	FOREIGN KEY (Professor_ID) REFERENCES Professor (Professor_ID),
	FOREIGN KEY (Flub_ID) REFERENCES Flub (Flub_ID)
);

CREATE TABLE Professor_Posts_Bounce (
	Professor_ID Integer,
	Bounce_ID Integer,
	FOREIGN KEY (Professor_ID) REFERENCES Professor (Professor_ID),
	FOREIGN KEY (Bounce_ID) REFERENCES Bounce (Bounce_ID)
);

CREATE TABLE Bounce_Shares_Flub (
	Bounce_ID Integer,
	Flub_ID Integer,
	FOREIGN KEY (Bounce_ID) REFERENCES Bounce (Bounce_ID),
	FOREIGN KEY (Flub_ID) REFERENCES Flub (Flub_ID)
);

CREATE TABLE Professor_Can_Be_Colleague (
	Professor_ID Integer,
	Colleague_ID Integer,
	FOREIGN KEY (Professor_ID) REFERENCES Professor (Professor_ID),
	FOREIGN KEY (Colleague_ID) REFERENCES Professor (Professor_ID)
);

CREATE TABLE Professor_Cites_Citation (
	Professor_ID Integer,
	Citation_ID Integer,
	FOREIGN KEY (Professor_ID) REFERENCES Professor (Professor_ID),
	FOREIGN KEY (Citation_ID) REFERENCES Citation (Citation_ID)
);

CREATE TABLE Citation_Refers_To_Flub (
	Citation_ID Integer,
	Flub_ID Integer,
	FOREIGN KEY (Citation_ID) REFERENCES Citation (Citation_ID),
	FOREIGN KEY (Flub_ID) REFERENCES Flub (Flub_ID)
);

-- ------------------------------ Midterm: Question 3 ------------------------------
-- A. Adding a new Professor
INSERT INTO professor (Professor_Name,Field,College) VALUES ('Luigi Kapaj', 'Computer Science', 'CUNY College of Staten Island');

-- B. Changing a specific Professor's Professor_Name
UPDATE professor P SET P.Professor_Name = 'Ping Shi' WHERE P.Professor_ID = 1;

-- C. Removing a Flub by ID
DELETE FROM flub F WHERE F.Flub_ID = 1;

-- D. Show a portfolio of the Flubs by a Professor in reverse chronological order
SELECT P.Professor_ID, P.Professor_Name, F.Flub_ID, F.Content, F.Purpose, F.Moment, F.Inventor 
FROM flub F
INNER JOIN Professor_Creates_Flub PCF
ON PCF.Professor_ID = 1 AND PCF.Flub_ID = F.Flub_ID
INNER JOIN professor P
ON PCF.Professor_ID = P.Professor_ID order by F.Flub_ID desc;
-- OR
-- SELECT Flub_ID FROM Professor_Creates_Flub WHERE Professor_ID = 1 order by Flub_ID desc;

-- E. Show how many Bounces and how many Citations a Flub of particular ID has
SELECT 
(SELECT COUNT(B.Flub_ID) FROM bounce B WHERE B.Flub_ID = 4) AS Number_of_Bounces,
(SELECT COUNT(C.Flub_ID) FROM citation C WHERE C.Flub_ID = 4) AS Number_of_Citations;

-- F. Show a portfolio of all Flubs and Bounces (the Flubs bounced) by all of a Professor's Colleagues in reverse chronological order
SELECT PCBC.Professor_ID, PCBC.Colleague_ID, F.Flub_ID AS 'Professor\'s Colleague Flub_ID' 
FROM Professor_Can_Be_Colleague PCBC
INNER JOIN flub F
ON PCBC.Professor_ID = 1 AND F.Inventor = PCBC.Colleague_ID ORDER BY Colleague_ID DESC, F.Flub_ID DESC;

SELECT PCBC.Professor_ID, PCBC.Colleague_ID, B.Bounce_ID AS 'Professor\'s Colleague Bounce_ID'
FROM Professor_Can_Be_Colleague PCBC
INNER JOIN bounce B
ON PCBC.Professor_ID = 1 AND B.Professor_ID = PCBC.Colleague_ID ORDER BY Colleague_ID DESC, B.Bounce_ID DESC;

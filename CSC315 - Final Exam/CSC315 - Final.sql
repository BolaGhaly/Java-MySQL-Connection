CREATE DATABASE CSC315Final2022;
USE CSC315Final2022;


CREATE TABLE Genre (
	gid INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	gname CHAR(20) NOT NULL
);

INSERT INTO Genre (gname) VALUES ('Rock'), ('Jazz'), ('Country'), ('Classical'), ('Throat Singing');

CREATE TABLE Sub_Genre (
	sgid INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	gname CHAR(20) NOT NULL,
	sgname CHAR(20) NOT NULL
);

INSERT INTO Sub_Genre (gname, sgname) VALUES ('Rock', 'Blues'), ('Rock', 'Classic Rock'), ('Rock', 'Power Metal'), ('Rock', 'Thrash Metal'), ('Rock', 'Death Metal'), ('Rock', 'Folk Metal');
INSERT INTO Sub_Genre (gname, sgname) VALUES ('Jazz', 'Swing'), ('Jazz', 'Smooth Jazz'), ('Jazz', 'Bossa Nova'), ('Jazz', 'Ragtime');
INSERT INTO Sub_Genre (gname, sgname) VALUES ('Country', 'Bluegrass'), ('Country', 'Country and Western'), ('Country', 'Jug Band');
INSERT INTO Sub_Genre (gname, sgname) VALUES ('Classical', 'Chamber Music'), ('Classical', 'Opera'), ('Classical', 'Orchestral');
INSERT INTO Sub_Genre (gname, sgname) VALUES ('Throat Singing', 'Khoomii'), ('Throat Singing', 'Kargyraa'), ('Throat Singing', 'Khamryn');

CREATE TABLE Region (
	rid INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	rname CHAR(20) NOT NULL
);

INSERT INTO Region (rname) VALUES ('Central Asia'), ('Europe'), ('North America'), ('South America');

CREATE TABLE Country (
	rid INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	rname CHAR(20) NOT NULL,
	cname CHAR(20) NOT NULL
);

INSERT INTO Country (rname, cname) VALUES ('Central Asia', 'Mongolia'), ('Central Asia', 'Tibet'), ('Central Asia', 'Tuva');
INSERT INTO Country (rname, cname) VALUES ('North America', 'Canada'), ('North America', 'United States'), ('North America', 'Mexico');
INSERT INTO Country (rname, cname) VALUES ('South America', 'Brazil'), ('South America', 'Argentina');
INSERT INTO Country (rname, cname) VALUES ('Europe', 'Norway'), ('Europe', 'Austria'), ('Europe', 'England'), ('Europe', 'Russia'), ('Europe', 'Portugal'), ('Europe', 'France'), ('Europe', 'Croatia');

CREATE TABLE Bands (
	bid INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	bname CHAR(20) NOT NULL
);

INSERT INTO Bands (bname) VALUES ('Seputura'), ('Death'), ('Muddy Waters'), ('Led Zeppelin'), ('The Guess Who');
INSERT INTO Bands (bname) VALUES ('The Hu'), ('Huun-Huur-Tu'), ('Paul Pena'), ('Battuvshin'), ('Sade');
INSERT INTO Bands (bname) VALUES ('Mozart'), ('Tchaikovsky'), ('Twisted Sister'), ('Testament'), ('Tengger Cavalry');

CREATE TABLE Band_Origins (
	boid INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	bname CHAR(20) NOT NULL,
	cname CHAR(20) NOT NULL
);

INSERT INTO Band_Origins (bname, cname) VALUES ('Seputura', 'Brazil');
INSERT INTO Band_Origins (bname, cname) VALUES ('Death', 'United States');
INSERT INTO Band_Origins (bname, cname) VALUES ('Muddy Waters', 'United States');
INSERT INTO Band_Origins (bname, cname) VALUES ('Led Zeppelin', 'England');
INSERT INTO Band_Origins (bname, cname) VALUES ('The Guess Who', 'Canada');
INSERT INTO Band_Origins (bname, cname) VALUES ('The Hu', 'Mongolia');
INSERT INTO Band_Origins (bname, cname) VALUES ('Huun-Huur-Tu', 'Tuva');
INSERT INTO Band_Origins (bname, cname) VALUES ('Paul Pena', 'United States');
INSERT INTO Band_Origins (bname, cname) VALUES ('Battuvshin', 'Mongolia');
INSERT INTO Band_Origins (bname, cname) VALUES ('Sade', 'England');
INSERT INTO Band_Origins (bname, cname) VALUES ('Mozart', 'Austria');
INSERT INTO Band_Origins (bname, cname) VALUES ('Tchaikovsky', 'Russia');
INSERT INTO Band_Origins (bname, cname) VALUES ('Twisted Sister', 'United States');
INSERT INTO Band_Origins (bname, cname) VALUES ('Testament', 'United States');
INSERT INTO Band_Origins (bname, cname) VALUES ('Tengger Cavalry', 'United States');

CREATE TABLE Band_Styles (
	bsid INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	bname CHAR(20) NOT NULL,
	sgname CHAR(20) NOT NULL
);

INSERT INTO Band_Styles (bname, sgname) VALUES ('Seputura', 'Death Metal');
INSERT INTO Band_Styles (bname, sgname) VALUES ('Seputura', 'Thrash Metal');
INSERT INTO Band_Styles (bname, sgname) VALUES ('Death', 'Death Metal');
INSERT INTO Band_Styles (bname, sgname) VALUES ('Muddy Waters', 'Blues');
INSERT INTO Band_Styles (bname, sgname) VALUES ('Led Zeppelin', 'Classic Rock');
INSERT INTO Band_Styles (bname, sgname) VALUES ('The Guess Who', 'Classic Rock');
INSERT INTO Band_Styles (bname, sgname) VALUES ('The Hu', 'Folk Metal');
INSERT INTO Band_Styles (bname, sgname) VALUES ('The Hu', 'Khoomii');
INSERT INTO Band_Styles (bname, sgname) VALUES ('Huun-Huur-Tu', 'Khoomii');
INSERT INTO Band_Styles (bname, sgname) VALUES ('Huun-Huur-Tu', 'Kargyraa');
INSERT INTO Band_Styles (bname, sgname) VALUES ('Paul Pena', 'Blues');
INSERT INTO Band_Styles (bname, sgname) VALUES ('Paul Pena', 'Kargyraa');
INSERT INTO Band_Styles (bname, sgname) VALUES ('Battuvshin', 'Khoomii');
INSERT INTO Band_Styles (bname, sgname) VALUES ('Battuvshin', 'Smooth Jazz');
INSERT INTO Band_Styles (bname, sgname) VALUES ('Sade', 'Smooth Jazz');
INSERT INTO Band_Styles (bname, sgname) VALUES ('Mozart', 'Opera');
INSERT INTO Band_Styles (bname, sgname) VALUES ('Tchaikovsky', 'Opera');
INSERT INTO Band_Styles (bname, sgname) VALUES ('Tchaikovsky', 'Orchestral');
INSERT INTO Band_Styles (bname, sgname) VALUES ('Twisted Sister', 'Thrash Metal');
INSERT INTO Band_Styles (bname, sgname) VALUES ('Testament', 'Thrash Metal');
INSERT INTO Band_Styles (bname, sgname) VALUES ('Tengger Cavalry', 'Folk Metal');
INSERT INTO Band_Styles (bname, sgname) VALUES ('Tengger Cavalry', 'Khoomii');

-- ------------------------------------------------------------------------------------------------

-- 2. (9 points) Create a "User" relation. User needs an ID, name, and home country.
CREATE TABLE `User` (
	uid INT NOT NULL AUTO_INCREMENT,
	uname CHAR(20) NOT NULL,
	homeCountry INT,
	PRIMARY KEY (uid),
	UNIQUE (uname, homeCountry),
	FOREIGN KEY (homeCountry) REFERENCES Country(rid)
);

INSERT INTO `User` (uname, homeCountry) VALUES ('Cristiano Ronaldo', 13);
INSERT INTO `User` (uname, homeCountry) VALUES ('Lionel Messi', 8);
INSERT INTO `User` (uname, homeCountry) VALUES ('Neymar', 7);

-- 3. (9 points) Create a "Favorites" relation. Favorites needs to reference user, and bands.
CREATE TABLE Favorites (
	userId INT,
	bandId INT,
	PRIMARY KEY (userId, bandId),
	FOREIGN KEY (userId) REFERENCES `User`(uid),
	FOREIGN KEY (bandId) REFERENCES Bands(bid)
);

INSERT INTO Favorites (userId, bandId) VALUES (1, 6);
INSERT INTO Favorites (userId, bandId) VALUES (1, 4);
INSERT INTO Favorites (userId, bandId) VALUES (1, 7);
INSERT INTO Favorites (userId, bandId) VALUES (1, 12);
INSERT INTO Favorites (userId, bandId) VALUES (2, 8);
INSERT INTO Favorites (userId, bandId) VALUES (2, 2);
INSERT INTO Favorites (userId, bandId) VALUES (2, 9);
INSERT INTO Favorites (userId, bandId) VALUES (2, 4);
INSERT INTO Favorites (userId, bandId) VALUES (3, 5);
INSERT INTO Favorites (userId, bandId) VALUES (3, 9);
INSERT INTO Favorites (userId, bandId) VALUES (3, 14);
INSERT INTO Favorites (userId, bandId) VALUES (3, 10);

-- 1. (9 points) Create db user called “api” with limited access of read only of initially given tables in the template, 
-- and read/write/update permissions for all additional tables created for this project in the next steps.
create user 'api'@'localhost' identified by 'apiUserpw1.';
grant select on CSC315Final2022.Genre to 'api'@'localhost';
grant select on CSC315Final2022.Sub_Genre to 'api'@'localhost';
grant select on CSC315Final2022.Region to 'api'@'localhost';
grant select on CSC315Final2022.Country to 'api'@'localhost';
grant select on CSC315Final2022.Bands to 'api'@'localhost';
grant select on CSC315Final2022.Band_Origins to 'api'@'localhost';
grant select on CSC315Final2022.Band_Styles to 'api'@'localhost';
grant select, insert, update on CSC315Final2022.`User` to 'api'@'localhost';
grant select, insert, update on CSC315Final2022.Favorites TO 'api'@'localhost';

-- 4. (9 points) Create a query to determine which sub_genres come from which regions.
SELECT sg.sgname AS "Sub Genre Name", r.rname AS "Region Name" FROM Sub_Genre sg
INNER JOIN Band_Styles bs ON bs.sgname=sg.sgname
INNER JOIN Band_Origins bo ON bo.bname=bs.bname
INNER JOIN Country c ON c.cname=bo.cname
INNER JOIN Region r ON r.rname=c.rname
GROUP BY sg.sgname, r.rname
ORDER BY sg.sgname;

-- 5. (9 points) Create a query to determine what other bands, 
-- not currently in their favorites, are of the same sub_genres as those which are.
SELECT bs.sgname AS "Sub Genre Name", bs.bname AS "Band Name" FROM Band_Styles bs WHERE bs.sgname IN(
SELECT sg.sgname FROM Bands b
INNER JOIN Favorites f ON f.bandId=b.bid AND f.userId=1
INNER JOIN Band_Styles bs ON bs.bname=b.bname
INNER JOIN Sub_Genre sg ON sg.sgname=bs.sgname) 
AND bs.bname NOT IN(
SELECT b.bname FROM Bands b
INNER JOIN Favorites f ON f.bandId=b.bid AND f.userId=1
INNER JOIN Band_Styles bs ON bs.bname=b.bname)
ORDER BY bs.sgname;

-- 6. (9 points) Create a query to determine what other bands, 
-- not currently in their favorites, are of the same genres as those which are.
SELECT DISTINCT bs.bname AS "Band Name" FROM Band_Styles bs, Sub_Genre sg WHERE bs.sgname IN
(SELECT sg.sgname FROM Sub_Genre sg WHERE sg.gname IN 
(SELECT DISTINCT sg.gname AS "Genre Name" FROM Sub_Genre sg WHERE sg.sgname IN
(SELECT sg.sgname FROM Bands b
INNER JOIN Favorites f ON f.bandId=b.bid AND f.userId=1
INNER JOIN Band_Styles bs ON bs.bname=b.bname
INNER JOIN Sub_Genre sg ON sg.sgname=bs.sgname))
ORDER BY sg.gname, sg.sgname) AND sg.gname IN
(SELECT DISTINCT sg.gname FROM Sub_Genre sg WHERE sg.gname IN (SELECT DISTINCT sg.gname AS "Genre Name" FROM Sub_Genre sg WHERE sg.sgname 
IN (SELECT sg.sgname FROM Bands b
INNER JOIN Favorites f ON f.bandId=b.bid AND f.userId=1
INNER JOIN Band_Styles bs ON bs.bname=b.bname
INNER JOIN Sub_Genre sg ON sg.sgname=bs.sgname))) AND bs.bname NOT IN
(SELECT DISTINCT b.bname FROM Bands b
INNER JOIN Favorites f ON f.bandId=b.bid AND f.userId=1
INNER JOIN Band_Styles bs ON bs.bname=b.bname);

-- 7. (9 points) Create a query which finds other users who have the same band in their favorites, 
-- and list their other favorite bands.
SELECT DISTINCT u.uname As "Other Users", b.bname "Band Names" FROM Favorites f
JOIN Favorites f1 ON f.bandId=f1.bandId AND f.userId <> f1.userId 
JOIN `User` u ON f1.userId = u.uid
JOIN favorites f2 ON u.uid=f2.userId AND f2.bandId <> f.bandId
JOIN Bands b ON f2.bandId=b.bid 
WHERE f.userId=1;

-- 8. (9 points) Create a query to list other countries, excluding the user’s home country, 
-- where they could travel to where they could hear the same genres as the bands in their favorites.
SELECT DISTINCT bo.cname AS "Other Countries" FROM Band_Origins bo WHERE bname 
IN (SELECT b.bname FROM Bands b WHERE b.bname 
IN (SELECT DISTINCT bs.bname FROM Band_Styles bs WHERE bs.sgname 
IN (SELECT sg.sgname FROM Sub_Genre sg WHERE sg.gname 
IN (SELECT DISTINCT sg.gname FROM Sub_Genre sg WHERE sg.gname 
IN (SELECT DISTINCT sg.gname AS "Genre Name" FROM Sub_Genre sg WHERE sg.sgname 
IN (SELECT sg.sgname FROM Bands b
JOIN Favorites f2 ON f2.bandId=b.bid AND f2.userId=3
JOIN Band_Styles bs ON bs.bname=b.bname
JOIN Sub_Genre sg ON sg.sgname=bs.sgname))))) AND b.bname NOT IN 
(SELECT b.bname FROM Bands b, Favorites f WHERE f.userId = 3 AND f.bandId = b.bid)) 
AND bo.cname NOT IN (SELECT c.cname FROM `User` u 
JOIN Country c ON c.rid = u.homeCountry WHERE u.uid = 3);

-- 9. (9 points) Add appropriate indexing to all tables and optimize all queries.

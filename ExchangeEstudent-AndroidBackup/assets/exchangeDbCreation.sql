CREATE TABLE [ADDRESS] (
[_id] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL,
[str_Name] VARCHAR(30)  NULL,
[str_Number] VARCHAR(30)  NULL,
[apt_Number] NUMERIC  NULL,
[city] VARCHAR(30)  NULL,
[zip_Code] VARCHAR(20)  NULL,
[state] VARCHAR(30)  NULL
);

CREATE TABLE CHAT (_id INTEGER PRIMARY KEY, FK_USER_1 INTEGER, FK_USER_2 INTEGER);

CREATE TABLE [COUNTRY] (
[_id] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,
[name] VARCHAR(30)  NULL,
       FK_ADDRESS integer not null,
   FOREIGN KEY (FK_ADDRESS) REFERENCES ADRESS(id)
);

CREATE TABLE [GROUP_DISK] (
[_id] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,
[name] VARCHAR(30)  NULL,
[discussion] VARCHAR(30)  NULL,
[FK_INVOLVES_IN_USER] INTEGER  NOT NULL
);

CREATE TABLE [INTEREST] (
[_id] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,
[tag] VARCHAR(30)  NULL,
[FK_STUDENT] INTEGER  NOT NULL
);

CREATE TABLE MAJOR (_id INTEGER PRIMARY KEY, name VARCHAR(30), FK_HAS_STUDENT INTEGER);

CREATE TABLE [MESSAGE] (
[_id] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,
[dateCreated] TEXT  NOT NULL,
[content] VARCHAR(30)  NULL,
[FK_FROM_CHAT] INTEGER  NOT NULL
);

CREATE TABLE [PROFILE] (
[_id] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,
[dateCreated] TEXT  NOT NULL,
[FK_HAS_USER] INTEGER  NOT NULL,
[profilePicture] BLOB  NULL
);

CREATE TABLE [STUDENT] (
[_id] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,
[last_name] VARCHAR(30)  NULL,
[middle_Name] VARCHAR(30)  NULL,
[first_Name] VARCHAR(30)  NULL,
[dob] DATE  NULL,
[FK_UNIVERSITY_FOREIGN] integer  NULL,
[FK_UNIVERSITY_HOME] integer  NULL,
[FK_ADDRESS] integer  NOT NULL,
[FK_INTEREST] integer  NOT NULL
);

CREATE TABLE [UNIVERSITY] (
[_id] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,
[name] VARCHAR(30)  NULL,
[FK_COUNTRY] integer  NOT NULL
);

CREATE TABLE [USER] (
[_id] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,
[ssn] VARCHAR(30)  NULL,
[FK_HAS_PROFILE] INTEGER  NOT NULL,
[FK_HAS_GROUP] INTEGER  NOT NULL,
[FK_PARTICIPATE_IN_CHAT] INTEGER  NOT NULL,
[FK_HAS_STUDENT] INTEGER  NOT NULL,
[username] VARCHAR(30)  NOT NULL,
[password] VARCHAR(15)  NOT NULL
);

INSERT INTO 'USER'
(
'ssn',
'username',
'password',
'FK_HAS_PROFILE',
'FK_HAS_GROUP',
'FK_PARTICIPATE_IN_CHAT',
'FK_HAS_STUDENT')
values(
'456123654',
'admin',
'admin',
1,
1,
1,
1);

INSERT INTO 'ADDRESS'
('str_name',
'str_number',
'apt_number',
'city',
'zip_code',
'state'
)
values(
'Oak Street',
5050,
242,
'Kansas-City',
64112,
'Missouri'
);


INSERT INTO 'ADDRESS' ('str_name',
'str_number',
'apt_number',
'city',
'zip_code',
'state'
)
values(
'Kimndor Street',
4040,
20,
'Kansas-City',
64112,
'Missouri'
);


INSERT INTO 'ADDRESS'
('str_name',
'str_number',
'apt_number',
'city',
'zip_code',
'state'
)
values(
'Oak Street',
5050,
312,
'Kansas-City',
64112,
'Missouri'
);

INSERT INTO 'ADDRESS'
('str_name',
'str_number',
'apt_number',
'city',
'zip_code',
'state'
)
values(
'Independency',
77,
111,
'Salt Lake City',
84101,
'Utah'
);

INSERT INTO 'ADDRESS'
('str_name',
'str_number',
'apt_number',
'city',
'zip_code',
'state'
)
values(
'Palms Street',
6079,
10,
'Long Beach',
90805,
'California'
);

INSERT INTO 'COUNTRY'
('name',
'fk_address'
)VALUES(
'Brazil',
1
);

INSERT INTO 'COUNTRY'
('name',
'fk_address'
)VALUES(
'United States',
2
);

INSERT INTO 'COUNTRY'
('name',
'fk_address'
)VALUES(
'France',
3
);

INSERT INTO 'INTEREST'
(
'tag',
'fk_student'
)
valueS(
'#Hardware and Software',
1
);

INSERT INTO 'INTEREST'
(
'tag',
'fk_student'
)
valueS(
'#Android',
2
);

INSERT INTO 'INTEREST'
(
'tag',
'fk_student'
)
valueS(
'#',
3
);

INSERT INTO 'INTEREST'
(
'tag',
'fk_student'
)
valueS(
'#Cirurgy',
4
);

INSERT INTO 'INTEREST'
(
'tag',
'fk_student'
)
valueS(
'#Softare',
5
);

INSERT INTO 'INTEREST'
(
'tag',
'fk_student'
)
valueS(
'#Energy',
6
);

INSERT INTO 'INTEREST'
(
'tag',
'fk_student'
)
valueS(
'#Engines',
7
);

INSERT INTO 'MAJOR'
(
'name',
'fk_has_student'
)
values(
'Computer Engineering',
1
);

INSERT INTO 'MAJOR'
(
'name',
'fk_has_student'
)
values(
'Information Systems',
2
);

INSERT INTO 'MAJOR'
(
'name',
'fk_has_student'
)
values(
' ',
3
);

INSERT INTO 'MAJOR'
(
'name',
'fk_has_student'
)
values(
'Dentistry',
4
);

INSERT INTO 'MAJOR'
(
'name',
'fk_has_student'
)
values(
'Computer Engineering',
5
);

INSERT INTO 'MAJOR'
(
'name',
'fk_has_student'
)
values(
'Eletrical Engineering',
6
);

INSERT INTO 'MAJOR'
(
'name',
'fk_has_student'
)
values(
'Mechanical Engineering',
7
);

INSERT INTO 'STUDENT'
('last_name',
'middle_name',
'first_name',
'dob',
'fk_university_home',
'fk_university_foreign',
'fk_address',
'fk_interest'
)
values (
'Tavares',
'Vieira',
'Thiago',
'1991-09-03',
1,
2,
1,
1
);

INSERT INTO 'STUDENT'
('last_name',
'middle_name',
'first_name',
'dob',
'fk_university_home',
'fk_university_foreign',
'fk_address',
'fk_interest'
)
values (
'Nogueira',
'Augusto',
'Cesar',
'1990-10-12',
6,
2,
2,
2
);

INSERT INTO 'STUDENT'
('last_name',
'middle_name',
'first_name',
'dob',
'fk_university_home',
'fk_university_foreign',
'fk_address',
'fk_interest'
)
values (
'Djimmandoumngar',
' ',
'Kimndor',
'',
2,
2,
3,
3
);

INSERT INTO 'STUDENT'
('last_name',
'middle_name',
'first_name',
'dob',
'fk_university_home',
'fk_university_foreign',
'fk_address',
'fk_interest'
)
values (
'Moura',
' ',
'Eline',
'1993-08-23 ',
7,
2,
4,
4
);

INSERT INTO 'STUDENT'
('last_name',
'middle_name',
'first_name',
'dob',
'fk_university_home',
'fk_university_foreign',
'fk_address',
'fk_interest'
)
values (
'Silva',
'Sander ',
'Alex',
'1993-10-09',
1,
3,
5,
5
);

INSERT INTO 'STUDENT'
('last_name',
'middle_name',
'first_name',
'dob',
'fk_university_home',
'fk_university_foreign',
'fk_address',
'fk_interest'
)
values (
'Dias',
'Gariglio ',
'Taina',
'1992-02-16',
1,
4,
6,
6
);

INSERT INTO 'STUDENT'
('last_name',
'middle_name',
'first_name',
'dob',
'fk_university_home',
'fk_university_foreign',
'fk_address',
'fk_interest'
)
values (
'Brito',
'Ribeiro ',
'Fernando',
'1993-06-05',
1,
5,
7,
7
);

INSERT INTO 'UNIVERSITY'
(
'name',
'fk_country')
values(
'Unifei',
1
);

INSERT INTO 'UNIVERSITY'
(
'name',
'fk_country')
values(
'UMKC',
2
);

INSERT INTO 'UNIVERSITY'
(
'name',
'fk_country')
values(
'University of Utah',
2
);

INSERT INTO 'UNIVERSITY'
(
'name',
'fk_country')
values(

'California State University',
2
);


INSERT INTO 'UNIVERSITY'
(
'name',
'fk_country')
values(
'Université de technologie de Compiègne',
3
);

INSERT INTO 'UNIVERSITY'
(
'name',
'fk_country')
values(
'UFSCAR',
1
);

INSERT INTO 'UNIVERSITY'
(
'name',
'fk_country')
values(
'UEPB',
1
);

INSERT INTO 'USER'
(
'ssn',
'username',
'password',
'FK_HAS_PROFILE',
'FK_HAS_GROUP',
'FK_PARTICIPATE_IN_CHAT',
'FK_HAS_STUDENT')
values(
'456123654',
'admin',
'admin',
1,
1,
1,
1);




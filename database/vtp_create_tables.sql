-- Create tables for Semester 1 2023 CTG example ER ModelPRAGMA foreign_keys = OFF;
PRAGMA foreign_keys = OFF;
drop table if exists LGA;
drop table if exists Population;
drop table if exists SchoolYear;
PRAGMA foreign_keys = ON;

CREATE TABLE LGA (
    code              INTEGER NOT NULL,
    year              INTEGER NOT NULL,
    name              TEXT NOT NULL,
    state_abbr        CHAR (10),
    area              DOUBLE,
    latitude          DOUBLE,
    longitude         DOUBLE,
    PRIMARY KEY (code, year)
);

CREATE TABLE Population (
    lga_code          INTEGER NOT NULL,
    lga_year          INTEGER NOT NULL,
    indigenous_status TEXT NOT NULL,
    sex               CHAR (1) NOT NULL,
    age_category      TEXT NOT NULL,
    count             INTEGER NOT NULL,
    age_min           INTEGER,
    age_max           INTEGER,
    PRIMARY KEY (lga_code, lga_year, indigenous_status, sex, age_category)
    FOREIGN KEY (lga_code) REFERENCES LGA(lga_code)
    FOREIGN KEY (lga_year) REFERENCES LGA(lga_year)
);

CREATE TABLE SchoolYear (
    lga_code          INTEGER NOT NULL,
    lga_year          INTEGER NOT NULL,
    indigenous_status TEXT NOT NULL,
    sex               CHAR (1) NOT NULL,
    School_Year      TEXT NOT NULL,
    count             INTEGER NOT NULL,
    PRIMARY KEY (lga_code, lga_year, indigenous_status, sex, School_Year)
    FOREIGN KEY (lga_code) REFERENCES LGA(lga_code)
    FOREIGN KEY (lga_year) REFERENCES LGA(lga_year)
);

CREATE TABLE LTHC (
    lga_code          INTEGER NOT NULL,
    lga_year          INTEGER NOT NULL,
    indigenous_status TEXT NOT NULL,
    sex               CHAR (1) NOT NULL,
    Long_Term      TEXT NOT NULL,
    count             INTEGER NOT NULL,
    PRIMARY KEY (lga_code, lga_year, indigenous_status, sex, Long_Term)
    FOREIGN KEY (lga_code) REFERENCES LGA(lga_code)
    FOREIGN KEY (lga_year) REFERENCES LGA(lga_year)
);

CREATE TABLE Non_School_ED (
    lga_code          INTEGER NOT NULL,
    lga_year          INTEGER NOT NULL,
    indigenous_status TEXT NOT NULL,
    sex               CHAR (1) NOT NULL,
    Education      TEXT NOT NULL,
    count             INTEGER NOT NULL,
    PRIMARY KEY (lga_code, lga_year, indigenous_status, sex, Education)
    FOREIGN KEY (lga_code) REFERENCES LGA(lga_code)
    FOREIGN KEY (lga_year) REFERENCES LGA(lga_year)
);
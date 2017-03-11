-- Database: education

-- DROP DATABASE education;

CREATE DATABASE education
    WITH 
    OWNER = ais
    ENCODING = 'UTF8'
    LC_COLLATE = 'Japanese_Japan.932'
    LC_CTYPE = 'Japanese_Japan.932'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE education
    IS 'JAVA‹³ˆç—pDatabase';
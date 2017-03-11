-- Table: public.tmp_zipcode

-- DROP TABLE public.tmp_zipcode;

CREATE TABLE public.tmp_zipcode
(
    zipold text COLLATE pg_catalog."default" NOT NULL,
    prefkana text COLLATE pg_catalog."default" NOT NULL,
    pref text COLLATE pg_catalog."default" NOT NULL,
    jiscode text COLLATE pg_catalog."default" NOT NULL,
    flag5 smallint,
    flag4 smallint,
    flag3 smallint,
    flag2 smallint,
    flag1 smallint,
    citykana text COLLATE pg_catalog."default" NOT NULL,
    citycode text COLLATE pg_catalog."default" NOT NULL,
    city text COLLATE pg_catalog."default" NOT NULL,
    areakana text COLLATE pg_catalog."default" NOT NULL,
    area text COLLATE pg_catalog."default" NOT NULL,
    flag6 smallint
)
WITH (
    OIDS = FALSE
)
TABLESPACE ais_education;

ALTER TABLE public.tmp_zipcode
    OWNER to ais;
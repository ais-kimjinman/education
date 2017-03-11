CREATE TABLE public.tbl_zip
(
    zip_seq integer NOT NULL DEFAULT nextval('tbl_zip_seq'::regclass),
    prefcode integer,
    citycode integer,
    zip7 text COLLATE pg_catalog."default",
    prefkana text COLLATE pg_catalog."default",
    citykana text COLLATE pg_catalog."default",
    areakana text COLLATE pg_catalog."default",
    pref text COLLATE pg_catalog."default",
    city text COLLATE pg_catalog."default",
    area text COLLATE pg_catalog."default",
    CONSTRAINT tbl_zip_pkey PRIMARY KEY (zip_seq)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.tbl_zip
    OWNER to postgres;
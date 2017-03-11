CREATE SEQUENCE public.tbl_zip_seq
    INCREMENT 1
    START 100000
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.tbl_zip_seq
    OWNER TO postgres;
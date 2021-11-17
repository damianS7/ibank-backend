CREATE TABLE public.users (
    id integer NOT NULL AUTO_INCREMENT,
    username character varying(30) UNIQUE NOT NULL,
    password character varying NOT NULL,
    email character varying(255) UNIQUE NOT NULL,
    role character varying(30)
);

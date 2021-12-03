CREATE TABLE public.users (
    id integer AUTO_INCREMENT primary key,
    username character varying(30) UNIQUE NOT NULL,
    email character varying(255) UNIQUE NOT NULL,
    password character varying NOT NULL,
    role character varying(30)
);

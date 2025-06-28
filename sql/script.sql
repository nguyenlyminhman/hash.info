-- public.hashed_data definition
-- Drop table
-- DROP TABLE public.hashed_data;

CREATE TABLE public.hashed_data (
	id serial4 NOT NULL,
	hashed_passport varchar NULL,
	encrypted_passport json NULL
);

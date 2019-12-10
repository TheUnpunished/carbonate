--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.10
-- Dumped by pg_dump version 9.6.11

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: contact_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.contact_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.contact_seq OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: contact; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contact (
    id bigint DEFAULT nextval('public.contact_seq'::regclass) NOT NULL,
    fullname character varying(255) NOT NULL,
    lastname character varying(255),
    firstname character varying(255),
    inblacklist boolean DEFAULT false NOT NULL
);


ALTER TABLE public.contact OWNER TO postgres;

--
-- Name: phonetype_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.phonetype_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.phonetype_seq OWNER TO postgres;

--
-- Name: dict_phonetype; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.dict_phonetype (
    id bigint DEFAULT nextval('public.phonetype_seq'::regclass) NOT NULL,
    code character varying(255),
    name character varying(255),
    fullname character varying(255)
);


ALTER TABLE public.dict_phonetype OWNER TO postgres;

--
-- Name: phone_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.phone_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.phone_seq OWNER TO postgres;

--
-- Name: phone; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.phone (
    contact_id bigint NOT NULL,
    id bigint DEFAULT nextval('public.phone_seq'::regclass) NOT NULL,
    phonetype_id bigint,
    phonenumber character varying(50) NOT NULL
);


ALTER TABLE public.phone OWNER TO postgres;

--
-- Data for Name: contact; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.contact (id, fullname, lastname, firstname, inblacklist) FROM stdin;
1	Андрей Яковлев	Яковлев	Андрей	t
\.


--
-- Name: contact_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.contact_seq', 1, true);


--
-- Data for Name: dict_phonetype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.dict_phonetype (id, code, name, fullname) FROM stdin;
1	1	Домашний	Домашний
2	2	Мобильный	Мобильный
3	3	Рабочий	Рабочий
9	+7(8482)	Домашний	Домашний
10	+7(905)	Мобильный	Мобильный
11	+7(905)	Домашний	Домашний
12	123	Мобильный	Мобильный
13	123	Домашний	Домашний
\.


--
-- Data for Name: phone; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.phone (contact_id, id, phonetype_id, phonenumber) FROM stdin;
1	5	12	456595
\.


--
-- Name: phone_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.phone_seq', 5, true);


--
-- Name: phonetype_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.phonetype_seq', 14, true);


--
-- Name: contact contact_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contact
    ADD CONSTRAINT contact_pkey PRIMARY KEY (id);


--
-- Name: dict_phonetype dict_phonetype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dict_phonetype
    ADD CONSTRAINT dict_phonetype_pkey PRIMARY KEY (id);


--
-- Name: dict_phonetype dict_phonetype_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dict_phonetype
    ADD CONSTRAINT dict_phonetype_unique UNIQUE (code, name, fullname);


--
-- Name: phone phone_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.phone
    ADD CONSTRAINT phone_pkey PRIMARY KEY (id);


--
-- Name: phone fk_contact; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.phone
    ADD CONSTRAINT fk_contact FOREIGN KEY (contact_id) REFERENCES public.contact(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: phone fk_phonetype; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.phone
    ADD CONSTRAINT fk_phonetype FOREIGN KEY (phonetype_id) REFERENCES public.dict_phonetype(id) ON UPDATE CASCADE;


--
-- PostgreSQL database dump complete
--


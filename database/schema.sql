--
-- PostgreSQL database dump
--

\restrict Jw2oQo7BTJvDZ84NMJSrP0oBd2nf9RZPyXVlPR7AUwvanjuD559nwvgQLEeSc4N

-- Dumped from database version 18.6
-- Dumped by pg_dump version 18.6

-- Started on 2026-11-13 19:34:45

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 222 (class 1259 OID 16404)
-- Name: artists; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.artists (
    id integer NOT NULL,
    name character varying(150) NOT NULL
);


--
-- TOC entry 221 (class 1259 OID 16403)
-- Name: artists_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.artists ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.artists_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 229 (class 1259 OID 16480)
-- Name: favorite_music; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.favorite_music (
    user_id integer NOT NULL,
    music_id integer NOT NULL
);

--
-- TOC entry 224 (class 1259 OID 16414)
-- Name: music; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.music (
    id integer NOT NULL,
    title character varying(100) NOT NULL,
    duration integer NOT NULL,
    lyrics text,
    CONSTRAINT music_duration_check CHECK ((duration > 0))
);


--
-- TOC entry 225 (class 1259 OID 16431)
-- Name: music_artists; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.music_artists (
    music_id integer NOT NULL,
    artist_id integer NOT NULL
);


--
-- TOC entry 223 (class 1259 OID 16413)
-- Name: music_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.music ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.music_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 228 (class 1259 OID 16463)
-- Name: playlist_music; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.playlist_music (
    playlist_id integer NOT NULL,
    music_id integer NOT NULL
);


--
-- TOC entry 227 (class 1259 OID 16449)
-- Name: playlists; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.playlists (
    id integer NOT NULL,
    title character varying(150) NOT NULL,
    user_id integer NOT NULL
);


--
-- TOC entry 226 (class 1259 OID 16448)
-- Name: playlists_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.playlists ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.playlists_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 220 (class 1259 OID 16388)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id integer NOT NULL,
    email character varying(255) NOT NULL,
    password_hash text NOT NULL
);


--
-- TOC entry 219 (class 1259 OID 16387)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 4889 (class 2606 OID 16412)
-- Name: artists artists_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artists
    ADD CONSTRAINT artists_name_key UNIQUE (name);


--
-- TOC entry 4891 (class 2606 OID 16410)
-- Name: artists artists_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artists
    ADD CONSTRAINT artists_pkey PRIMARY KEY (id);


--
-- TOC entry 4893 (class 2606 OID 16425)
-- Name: music music_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.music
    ADD CONSTRAINT music_pkey PRIMARY KEY (id);


--
-- TOC entry 4901 (class 2606 OID 16486)
-- Name: favorite_music pk_favorite_music; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.favorite_music
    ADD CONSTRAINT pk_favorite_music PRIMARY KEY (user_id, music_id);


--
-- TOC entry 4895 (class 2606 OID 16437)
-- Name: music_artists pk_music_artists; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.music_artists
    ADD CONSTRAINT pk_music_artists PRIMARY KEY (music_id, artist_id);


--
-- TOC entry 4899 (class 2606 OID 16469)
-- Name: playlist_music pk_playlist_music; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.playlist_music
    ADD CONSTRAINT pk_playlist_music PRIMARY KEY (playlist_id, music_id);


--
-- TOC entry 4897 (class 2606 OID 16456)
-- Name: playlists playlists_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.playlists
    ADD CONSTRAINT playlists_pkey PRIMARY KEY (id);


--
-- TOC entry 4885 (class 2606 OID 16399)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 4887 (class 2606 OID 16397)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 4907 (class 2606 OID 16492)
-- Name: favorite_music fk_favorite_music_music; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.favorite_music
    ADD CONSTRAINT fk_favorite_music_music FOREIGN KEY (music_id) REFERENCES public.music(id) ON DELETE CASCADE;


--
-- TOC entry 4908 (class 2606 OID 16487)
-- Name: favorite_music fk_favorite_music_user; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.favorite_music
    ADD CONSTRAINT fk_favorite_music_user FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4902 (class 2606 OID 16443)
-- Name: music_artists fk_music_artists_artist; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.music_artists
    ADD CONSTRAINT fk_music_artists_artist FOREIGN KEY (artist_id) REFERENCES public.artists(id) ON DELETE CASCADE;


--
-- TOC entry 4903 (class 2606 OID 16438)
-- Name: music_artists fk_music_artists_music; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.music_artists
    ADD CONSTRAINT fk_music_artists_music FOREIGN KEY (music_id) REFERENCES public.music(id) ON DELETE CASCADE;


--
-- TOC entry 4905 (class 2606 OID 16475)
-- Name: playlist_music fk_playlist_music_music; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.playlist_music
    ADD CONSTRAINT fk_playlist_music_music FOREIGN KEY (music_id) REFERENCES public.music(id) ON DELETE CASCADE;


--
-- TOC entry 4906 (class 2606 OID 16470)
-- Name: playlist_music fk_playlist_music_playlist; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.playlist_music
    ADD CONSTRAINT fk_playlist_music_playlist FOREIGN KEY (playlist_id) REFERENCES public.playlists(id) ON DELETE CASCADE;


--
-- TOC entry 4904 (class 2606 OID 16457)
-- Name: playlists fk_playlists_user; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.playlists
    ADD CONSTRAINT fk_playlists_user FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


-- Completed on 2026-11-13 19:34:46

--
-- PostgreSQL database dump complete
--

\unrestrict Jw2oQo7BTJvDZ84NMJSrP0oBd2nf9RZPyXVlPR7AUwvanjuD559nwvgQLEeSc4N


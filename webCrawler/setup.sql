-- Table: crawler.page

-- DROP TABLE crawler.page;

CREATE TABLE IF NOT EXISTS crawler.page
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    research bigint NOT NULL,
    uri character varying(300) COLLATE pg_catalog."default" NOT NULL,
    distance bigint NOT NULL,
    parent character varying(300) COLLATE pg_catalog."default",
    elcount bigint NOT NULL,
    CONSTRAINT page_pkey PRIMARY KEY (id),
    CONSTRAINT research FOREIGN KEY (research)
        REFERENCES crawler.research (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT positive_elcount CHECK (elcount >= 0) NOT VALID
)

-- Table: crawler.research

-- DROP TABLE crawler.research;

CREATE TABLE IF NOT EXISTS crawler.research
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    "time" timestamp without time zone NOT NULL,
    startpage character varying(300) COLLATE pg_catalog."default" NOT NULL,
    mufetched bigint NOT NULL,
    flcount bigint NOT NULL,
    maxdist bigint NOT NULL,
    elcount bigint NOT NULL,
    telapse bigint NOT NULL,
    CONSTRAINT id PRIMARY KEY (id),
    CONSTRAINT time_start_page UNIQUE ("time", startpage),
    CONSTRAINT positive_max_fetched CHECK (mufetched >= 0) NOT VALID,
    CONSTRAINT positive_fetched_count CHECK (flcount >= 0) NOT VALID,
    CONSTRAINT positive_max_distance CHECK (maxdist >= 0) NOT VALID,
    CONSTRAINT positive_ext_links_count CHECK (elcount >= 0) NOT VALID,
    CONSTRAINT fetched_count_under_max CHECK (flcount <= mufetched) NOT VALID,
    CONSTRAINT positive_elapsed_time CHECK (telapse >= 0) NOT VALID
)

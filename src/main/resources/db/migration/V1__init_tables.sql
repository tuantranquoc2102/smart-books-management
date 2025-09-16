CREATE TABLE IF NOT EXISTS books (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100),
    content OID,          -- Hibernate @Lob String -> OID
    published_year INT,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
    );

CREATE INDEX IF NOT EXISTS idx_books_status ON books(status);

CREATE TABLE chapter_summaries
(
    id            BIGSERIAL PRIMARY KEY,
    book_id       BIGINT       NOT NULL,
    chapter_index INT          NOT NULL,
    chapter_title VARCHAR(500) NOT NULL,
    summary       TEXT,
    raw_text      TEXT,
    CONSTRAINT fk_chapter_summaries_books
        FOREIGN KEY (book_id)
            REFERENCES books (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_chapter_summaries_book_id ON chapter_summaries (book_id);
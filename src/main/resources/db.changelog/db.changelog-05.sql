CREATE TABLE users(
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    email VARCHAR(255) UNIQUE CHECK(email ~* '^[A-Za-z0-9._%+-]+@[A-Z]+@[A-Za-z0-9.-].[A-Za-z]{2,}$'),
    password VARCHAR(255) NOT NULL,
    role VARCHAR(10)
)
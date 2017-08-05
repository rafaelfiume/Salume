CREATE TABLE salumistore.reputation (                                                      -- long-established time-honored product?
    id          integer         PRIMARY KEY,
    name        varchar(15)     NOT NULL UNIQUE
);

CREATE TABLE salumistore.products (
    id          serial          PRIMARY KEY,
    name        varchar(80)     NOT NULL UNIQUE,
    price       numeric(5, 2)   NOT NULL CONSTRAINT positive_price CHECK (price > 0),
    fat         real            NOT NULL CONSTRAINT positive_fat   CHECK (fat > 0),          -- % of fat
    reputation  integer         REFERENCES salumistore.reputation (id) ON DELETE RESTRICT
);


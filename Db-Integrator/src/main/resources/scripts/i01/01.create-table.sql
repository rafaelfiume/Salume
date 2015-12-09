-- TODO RF 12/10/2015 Pass the schema as a maven property?

CREATE TABLE salumistore.reputation (                                                      -- long-established time-honored product?
    id          integer         PRIMARY KEY,
    name        varchar(15)     NOT NULL UNIQUE
);

CREATE TABLE salumistore.products (
    id          integer         PRIMARY KEY,
    name        varchar(80)     NOT NULL UNIQUE,
    price       numeric(5, 2)   NOT NULL CONSTRAINT positive_price CHECK (price > 0),
    fat         real            NOT NULL CONSTRAINT positive_fat CHECK (fat > 0),          -- % of fat
    reputation  integer         REFERENCES salumistore.reputation (id) ON DELETE RESTRICT
);

INSERT INTO salumistore.reputation VALUES (1, 'Traditional');
INSERT INTO salumistore.reputation VALUES (2, 'Normal'     );

INSERT INTO salumistore.products VALUES (1, 'Cheap Salume'             , 15.55, 49.99, 2);
INSERT INTO salumistore.products VALUES (2, 'Light Salume'             , 29.55, 31.00, 2);
INSERT INTO salumistore.products VALUES (3, 'Not Light In Your Pocket' , 57.37, 31.00, 2);
INSERT INTO salumistore.products VALUES (4, 'Traditional Salume'       , 41.60, 37.00, 1);
INSERT INTO salumistore.products VALUES (5, 'Premium Salume'           , 73.23, 38.00, 1);

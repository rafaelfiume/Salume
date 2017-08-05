CREATE TABLE salumistore.variety (
    id            serial         PRIMARY KEY,
    name          varchar(50)    NOT NULL UNIQUE,
    image_link    varchar(55)
);

INSERT INTO salumistore.variety VALUES (1, 'Bastardei'                  , NULL);
INSERT INTO salumistore.variety VALUES (2, 'Chorizo'                    , '3/3f/Palacioschorizo.jpg');
INSERT INTO salumistore.variety VALUES (3, 'Falukorv'                   , '5/54/Falukorv.jpg');
INSERT INTO salumistore.variety VALUES (4, 'Fuet'                       , '0/05/Fuet.jpg');
INSERT INTO salumistore.variety VALUES (5, 'Kabanos'                    , '9/9e/Kabanos.jpg');
INSERT INTO salumistore.variety VALUES (6, '''Nduja'                    , '0/00/Nduja.jpg');
INSERT INTO salumistore.variety VALUES (7, 'Salam casalin'              , NULL);
INSERT INTO salumistore.variety VALUES (8, 'Salame all''aglio'          , '4/4f/Salami_all''aglio_appena_fatti.png');
INSERT INTO salumistore.variety VALUES (9, 'Salame_Brianza Lombardo'    , NULL);

ALTER TABLE salumistore.products ADD COLUMN     variety               INTEGER;
ALTER TABLE salumistore.products ADD CONSTRAINT products_variety_fkey FOREIGN KEY (variety) REFERENCES salumistore.variety (id) ON DELETE RESTRICT;

UPDATE salumistore.products SET variety = 2  WHERE name = 'Chorizo Spagnolo';
UPDATE salumistore.products SET variety = 3  WHERE name = 'Swedish Falukorv';
UPDATE salumistore.products SET variety = 4  WHERE name = 'Light Fuet';
UPDATE salumistore.products SET variety = 5  WHERE name = 'Premium Polish Kabanos';
UPDATE salumistore.products SET variety = 6  WHERE name = 'Premium ''Nduja Calabrese';
UPDATE salumistore.products SET variety = 7  WHERE name = 'Gourmet Salam Casalin';
UPDATE salumistore.products SET variety = 8  WHERE name = 'Salame all''aglio';
UPDATE salumistore.products SET variety = 9  WHERE name = 'Salame Brianza D.O.P.';
UPDATE salumistore.products SET variety = 1  WHERE name = 'Salame Bastardei';

ALTER TABLE salumistore.products ALTER COLUMN variety SET NOT NULL;

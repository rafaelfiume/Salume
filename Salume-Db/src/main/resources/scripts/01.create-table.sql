CREATE TABLE salumistore.weather ( -- TODO RF 12/10/2015 Pass the schema as a maven property
    city            varchar(80),
    temp_lo         int,           -- low temperature
    temp_hi         int,           -- high temperature
    prcp            real,          -- precipitation
    date            date
);
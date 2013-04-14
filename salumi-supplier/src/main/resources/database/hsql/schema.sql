drop table if exists product;
drop table if exists category;
drop table if exists product_category;
drop table if exists profile;

create table product (
   id int not null,
   name varchar(80) not null unique,
   price decimal not null,
   primary key (id)
);

create table category (
   id int not null,
   name varchar(80) not null unique,
   primary key (id)
);
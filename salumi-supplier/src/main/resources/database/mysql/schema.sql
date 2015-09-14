drop table if exists product;
drop table if exists category;
drop table if exists product_category;
drop table if exists profile;

create table customer_profile (
   id int not null auto_increment,
   name varchar(80) not null unique,
   primary key (id)
);

create table category (
   id int not null auto_increment,
   name varchar(80) not null unique,
   primary key (id)
);

create table product (
   id int not null auto_increment,
   name varchar(80) not null unique,
   price decimal not null,
   primary key (id)
);

create table product_category (
   category_id int not null,
   product_id int not null,
   primary key (category_id, product_id),
   constraint fk_product_category_category_id foreign key (category_id) references category (id),
   constraint fk_product_category_product_id foreign key (product_id) references product (id)
);


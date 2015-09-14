insert into customer_profile(id, name) values (1, 'Premiun');
insert into customer_profile(id, name) values (2, 'Gourmet');
insert into customer_profile(id, name) values (3, 'Magic');
insert into customer_profile(id, name) values (4, 'Healthy');

insert into category(id, name) values (1, 'Salume');
insert into category(id, name) values (2, 'Prosciutto'); /* ham */
insert into category(id, name) values (3, 'Formaggio'); /* cheese */
insert into category(id, name) values (4, 'Formaggio Bianco'); /* white cheese */

insert into product(id, name, price) values (1, 'Salume Fa Schifo', 12.21);
insert into product(id, name, price) values (2, 'Salume Caro', 29.11);
insert into product(id, name, price) values (3, 'Prosciutto Cotto', 38.90);
insert into product(id, name, price) values (4, 'Prosciutto Crudo Roma', 45.55);
insert into product(id, name, price) values (5, 'Gorgonzola', 34.56);
insert into product(id, name, price) values (6, 'Mozzarela', 32.05);
insert into product(id, name, price) values (7, 'Ricota', 17.21);

insert into product_category(category_id, product_id) values (1, 1);
insert into product_category(category_id, product_id) values (1, 2);
insert into product_category(category_id, product_id) values (2, 3);
insert into product_category(category_id, product_id) values (2, 4);
insert into product_category(category_id, product_id) values (3, 5);
insert into product_category(category_id, product_id) values (3, 6);
insert into product_category(category_id, product_id) values (3, 7);
insert into product_category(category_id, product_id) values (4, 6);
insert into product_category(category_id, product_id) values (4, 7);

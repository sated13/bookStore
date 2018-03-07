-- book categories

insert into book_categories(book_category_id, category) values(1, 'super books');
insert into book_categories(book_category_id, category) values(2, 'useful books');
insert into book_categories(book_category_id, category) values(3, 'for dummies');
insert into book_categories(book_category_id, category) values(4, 'just reading');
insert into book_categories(book_category_id, category) values(5, 'improving skills');
insert into book_categories(book_category_id, category) values(6, 'category_6');
commit;

--books

insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(1, 'Super book', 1000000, 1000, 2019, 80);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(2, 'New Super book', 2000000, 2000, 2019, 100);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(3, 'Fresh Super book', 1500000, 1500, 2019, 90);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(4, 'Useful book', 1000000, 1000, 2019, 81);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(5, 'Super book for dummies', 1000000, 10000, 2015, 20);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(6, 'Just Super book', 1000000, 1000, 2017, 66);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(7, 'Introspection of consciousness', 50, 100, 2017, 24);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(8, 'Learning Java EE 7', 1000, 630, 2014, 8);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(9, 'Java EE 7 for dummies', 1000, 1000, 2016, 21);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(10, 'Super Spring', 300, 1000, 2015, 7);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(11, 'Super book', 10, 3, 2016, 99);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(12, 'Dancing with dummies', 500, 1306, 2005, 49);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(13, 'Huyak, huyak and Production', 517, 311, 2018, 16);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(14, 'Working with dummies', 100, 408, 2003, 41);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(15, 'I''m the best', 31, 89, 2008, 31);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(16, 'The Ballmer peak', 1357, 503, 1989, 31);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(17, 'Read_Me.txt', 5000, 1, 2001, 1);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(18, 'Introduction to self education', 15, 139, 2007, 22);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(19, 'It''s working on my computer', 759, 5, 2014, 19);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(20, 'How to spend 5000000$ on the developing of NotePad+++++++', 69, 29, 2018, 150);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(21, 'How to use ''It''s not a bug'' with illustrations', 601, 1061, 2017, 17);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(22, 'White book', 567, 500, 2000, 4);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(23, 'Black book', 891, 1500, 2000, 50);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(24, 'Yellow book', 1311, 91, 2000, 23);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(25, 'Super Mario brothers and broken ...', 10000, 917, 1995, 42);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(26, 'Are you stupid?', 853, 612, 1999, 2);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(27, 'Production. Be or not to be', 6000, 817, 2013, 13);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(28, 'read me, Read Me, READ ME!!!!', 1000000, 1000, 2003, 3);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(29, 'Working for an interesting company', 655, 122, 2016, 15);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(30, 'How to pass all the tests', 4446, 666, 2009, 66);
insert into books(book_id, book_title, number_of_copies, number_of_pages, year, price) values(31, 'Performance is not for us', 5600, 333, 2018, 31);
commit;

--book_categories

insert into books_categories(book_id, book_category_id) values(1, 1);
insert into books_categories(book_id, book_category_id) values(2, 1);
insert into books_categories(book_id, book_category_id) values(3, 1);
insert into books_categories(book_id, book_category_id) values(1, 3);
insert into books_categories(book_id, book_category_id) values(2, 3);
insert into books_categories(book_id, book_category_id) values(3, 3);
insert into books_categories(book_id, book_category_id) values(4, 2);
insert into books_categories(book_id, book_category_id) values(5, 1);
insert into books_categories(book_id, book_category_id) values(5, 2);
insert into books_categories(book_id, book_category_id) values(5, 3);
insert into books_categories(book_id, book_category_id) values(6, 1);
insert into books_categories(book_id, book_category_id) values(6, 4);
insert into books_categories(book_id, book_category_id) values(7, 6);
insert into books_categories(book_id, book_category_id) values(7, 5);
insert into books_categories(book_id, book_category_id) values(7, 2);
insert into books_categories(book_id, book_category_id) values(8, 2);
insert into books_categories(book_id, book_category_id) values(8, 5);
insert into books_categories(book_id, book_category_id) values(9, 2);
insert into books_categories(book_id, book_category_id) values(9, 5);
insert into books_categories(book_id, book_category_id) values(9, 3);
insert into books_categories(book_id, book_category_id) values(10, 5);
insert into books_categories(book_id, book_category_id) values(11, 1);
insert into books_categories(book_id, book_category_id) values(11, 4);
insert into books_categories(book_id, book_category_id) values(12, 3);
insert into books_categories(book_id, book_category_id) values(13, 2);
insert into books_categories(book_id, book_category_id) values(13, 5);
insert into books_categories(book_id, book_category_id) values(14, 5);
insert into books_categories(book_id, book_category_id) values(15, 1);
insert into books_categories(book_id, book_category_id) values(16, 2);
insert into books_categories(book_id, book_category_id) values(16, 5);
insert into books_categories(book_id, book_category_id) values(16, 6);
insert into books_categories(book_id, book_category_id) values(17, 4);
insert into books_categories(book_id, book_category_id) values(18, 2);
insert into books_categories(book_id, book_category_id) values(18, 5);
insert into books_categories(book_id, book_category_id) values(19, 2);
insert into books_categories(book_id, book_category_id) values(20, 2);
insert into books_categories(book_id, book_category_id) values(21, 2);
insert into books_categories(book_id, book_category_id) values(22, 4);
insert into books_categories(book_id, book_category_id) values(23, 4);
insert into books_categories(book_id, book_category_id) values(24, 4);
insert into books_categories(book_id, book_category_id) values(25, 4);
insert into books_categories(book_id, book_category_id) values(26, 2);
insert into books_categories(book_id, book_category_id) values(26, 5);
insert into books_categories(book_id, book_category_id) values(27, 2);
insert into books_categories(book_id, book_category_id) values(27, 5);
insert into books_categories(book_id, book_category_id) values(28, 4);
insert into books_categories(book_id, book_category_id) values(29, 2);
insert into books_categories(book_id, book_category_id) values(30, 5);
insert into books_categories(book_id, book_category_id) values(31, 2);
commit;

--BOOK_CATEGORIES_BOOKS
/*insert into BOOK_CATEGORIES_BOOKS values(1, 1);
commit;
insert into BOOK_CATEGORIES_BOOKS values(1, 2);
commit;
insert into BOOK_CATEGORIES_BOOKS values(1, 3);
commit;
insert into BOOK_CATEGORIES_BOOKS values(1, 5);
commit;
insert into BOOK_CATEGORIES_BOOKS values(1, 6);
commit;
insert into BOOK_CATEGORIES_BOOKS values(1, 11);
commit;
insert into BOOK_CATEGORIES_BOOKS values(1, 15);
commit;
insert into BOOK_CATEGORIES_BOOKS values(2, 4);
commit;
insert into BOOK_CATEGORIES_BOOKS values(2, 5);
commit;
insert into BOOK_CATEGORIES_BOOKS values(2, 7);
commit;
insert into BOOK_CATEGORIES_BOOKS values(2, 8);
commit;
insert into BOOK_CATEGORIES_BOOKS values(2, 9);
commit;
insert into BOOK_CATEGORIES_BOOKS values(2, 13);
commit;
insert into BOOK_CATEGORIES_BOOKS values(2, 16);
commit;
insert into BOOK_CATEGORIES_BOOKS values(2, 18);
commit;
insert into BOOK_CATEGORIES_BOOKS values(2, 19);
commit;
insert into BOOK_CATEGORIES_BOOKS values(2, 20);
commit;
insert into BOOK_CATEGORIES_BOOKS values(2, 21);
commit;
insert into BOOK_CATEGORIES_BOOKS values(2, 26);
commit;
insert into BOOK_CATEGORIES_BOOKS values(2, 27);
commit;
insert into BOOK_CATEGORIES_BOOKS values(2, 29);
commit;
insert into BOOK_CATEGORIES_BOOKS values(2, 31);
commit;
insert into BOOK_CATEGORIES_BOOKS values(3, 1);
commit;
insert into BOOK_CATEGORIES_BOOKS values(3, 2);
commit;
insert into BOOK_CATEGORIES_BOOKS values(3, 3);
commit;
insert into BOOK_CATEGORIES_BOOKS values(3, 5);
commit;
insert into BOOK_CATEGORIES_BOOKS values(3, 9);
commit;
insert into BOOK_CATEGORIES_BOOKS values(3, 12);
commit;
insert into BOOK_CATEGORIES_BOOKS values(4, 6);
commit;
insert into BOOK_CATEGORIES_BOOKS values(4, 11);
commit;
insert into BOOK_CATEGORIES_BOOKS values(4, 17);
commit;
insert into BOOK_CATEGORIES_BOOKS values(4, 22);
commit;
insert into BOOK_CATEGORIES_BOOKS values(4, 23);
commit;
insert into BOOK_CATEGORIES_BOOKS values(4, 24);
commit;
insert into BOOK_CATEGORIES_BOOKS values(4, 25);
commit;
insert into BOOK_CATEGORIES_BOOKS values(4, 28);
commit;
insert into BOOK_CATEGORIES_BOOKS values(5, 7);
commit;
insert into BOOK_CATEGORIES_BOOKS values(5, 8);
commit;
insert into BOOK_CATEGORIES_BOOKS values(5, 9);
commit;
insert into BOOK_CATEGORIES_BOOKS values(5, 10);
commit;
insert into BOOK_CATEGORIES_BOOKS values(5, 13);
commit;
insert into BOOK_CATEGORIES_BOOKS values(5, 14);
commit;
insert into BOOK_CATEGORIES_BOOKS values(5, 16);
commit;
insert into BOOK_CATEGORIES_BOOKS values(5, 18);
commit;
insert into BOOK_CATEGORIES_BOOKS values(5, 26);
commit;
insert into BOOK_CATEGORIES_BOOKS values(5, 27);
commit;
insert into BOOK_CATEGORIES_BOOKS values(5, 30);
commit;
insert into BOOK_CATEGORIES_BOOKS values(6, 7);
commit;
insert into BOOK_CATEGORIES_BOOKS values(6, 16);
commit;*/

--users

insert into users(user_id, username, password, enabled) values(1, 'admin', '$2a$10$1HUIfBS2OWXwqNLjBmljEueZzAtrgfoUDn7TSdFf8rryjlhYvKcfS', 1);//password: adminishe
commit;
INSERT INTO users(user_id, username, password, enabled) values(2, 'test', '$2a$10$JUR6XWi.KIixW0lFSevj7OWosw0DWOY9BtqZPtMf5DouJN7R8fHVS', 1); //test
commit;
insert into users(user_id, username, password, enabled) values(3, 'customer_1', '$2a$10$.YpNXLtMLbRbw7hcbeVU3uq7tQp7gPZGtlwhQ1KNCIpK3F8lo9Rme', 1);//password: password-1234
commit;
insert into users(user_id, username, password, enabled) values(4, 'customer_2', '$2a$10$V1DRG8Za2Hb97lEgD7dGyO89zMf24YnUSTYjl1lLlNEsMe21LK8Da', 1);//password: Password-4321
commit;

--user roles

insert into user_roles (user_role_id, authority) values(1, 'admin');
insert into user_roles (user_role_id, authority) values(2, 'not_destructive_actions_only');
insert into user_roles (user_role_id, authority) values(3, 'customer');
commit;

--users_user_roles

insert into users_roles(user_id, user_role_id) values(1, 1);
insert into users_roles(user_id, user_role_id) values(2, 2);
insert into users_roles(user_id, user_role_id) values(3, 3);
insert into users_roles(user_id, user_role_id) values(4, 3);
commit;

--USER_ROLES_USERS

/*
insert into USER_ROLES_USERS(USER_ROLE_USER_ROLE_ID, USERS_USER_ID) values(1, 1);
insert into USER_ROLES_USERS(USER_ROLE_USER_ROLE_ID, USERS_USER_ID) values(2, 2);
insert into USER_ROLES_USERS(USER_ROLE_USER_ROLE_ID, USERS_USER_ID) values(3, 3);
insert into USER_ROLES_USERS(USER_ROLE_USER_ROLE_ID, USERS_USER_ID) values(3, 4);
commit;*/

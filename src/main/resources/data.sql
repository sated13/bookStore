-- book categories

insert into book_categories (book_category_id, category) values (1, 'psychology in practice');
insert into book_categories (book_category_id, category) values (2, 'artificial intelligence');
insert into book_categories (book_category_id, category) values (3, 'software development');
insert into book_categories (book_category_id, category) values (4, 'medicine');
insert into book_categories (book_category_id, category) values (5, 'biography');
insert into book_categories (book_category_id, category) values (6, 'memoirs');
commit;

--COVERS

insert into COVERS (COVER_ID, IS_PRESENTED, FILE_NAME)
values (1, true, 'book_1.png');
insert into COVERS (COVER_ID, IS_PRESENTED, FILE_NAME)
values (2, true, 'book_2.png');
insert into COVERS (COVER_ID, IS_PRESENTED, FILE_NAME)
values (3, true, 'book_3.png');
insert into COVERS (COVER_ID, IS_PRESENTED, FILE_NAME)
values (4, true, 'book_4.png');
insert into COVERS (COVER_ID, IS_PRESENTED, FILE_NAME)
values (5, true, 'book_5.png');
insert into COVERS (COVER_ID, IS_PRESENTED, FILE_NAME)
values (6, true, 'book_6.png');
insert into COVERS (COVER_ID, IS_PRESENTED, FILE_NAME)
values (7, true, 'book_7.png');
commit;

--books

insert into books (book_id, book_title, PUBLISHING_HOUSE, number_of_copies, number_of_pages, year, price, adding_day, count_of_sold_items, description, PICTURE_OF_BOOK_COVER_COVER_ID)
values (1, 'Сквозь глубины. Рассказ мальчика из Бухенвальда, который наконец вернулся домой', 'Феникс', 2000, 372, 2015,
  22.33,
  'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e2030b78', 59,
  'Книга рассказывает о детстве раввина Исраэля Меира Лау, пришедшемся на годы войны, о выпавших на его долю испытаниях, спасении из Бухенвальда и дальнейшей репатриации в Эрец-Исраэль. Вчерашний узник лагеря смерти смог пойти по стопам своих предков и стал раввином. С течением времени он занял самый высокий духовный пост - главного раввина Израиля. По роду своей деятельности он встречался со многими религиозными и государственными лидерами, принимая участие в решении важных вопросов на самом высоком уровне. Подробности его невероятной судьбы вы сможете узнать, прочитав эту книгу.',
  1);
insert into books (book_id, book_title, PUBLISHING_HOUSE, number_of_copies, number_of_pages, year, price, adding_day, count_of_sold_items, description, PICTURE_OF_BOOK_COVER_COVER_ID)
values (2, 'Илон Маск. Tesla, SpaceX и дорога в будущее', 'Олимп-Бизнес', 0, 416, 2017, 23,
  'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e2040978', 13,
  'В книге "Илон Маск: Tesla, SpaceX и дорога в будущее" известный технологический журналист Эшли Вэнс впервые предлагает независимый и разносторонний взгляд на жизнь и достижения самого отважного предпринимателя Кремниевой долины. Основанная на эксклюзивных интервью с Маском, членами его семьи и друзьями, книга содержит интригующую историю его жизни - от детства в Южной Африке и до нынешнего положения на пике глобального бизнеса.В третье издание книги вошли новые факты биографии Илона Маска.

Книга будет интересна тем, кого занимает природа лидерства, кто следит за развитием новых технологий, а также любителям качественной биографической литературы.',
  2);
insert into books (book_id, book_title, PUBLISHING_HOUSE, number_of_copies, number_of_pages, year, price, adding_day, count_of_sold_items, description, PICTURE_OF_BOOK_COVER_COVER_ID)
values (3, 'Геномы', 'Институт компьютерных исследований', 0, 944, 2011, 48,
  'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e2040978', 1,
  'Предлагаемая книга является первым наиболее полным и авторитетным руководством по интенсивно развивающейся области науки - молекулярной генетике, аналогов которому в мировой научной литературе нет. Издание охватывает молекулярную генетику от самых основ до экпрессии генома и молекулярной филогении. Изложение сопровождается огромным количеством цветных рисунков, в конце каждой главы приводятся задачи и вопросы, а также библиографический список.
Книга предназначена для студентов, аспирантов и исследователей в области молекулярной биологии, генетики и биоинформатики, а также всех специалистов, работающих в смежных с биологией и медициной областях.',
  3);
insert into books (book_id, book_title, PUBLISHING_HOUSE, number_of_copies, number_of_pages, year, price, adding_day, count_of_sold_items, description, PICTURE_OF_BOOK_COVER_COVER_ID)
values (4, 'Совершенный код. Мастер-класс', 'БХВ-Петербург', 0, 896, 2017, 20,
  'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e2040978', 61,
  'Более 10 лет первое издание этой книги считалось одним из лучших практических руководств по программированию. Сейчас эта книга полностью обновлена с учетом современных тенденций и технологий и дополнена сотнями новых примеров, иллюстрирующих искусство и науку программирования. Опираясь на академические исследования, с одной стороны, и практический опыт коммерческих разработок ПО - с другой, автор синтезировал из самых эффективных методик и наиболее эффективных принципов ясное прагматичное руководство. Каков бы ни был ваш профессиональный уровень, с какими бы средствами разработки вы ни работали, какова бы ни была сложность вашего проекта, в этой книге вы найдете нужную информацию, она заставит вас размышлять и поможет создать совершенный код.
Книга состоит из 35 глав, предметного указателя и библиографии.',
  4);
insert into books (book_id, book_title, PUBLISHING_HOUSE, number_of_copies, number_of_pages, year, price, adding_day, count_of_sold_items, description, PICTURE_OF_BOOK_COVER_COVER_ID)
values (5, 'Чистая архитектура. Искусство разработки программного обеспечения', 'Питер', 2000, 352, 2018, 15,
  'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e2040978', 155,
  '"Идеальный программист" и "Чистый код" - легендарные бестселлеры Роберта Мартина - рассказывают, как достичь высот профессионализма. "Чистая архитектура" продолжает эту тему, но не предлагает несколько вариантов в стиле "решай сам", а объясняет, что именно следует делать, по какой причине и почему именно такое решение станет принципиально важным для вашего успеха.
Роберт Мартин дает прямые и лаконичные ответы на ключевые вопросы архитектуры и дизайна. "Чистую архитектуру" обязаны прочитать разработчики всех уровней, системные аналитики, архитекторы и каждый программист, который желает подняться по карьерной лестнице или хотя бы повлиять на людей, которые занимаются данной работой.
Все архитектуры подчиняются одним и тем же правилам!
Роберт Мартин (дядюшка Боб)',
  5);
insert into books (book_id, book_title, PUBLISHING_HOUSE, number_of_copies, number_of_pages, year, price, adding_day, count_of_sold_items, description, PICTURE_OF_BOOK_COVER_COVER_ID)
values (6, 'Создаем нейронную сеть', 'Питер', 0, 272, 2017, 24.75,
  'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e2040978', 155,
  'Эта книга представляет собой введение в теорию и практику создания нейронных сетей. Она предназначена для тех, кто хочет узнать, что такое нейронные сети, где они применяются и как самому создать такую сеть, не имея опыта работы в данной области. Автор простым и понятным языком объясняет теоретические аспекты, знание которых необходимо для понимания принципов функционирования нейронных сетей и написания соответствующих программных инструкций. Изложение материала сопровождается подробным описанием процедуры поэтапного создания полностью функционального кода, который реализует нейронную сеть на языке Python и способен выполняться даже на таком миниатюрном компьютере, как Raspberry Pi Zero.Основные темы книги:нейронные сети и системы искусственного интеллекта;
структура нейронных сетей;
сглаживание сигналов, распространяющихся по нейронной сети, с помощью функции активации;
тренировка и тестирование нейронных сетей;
интерактивная среда программирования IPython;
использование нейронных сетей в качестве классификаторов объектов;
распознавание образов с помощью нейронных сетей.Книга обсуждается в отдельном сообщении в блоге Виктора Штонда.Тарик Рашид - специалист в области количественного анализа данных и разработки решений на базе продуктов с открытым исходным кодом.Имеет ученую степень по физике и степень магистра по специальности "Machine Learning and Data Mining". Проживая в Лондоне, он возглавляет местную группу разработчиков Python (насчитывающую около 3000 участников), организует многочисленные семинары и часто выступает с докладами на международных конференциях.',
  6);
insert into books (book_id, book_title, PUBLISHING_HOUSE, number_of_copies, number_of_pages, year, price, adding_day, count_of_sold_items, description, PICTURE_OF_BOOK_COVER_COVER_ID)
values (7, 'Хочу и буду. Принять себя, полюбить жизнь и стать счастливым', 'Альпина Паблишер', 0, 320, 2017, 9,
  'aced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e2040978', 155,
  'Психолог Михаил Лабковский абсолютно уверен, что человек может и имеет право быть счастливым и делать только то, что он хочет. Его книга о том, как понять себя, обрести гармонию и научиться радоваться жизни. Автор исследует причины, препятствующие психически здоровому образу жизни: откуда в нас осознанные и бессознательные тревоги, страхи, неумение слушать себя и строить отношения с другими людьми?
Отличительная черта подхода Лабковского - в конкретике. На любой самый сложный вопрос он всегда дает предельно доходчивый ответ. Его заявления и советы настолько радикальны, что многим приходится сначала испытать удивление, если не шок. В рекомендациях автор не прячется за обтекаемыми формулировками, а четко называет причины проблем. И самое главное, что он знает, как эту проблему решить - без копания в детских психотравмах и пристального анализа вашего прошлого. Если у человека есть знание и желание, то изменить себя и свою жизнь к лучшему вполне реально.
Цель любой работы психолога - личное счастье и благополучие его пациента. Цель издания этой книги - личное счастье каждого, кто ее прочитает.',
  7);
commit;

--book_categories

insert into books_categories (book_id, book_category_id) values (1, 6);
insert into books_categories (book_id, book_category_id) values (2, 5);
insert into books_categories (book_id, book_category_id) values (2, 6);
insert into books_categories (book_id, book_category_id) values (3, 4);
insert into books_categories (book_id, book_category_id) values (4, 3);
insert into books_categories (book_id, book_category_id) values (5, 3);
insert into books_categories (book_id, book_category_id) values (6, 2);
insert into books_categories (book_id, book_category_id) values (7, 1);
commit;

--BOOK_AUTHORS

insert into BOOK_AUTHORS (book_book_id, authors) values (1, 'Исраэль Лау');
insert into BOOK_AUTHORS (book_book_id, authors) values (2, 'Эшли Вэнс');
insert into BOOK_AUTHORS (book_book_id, authors) values (3, 'Терри А. Браун');
insert into BOOK_AUTHORS (book_book_id, authors) values (4, 'Стив Макконнелл');
insert into BOOK_AUTHORS (book_book_id, authors) values (5, 'Роберт Мартин');
insert into BOOK_AUTHORS (book_book_id, authors) values (6, 'Тарик Рашид');
insert into BOOK_AUTHORS (book_book_id, authors) values (7, 'Михаил Лабковский');
commit;

--users

insert into users (user_id, username, password, enabled)
values (1, 'admin', '$2a$10$1HUIfBS2OWXwqNLjBmljEueZzAtrgfoUDn7TSdFf8rryjlhYvKcfS', 1); //password: adminishe
commit;
INSERT INTO users (user_id, username, password, enabled)
values (2, 'test', '$2a$10$JUR6XWi.KIixW0lFSevj7OWosw0DWOY9BtqZPtMf5DouJN7R8fHVS', 1); //test
commit;
insert into users (user_id, username, password, enabled)
values (3, 'customer_1', '$2a$10$.YpNXLtMLbRbw7hcbeVU3uq7tQp7gPZGtlwhQ1KNCIpK3F8lo9Rme', 1); //password: password-1234
commit;
insert into users (user_id, username, password, enabled)
values (4, 'customer_2', '$2a$10$V1DRG8Za2Hb97lEgD7dGyO89zMf24YnUSTYjl1lLlNEsMe21LK8Da', 1); //password: Password-4321
commit;

--user roles

insert into user_roles (user_role_id, authority) values (1, 'admin');
insert into user_roles (user_role_id, authority) values (2, 'not_destructive_actions_only');
insert into user_roles (user_role_id, authority) values (3, 'customer');
commit;

--users_user_roles

insert into users_roles (user_id, user_role_id) values (1, 1);
insert into users_roles (user_id, user_role_id) values (2, 2);
insert into users_roles (user_id, user_role_id) values (3, 3);
insert into users_roles (user_id, user_role_id) values (4, 3);
commit;
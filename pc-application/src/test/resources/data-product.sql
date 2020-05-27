use product;

--@formatter:on
--VALUES                     id name
INSERT INTO category VALUES(1, 'Novel');
INSERT INTO category VALUES(2, 'Religious');

--VALUES               id  title                   author           category_id  year   photo_url description available recommended price  num_pages cover_type   date_added
INSERT INTO book VALUES(1, 'Nineteen Eighty-Four', 'George Orwell', 1,           1949,  null,     null,       1,        1,          21.37, 345,      'HARDCOVER', '2020-05-01');
INSERT INTO book VALUES(2, 'Holy Bible',           'Unknown',       2,           null,  null,     null,       1,        0,          6.66,  69,       'PAPERBACK', '2019-12-12');
INSERT INTO book VALUES(3, 'Holyaaa Bible',        'Unknown',       2,           null,  null,     null,       1,        0,          6.66,  null,     'HARDCOVER', '2020-01-01');

--@formatter:off
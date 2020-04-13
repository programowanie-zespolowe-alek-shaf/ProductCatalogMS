use product;

--@formatter:on
--VALUES                     id name
INSERT INTO category VALUES(1, 'Novel');
INSERT INTO category VALUES(2, 'Religious');

--VALUES               id  title                   author           category_id  year   photo_url description available price
INSERT INTO book VALUES(1, 'Nineteen Eighty-Four', 'George Orwell', 1,           1949,  null,     null,       1,        21.37);
INSERT INTO book VALUES(2, 'Holy Bible',           'Unknown',       2,           null,  null,     null,       1,        6.66);
INSERT INTO book VALUES(3, 'Holyaaa Bible',        'Unknown',       2,           null,  null,     null,       1,        6.66);

--@formatter:off
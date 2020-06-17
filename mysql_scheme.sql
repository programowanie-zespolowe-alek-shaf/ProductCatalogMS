DROP SCHEMA IF EXISTS product;
CREATE SCHEMA IF NOT EXISTS product;

create user 'product'@'%' identified by 'product';
grant all on product.* to 'product'@'%';

USE product;

CREATE TABLE category (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY  NOT NULL,
  name        VARCHAR(70) NOT NULL UNIQUE
);

CREATE TABLE book (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY  NOT NULL,
  title       VARCHAR(255) NOT NULL,
  author      VARCHAR(255) NOT NULL,
  category_id BIGINT(20) NOT NULL,
  year        INT,
  photo_url   VARCHAR(255),
  description VARCHAR(255),
  available   TINYINT(1) NOT NULL DEFAULT '1',
  recommended TINYINT(1) NOT NULL DEFAULT '0',
  price       DECIMAL(10, 2) NOT NULL,
  num_pages   INT,
  cover_type  VARCHAR(255) NOT NULL,
  date_added  DATE NOT NULL,
  FOREIGN KEY (category_id) REFERENCES category(id)
);
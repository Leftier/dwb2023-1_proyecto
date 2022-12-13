/****************************************************
 *                                                  *
 * Creamos tablas para el microservicio de customer *
 *                                                  *
 ****************************************************/
CREATE DATABASE IF NOT EXISTS `dwb2023-1_customer`;
USE `dwb2023-1_customer`;

DROP TABLE IF EXISTS customer_image;

CREATE TABLE customer_image
(
    customer_image_id INT  NOT NULL AUTO_INCREMENT,
    customer_image    TEXT NOT NULL,
    PRIMARY KEY (customer_image_id)
);

DROP TABLE IF EXISTS region;

CREATE TABLE region
(
    region_id INT                 NOT NULL AUTO_INCREMENT,
    region    VARCHAR(100) UNIQUE NOT NULL,
    status    TINYINT             NOT NULL,
    PRIMARY KEY (region_id)
);

DROP TABLE IF EXISTS customer;

CREATE TABLE customer
(
    customer_id       INT                 NOT NULL AUTO_INCREMENT,
    name              VARCHAR(100)        NOT NULL,
    surname           VARCHAR(100)        NOT NULL,
    date_birth        DATE                NOT NULL,
    rfc               VARCHAR(13) UNIQUE  NOT NULL,
    mail              VARCHAR(100) UNIQUE NOT NULL,
    address           VARCHAR(255),
    region_id         INT                 NOT NULL,
    customer_image_id INT                 NOT NULL,
    status            TINYINT             NOT NULL,
    PRIMARY KEY (customer_id),
    FOREIGN KEY (region_id) REFERENCES region (region_id),
    FOREIGN KEY (customer_image_id) REFERENCES customer_image (customer_image_id)
);

/***************************************************
 *                                                 *
 * Creamos tablas para el microservicio de product *
 *                                                 *
 ***************************************************/
CREATE DATABASE IF NOT EXISTS `dwb2023-1_product`;
USE `dwb2023-1_product`;

DROP TABLE IF EXISTS product;

CREATE TABLE product
(
    product_id  INT                 NOT NULL AUTO_INCREMENT,
    gtin        CHAR(13) UNIQUE     NOT NULL,
    product     VARCHAR(100) UNIQUE NOT NULL,
    description VARCHAR(255),
    price       FLOAT               NOT NULL,
    stock       INT                 NOT NULL,
    category_id INT                 NOT NULL,
    status      TINYINT             NOT NULL,
    PRIMARY KEY (product_id),
    FOREIGN KEY (category_id) REFERENCES category (category_id)
);

DROP TABLE IF EXISTS category;

CREATE TABLE category
(
    category_id INT                 NOT NULL AUTO_INCREMENT,
    category    VARCHAR(100) UNIQUE NOT NULL,
    status      TINYINT             NOT NULL,
    PRIMARY KEY (category_id)
);

DROP TABLE IF EXISTS product_image;

CREATE TABLE product_image
(
    product_image_id INT     NOT NULL AUTO_INCREMENT,
    product_id       INT     NOT NULL,
    image            TEXT    NOT NULL,
    status           TINYINT NOT NULL,
    PRIMARY KEY (product_image_id),
    FOREIGN KEY (product_id) REFERENCES product (product_id)
);

/***************************************************
 *                                                 *
 * Creamos tablas para el microservicio de invoice *
 *                                                 *
 ***************************************************/
CREATE DATABASE IF NOT EXISTS `dwb2023-1_invoice`;
USE `dwb2023-1_invoice`;

DROP TABLE IF EXISTS invoice;

CREATE TABLE invoice
(
    invoice_id INT         NOT NULL AUTO_INCREMENT,
    rfc        VARCHAR(13) NOT NULL,
    subtotal   FLOAT       NOT NULL,
    taxes      FLOAT       NOT NULL,
    total      FLOAT       NOT NULL,
    created_at DATE        NOT NULL,
    status     TINYINT     NOT NULL,
    PRIMARY KEY (invoice_id)
);

DROP TABLE IF EXISTS item;

CREATE TABLE item
(
    item_id    INT      NOT NULL AUTO_INCREMENT,
    invoice_id INT      NOT NULL,
    gtin       CHAR(13) NOT NULL,
    quantity   INT      NOT NULL,
    unit_price FLOAT    NOT NULL,
    subtotal   FLOAT    NOT NULL,
    taxes      FLOAT    NOT NULL,
    total      FLOAT    NOT NULL,
    status     TINYINT  NOT NULL,
    PRIMARY KEY (item_id),
    FOREIGN KEY (invoice_id) REFERENCES invoice (invoice_id)
);

DROP TABLE IF EXISTS cart;

CREATE TABLE cart
(
    cart_id  INT         NOT NULL AUTO_INCREMENT,
    rfc      VARCHAR(13) NOT NULL,
    gtin     CHAR(13)    NOT NULL,
    quantity INT         NOT NULL,
    status   TINYINT     NOT NULL,
    PRIMARY KEY (cart_id)
);
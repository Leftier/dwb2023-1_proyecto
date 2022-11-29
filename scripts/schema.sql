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

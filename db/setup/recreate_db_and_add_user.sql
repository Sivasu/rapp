DROP DATABASE IF EXISTS rapp;

CREATE DATABASE rapp;

DROP USER 'rappuser'@'localhost';

CREATE USER 'rappuser'@'localhost' IDENTIFIED BY 'rapp123';
GRANT ALL ON rapp.* TO 'rappuser'@'localhost';

FLUSH PRIVILEGES;


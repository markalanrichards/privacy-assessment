CREATE SCHEMA server;
CREATE USER 'privacy'@'localhost' IDENTIFIED BY 'privacy';
CREATE USER 'privacy'@'%' IDENTIFIED BY 'privacy';
GRANT ALL ON *.* TO 'privacy'@'localhost';
GRANT ALL ON *.* TO 'privacy'@'%';
FLUSH PRIVILEGES;
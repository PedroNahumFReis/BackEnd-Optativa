CREATE TABLE tb_password_recover (
                                     id BIGINT NOT NULL AUTO_INCREMENT,
                                     token VARCHAR(255) NOT NULL,
                                     email VARCHAR(255) NOT NULL,
                                     expiration DATETIME NOT NULL,
                                     PRIMARY KEY (id)
);
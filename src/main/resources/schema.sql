CREATE TABLE IF NOT EXISTS Staff(
                                    staff_id VARCHAR(100) PRIMARY KEY ,
                                    full_name VARCHAR(100) NOT NULL,
                                    address VARCHAR(100) NOT NULL,
                                    contact VARCHAR(100) NOT NULL,
                                    user_name VARCHAR(100) NOT NULL ,
                                    password VARCHAR(100) NOT NULL,
                                    gender ENUM('MALE','FEMALE'),
                                    status ENUM('OWNER','CASHIER')


);

CREATE TABLE IF NOT EXISTS Picture(
                                      picture MEDIUMBLOB NOT NULL ,
                                      id VARCHAR(20) PRIMARY KEY ,
                                      CONSTRAINT fk_id FOREIGN KEY (id) REFERENCES Staff(staff_id)
);

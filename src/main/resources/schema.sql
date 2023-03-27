CREATE TABLE IF NOT EXISTS Staff(
                                    staff_id INT PRIMARY KEY AUTO_INCREMENT ,
                                    full_name VARCHAR(100) NOT NULL,
                                    user_name VARCHAR(100) NOT NULL ,
                                    password VARCHAR(100) NOT NULL,
                                    status ENUM('OWNER','CASHIER')


);
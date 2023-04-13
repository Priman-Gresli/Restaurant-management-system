CREATE TABLE IF NOT EXISTS Staff
(
    staff_id  VARCHAR(100) PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    address   VARCHAR(100) NOT NULL,
    contact   VARCHAR(100) NOT NULL,
    user_name VARCHAR(100) NOT NULL,
    password  VARCHAR(100) NOT NULL,
    gender    ENUM ('MALE','FEMALE'),
    status    ENUM ('OWNER','CASHIER')
);

CREATE TABLE IF NOT EXISTS Picture
(
    picture MEDIUMBLOB NOT NULL,
    id      VARCHAR(20) PRIMARY KEY,
    CONSTRAINT fk_id FOREIGN KEY (id) REFERENCES Staff (staff_id)
);


CREATE TABLE IF NOT EXISTS Item
(
    item_id  VARCHAR(100) PRIMARY KEY,
    category VARCHAR(100) NOT NULL,
    name     VARCHAR(100) NOT NULL,
    size     ENUM ('SMALL','LARGE'),
    prize    DOUBLE    NOT NULL
);

CREATE TABLE IF NOT EXISTS Bill
(
    bill_id     INT PRIMARY KEY AUTO_INCREMENT,
    bill_date   VARCHAR(100)NOT NULL,
    bill_time   VARCHAR(100)NOT NULL,
    cashier     VARCHAR(100)NOT NULL,
    total_prize DOUBLE NOT NULL


);
CREATE TABLE IF NOT EXISTS BillItems
(
    id         INT          NOT NULL,
    item_name  VARCHAR(100) NOT NULL,
    item_qty   INT          NOT NULL,
    category   VARCHAR(100) NOT NULL,
    item_size  ENUM ('SMALL','LARGE'),
    item_prize DOUBLE          NOT NULL,
    CONSTRAINT pk_contact PRIMARY KEY (item_name, category, item_size, id),
    CONSTRAINT fk_id2 FOREIGN KEY (id) REFERENCES Bill (bill_id)
);


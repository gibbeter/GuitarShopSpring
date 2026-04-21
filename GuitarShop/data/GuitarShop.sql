CREATE TABLE `Product` (
  `prod_id` integer PRIMARY KEY AUTO_INCREMENT,
  `stock` integer,
  `price` integer,
  `type`integer,
  `product_name` varchar(255),
  `product_desc` TEXT
);

CREATE TABLE `Overview` (
    `user_id` integer,
    `user_name` varchar(255),
    `product_id` integer,
    `text` varchar(255),
    `rating` integer,
    primary key(user_id, product_id)
);

CREATE TABLE `Type` (
  `type_id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255)
);

CREATE TABLE `User` (
  `user_id` integer PRIMARY KEY AUTO_INCREMENT,
  `type` varchar(5) COMMENT 'admin, user, guest',
  `user_name` varchar(255),
  `password` varchar(255),
  `name` varchar(255),
  `surname` varchar(255),
  `phone_number` integer,
  `user_mail` varchar(255)
);

CREATE TABLE `Cart` (
  `cart_id` integer PRIMARY KEY AUTO_INCREMENT,
  `user_id` integer UNIQUE,
  `summ` integer
);

CREATE TABLE `CartItem` (
  `cart_id` integer,
  `prod_id` integer,
  `quantity` integer,
  primary key(cart_id,prod_id) 
);

CREATE TABLE `PurchaseOrder` (
    `order_id` integer PRIMARY KEY AUTO_INCREMENT,
    `order_status` varchar(8) COMMENT 'new, shipping, complete',
	`order_date` datetime,
    `user_id` integer,
    `name` varchar(255),
    `surname` varchar(255),
    `phone_number` integer,
    `order_type` varchar(2) COMMENT 'PU:pick-up, SP:shipping',
    `pu_adress` varchar(255),
    `sp_adress` varchar(255),
    `comp_time` datetime COMMENT 'estimated copmletion time',
    `summ` integer
);

CREATE TABLE `OrderItem` (
    `order_id` integer,
    `prod_id` integer,
    `quantity` integer,
    `price` integer,
    primary key(order_id, prod_id)
);

CREATE TABLE `Chat` (
  `chat_id` integer PRIMARY KEY AUTO_INCREMENT,
  `user1_id` integer,
  `user2_id` integer,
  `user1_username` varchar(255),
  `user2_username` varchar(255)
);

CREATE TABLE `Message` (
  `message_id` integer PRIMARY KEY AUTO_INCREMENT,
  `sender_id` integer,
  `chat_id` integer,
  `message_text` varchar(255)
);

CREATE TABLE `StoreAdress` (
	`store_id` integer PRIMARY KEY AUTO_INCREMENT,
    `store_adress` varchar(255)
);

ALTER TABLE `Cart` ADD FOREIGN KEY (`user_id`) REFERENCES `User` (`user_id`);

ALTER TABLE `CartItem` ADD FOREIGN KEY (`cart_id`) REFERENCES `Cart` (`cart_id`);

ALTER TABLE `CartItem` ADD FOREIGN KEY (`prod_id`) REFERENCES `Product` (`prod_id`);

ALTER TABLE `Chat` ADD FOREIGN KEY (`user1_id`) REFERENCES `User` (`user_id`);

ALTER TABLE `Chat` ADD FOREIGN KEY (`user2_id`) REFERENCES `User` (`user_id`);

ALTER TABLE `Message` ADD FOREIGN KEY (`chat_id`) REFERENCES `Chat` (`chat_id`);

ALTER TABLE `Product` ADD FOREIGN KEY (`type`) REFERENCES `Type` (`type_id`);

ALTER TABLE `PurchaseOrder` ADD FOREIGN KEY (`user_id`) REFERENCES `User` (`user_id`);

ALTER TABLE `OrderItem` ADD FOREIGN KEY (`order_id`) REFERENCES `PurchaseOrder` (`order_id`);

-- ALTER TABLE `purchaseorder` ADD `name` varchar(255);
-- ALTER TABLE `purchaseorder` ADD `surname` varchar(255);
-- ALTER TABLE `purchaseorder` ADD `phone_number` integer;
CREATE TABLE `Product` (
  `prod_id` integer PRIMARY KEY AUTO_INCREMENT,
  `stok` integer,
  `type`integer,
  `product_name` varchar(255),
  `product_desc` varchar(255)
);

CREATE TABLE `Type` (
  `type_id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255)
);

CREATE TABLE `User` (
  `user_id` integer PRIMARY KEY AUTO_INCREMENT,
  `type` varchar(255) COMMENT 'admin, user, guest',
  `user_name` varchar(255),
  `password` varchar(255),
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

CREATE TABLE `Chat` (
  `chat_id` integer PRIMARY KEY AUTO_INCREMENT,
  `user1_id` integer,
  `user2_id` integer
);

CREATE TABLE `Message` (
  `message_id` integer PRIMARY KEY AUTO_INCREMENT,
  `chat_id` integer,
  `message_text` varchar(255)
);

ALTER TABLE `Cart` ADD FOREIGN KEY (`user_id`) REFERENCES `User` (`user_id`);

ALTER TABLE `CartItem` ADD FOREIGN KEY (`cart_id`) REFERENCES `Cart` (`cart_id`);

ALTER TABLE `CartItem` ADD FOREIGN KEY (`prod_id`) REFERENCES `Product` (`prod_id`);

ALTER TABLE `Chat` ADD FOREIGN KEY (`user1_id`) REFERENCES `User` (`user_id`);

ALTER TABLE `Chat` ADD FOREIGN KEY (`user2_id`) REFERENCES `User` (`user_id`);

ALTER TABLE `Message` ADD FOREIGN KEY (`chat_id`) REFERENCES `Chat` (`chat_id`);

ALTER TABLE `Product` ADD FOREIGN KEY (`type`) REFERENCES `Type` (`type_id`);

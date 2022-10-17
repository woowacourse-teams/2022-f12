DROP TABLE IF EXISTS `following`;
CREATE TABLE `following`
(
    `id`           bigint NOT NULL AUTO_INCREMENT,
    `created_at`   datetime(6) NOT NULL,
    `follower_id`  bigint NOT NULL,
    `following_id` bigint NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `following_following_id_follower_id_unique` (`following_id`,`follower_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `inventory_product`;
CREATE TABLE `inventory_product`
(
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `selected`   bit(1) DEFAULT 0,
    `member_id`  bigint NOT NULL DEFAULT NULL,
    `product_id` bigint NOT NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `inventory_member_product_unique` (`member_id`,`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`
(
    `id`             bigint        NOT NULL AUTO_INCREMENT,
    `career_level`   varchar(255) DEFAULT NULL,
    `follower_count` int           NOT NULL DEFAULT 0,
    `github_id`      varchar(255)  NOT NULL,
    `image_url`      varchar(15000) NOT NULL,
    `job_type`       varchar(255) DEFAULT NULL,
    `name`           varchar(255) DEFAULT NULL,
    `registered`     bit(1)        NOT NULL DEFAULT 0,
    `role`           varchar(255)  NOT NULL DEFAULT 'USER',
    PRIMARY KEY (`id`),
    UNIQUE KEY `github_id_unique` (`github_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`
(
    `id`           bigint         NOT NULL AUTO_INCREMENT,
    `category`     varchar(255)     NOT NULL,
    `image_url`    varchar(15000) NOT NULL,
    `name`         varchar(255)   NOT NULL,
    `avg_rating`   double         NOT NULL DEFAULT 0,
    `review_count` int            NOT NULL DEFAULT 0,
    `total_rating` int            NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `NAME_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `review`;
CREATE TABLE `review`
(
    `id`         bigint        NOT NULL AUTO_INCREMENT,
    `content`    varchar(1000) NOT NULL,
    `created_at` datetime(6) NOT NULL,
    `rating`     int           NOT NULL,
    `member_id`  bigint        NOT NULL,
    `product_id` bigint        NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `review_product_id_member_id_unique` (`product_id`,`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `refresh_token`;
create table `refresh_token`
(
    `token_value` varchar(50) primary key,
    `expired_at`  timestamp not null,
    `member_id`   bigint    not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

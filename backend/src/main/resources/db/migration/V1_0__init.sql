CREATE TABLE IF NOT EXISTS `following`
(
    `id`           bigint    NOT NULL AUTO_INCREMENT,
    `created_at`   TIMESTAMP NOT NULL,
    `follower_id`  bigint    NOT NULL,
    `following_id` bigint             DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `following_following_id_follower_id_unique` (`follower_id`, `following_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `inventory_product`
(
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `selected`   TINYINT(1) DEFAULT NULL,
    `member_id`  bigint     DEFAULT NULL,
    `product_id` bigint     DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `inventory_member_product_unique` (`member_id`, `product_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `member`
(
    `id`             bigint         NOT NULL AUTO_INCREMENT,
    `career_level`   varchar(255)            DEFAULT NULL,
    `follower_count` int            NOT NULL DEFAULT 0,
    `github_id`      varchar(255)   NOT NULL,
    `image_url`      varchar(15000) NOT NULL,
    `job_type`       varchar(255)            DEFAULT NULL,
    `name`           varchar(255)            DEFAULT NULL,
    `registered`     TINYINT(1)     NOT NULL DEFAULT 0,
    `role`           varchar(255)   NOT NULL DEFAULT 'USER',
    PRIMARY KEY (`id`),
    UNIQUE KEY `github_id_unique` (`github_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `product`
(
    `id`           bigint         NOT NULL AUTO_INCREMENT,
    `category`     varchar(255)            DEFAULT NULL,
    `image_url`    varchar(15000) NOT NULL,
    `name`         varchar(255)   NOT NULL,
    `avg_rating`   double         NOT NULL DEFAULT 0,
    `review_count` int            NOT NULL DEFAULT 0,
    `total_rating` int            NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `NAME_UNIQUE` (`name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `review`
(
    `id`         bigint        NOT NULL AUTO_INCREMENT,
    `content`    varchar(1000) NOT NULL,
    `created_at` TIMESTAMP     NOT NULL,
    `rating`     int           NOT NULL,
    `member_id`  bigint        NOT NULL,
    `product_id` bigint        NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `review_product_id_member_id_unique` (`member_id`, `product_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `refresh_token`
(
    `token_value` varchar(50) primary key,
    `expired_at`  TIMESTAMP not null,
    `member_id`   bigint    not null
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

alter table inventory_product add constraint inventory_product_ibfk_1 foreign key (member_id) references member (id);
alter table inventory_product add constraint inventory_product_ibfk_2 foreign key (product_id) references product (id);

alter table review add constraint review_ibfk_1 foreign key (member_id) references member (id);
alter table review add constraint review_ibfk_2 foreign key (product_id) references product (id);

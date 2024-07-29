DROP DATABASE IF EXISTS one_pass_zwq;

CREATE DATABASE one_pass_zwq DEFAULT CHARACTER SET utf8;

USE one_pass_zwq;

CREATE TABLE `user_balance` (
                                `id` BIGINT NOT NULL COMMENT '用户ID',
                                `balance_amount` DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '账户余额',
--                                 `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户余额表';

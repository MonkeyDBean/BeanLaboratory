CREATE DATABASE IF NOT EXISTS `security_test` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
use `security_test`;

-- 账户表 --
CREATE TABLE IF NOT EXISTS `account` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_name` varchar(50) NOT NULL COMMENT '用户名, 该字段值需考虑与其他账户储存方式的区分，如idap, 数据库字段存储值可加统一前缀',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `phone` varchar(11) COMMENT '手机号',
  `email` VARCHAR(64) COMMENT '邮箱地址',
  `enabled` BIT(1) NOT NULL DEFAULT b'0' COMMENT '0标识不可用, 1标识可用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`user_name`),
  UNIQUE KEY (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账户表';

-- 表 update_version 结构
CREATE TABLE IF NOT EXISTS `update_version` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增索引',
  `update_version` varchar(255) NOT NULL COMMENT '更新数据库版本号，按数字自增规则， 初始为1',
  `update_log` varchar(255) NOT NULL COMMENT '更新日志',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `update_version` (`update_version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据库更新记录表，每次更新插入一条记录';

-- 更新日志 --
INSERT INTO `update_version`(`update_version`, `update_log`)
  SELECT '1', '数据库初始化'
  FROM DUAL
  WHERE NOT EXISTS(
      SELECT `update_version`
      FROM `update_version`
      WHERE `update_version` = '1');
-- 1: database monkey_test1 --
CREATE DATABASE IF NOT EXISTS `monkey_test1` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `monkey_test1`;

-- 表 test_record --
CREATE TABLE IF NOT EXISTS `test_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增索引',
  `record_id` varchar(50) NOT NULL COMMENT '记录Id',
  `record_data` varchar(255) NOT NULL DEFAULT 'test' COMMENT '记录数据',
  `create_time` datetime NOT NULL COMMENT '记录创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `record_id` (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='测试记录表';


-- 2: database monkey_test2 --
CREATE DATABASE IF NOT EXISTS `monkey_test2` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `monkey_test2`;

-- 表 test_record --
CREATE TABLE IF NOT EXISTS `test_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增索引',
  `record_id` varchar(50) NOT NULL COMMENT '记录Id',
  `record_data` varchar(255) NOT NULL DEFAULT 'test' COMMENT '记录数据',
  `create_time` datetime NOT NULL COMMENT '记录创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `record_id` (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='测试记录表';


-- 3: database monkey_test3 --
CREATE DATABASE IF NOT EXISTS `monkey_test3` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `monkey_test3`;

-- 表 test_record --
CREATE TABLE IF NOT EXISTS `test_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增索引',
  `record_id` varchar(50) NOT NULL COMMENT '记录Id',
  `record_data` varchar(255) NOT NULL DEFAULT 'test' COMMENT '记录数据',
  `create_time` datetime NOT NULL COMMENT '记录创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `record_id` (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='测试记录表';
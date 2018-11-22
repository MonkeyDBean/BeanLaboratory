CREATE DATABASE IF NOT EXISTS `monkey_test`;
USE `monkey_test`;

CREATE TABLE IF NOT EXISTS `test_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增索引',
  `record_id` varchar(50) NOT NULL COMMENT '记录Id',
  `record_data` varchar(50) NOT NULL DEFAULT 'test' COMMENT '记录数据',
  `create_time` datetime NOT NULL COMMENT '记录创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `record_id` (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='测试记录表';
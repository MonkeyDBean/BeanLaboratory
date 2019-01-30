CREATE DATABASE IF NOT EXISTS `schedule_test` DEFAULT CHARACTER SET utf8;
USE `schedule_test`;

-- 表 task_record --
CREATE TABLE IF NOT EXISTS `task_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增索引',
  `task_id` varchar(50) NOT NULL COMMENT '任务Id',
  `node_uuid` varchar(50) NOT NULL COMMENT '服务节点唯一标识',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='定时任务记录表';

-- 表 actual_task_record --
CREATE TABLE IF NOT EXISTS `actual_task_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增索引',
  `task_id` varchar(50) NOT NULL COMMENT '任务Id',
  `node_uuid` varchar(50) NOT NULL COMMENT '服务节点唯一标识',
  `custom` varchar(255) NOT NULL COMMENT '自定义数据',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='实际执行的定时任务记录表';
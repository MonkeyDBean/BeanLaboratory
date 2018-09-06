CREATE DATABASE IF NOT EXISTS `monkey_chat`;
USE `monkey_chat`;

-- 表 chat_record --
CREATE TABLE IF NOT EXISTS `chat_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增索引',
  `user_name` varchar(255) NOT NULL COMMENT '用户名',
  `message_type` int(2) unsigned NOT NULL DEFAULT '0' COMMENT '消息类型, 聊天为0，加入房间为1，离开房间为2',
  `message_content` varchar(4096) NOT NULL COMMENT '消息内容',
  `create_time` datetime NOT NULL COMMENT '记录生成时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='聊天记录表';

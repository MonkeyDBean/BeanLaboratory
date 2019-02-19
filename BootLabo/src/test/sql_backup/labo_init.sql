-- 数据库若支持表情符号，编码方式设为utf8mb4, 否则设置为utf8_general_ci即可 --
CREATE DATABASE IF NOT EXISTS `monkey_labo` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `monkey_labo`;

-- 表 account --
CREATE TABLE IF NOT EXISTS `account` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增索引',
  `account_id` int(10) unsigned NOT NULL COMMENT '账户Id',
  `phone` char(11) NOT NULL COMMENT '用户手机号，手机号长度限定为11位',
  `password` varchar(256) NOT NULL COMMENT '密码',
  `nickname` varchar(20) NOT NULL COMMENT '用户昵称',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `avatar` mediumblob COMMENT '头像',
  `is_admin` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否为管理员',
  `ipv4` varchar(20) NOT NULL COMMENT '最近一次登录的ipv4地址',
  `login_time` datetime NOT NULL COMMENT '最近一次登录时间',
  `forbid_time` datetime NOT NULL DEFAULT '1970-01-01 08:00:00' COMMENT '封号到期时间',
  `forbid_cause` varchar(50) DEFAULT NULL COMMENT '封号原因',
  `create_time` datetime NOT NULL COMMENT '账号创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `account_id` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';

-- 表 message_record --
CREATE TABLE IF NOT EXISTS `message_record` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增索引',
  `phone` char(11) NOT NULL COMMENT '手机号',
  `message_code` varchar(6) NOT NULL COMMENT '短信验证码',
  `verify_status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '验证码是否已使用，已使用则标记为1',
  `res_code` varchar(50) DEFAULT NULL COMMENT '短信平台的状态码',
  `res_custom` varchar(50) DEFAULT NULL COMMENT '存储短信平台参数，阿里云短信平台则存储发送回执Id(BizId)',
  `create_time` datetime NOT NULL COMMENT '记录创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  KEY `phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='短信验证码记录表';

-- 表 asset_temp --
CREATE TABLE IF NOT EXISTS `asset_temp` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '索引',
  `account_id` int(10) unsigned NOT NULL COMMENT '账户Id',
  `file_name` varchar(128) NOT NULL COMMENT '名称',
  `cover` mediumblob NOT NULL COMMENT '图片',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `file_name` (`file_name`),
  KEY `phone` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='存储临时图片';

-- 表 image_info --
CREATE TABLE IF NOT EXISTS `image_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增索引',
  `account_id` int(10) unsigned NOT NULL COMMENT '账户Id',
  `file_name` varchar(50) NOT NULL COMMENT '文件名',
  `file_hash` char(32) NOT NULL COMMENT 'md5, 16进制hash值',
  `file_des` varchar(50) DEFAULT NULL COMMENT '图片描述',
  `store_path` varchar(255) NOT NULL COMMENT '资源存储路径',
  `access_path` varchar(255) NOT NULL COMMENT '资源访问地址',
  `is_share` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否共享，默认不共享，1为共享',
  `is_delete` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除，0为不删除，1为删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `account_id_file_hash` (`account_id`,`file_hash`),
  KEY `file_hash` (`file_hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='存储上传图片的信息表';

-- 表 other_project_info --
CREATE TABLE IF NOT EXISTS `other_project_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增索引',
  `project_type` int(2) unsigned NOT NULL DEFAULT '0' COMMENT '项目类型，默认0为个人项目，1为工具类网站，2为创意类网站，3为技术类网站',
  `project_name` varchar(50) NOT NULL COMMENT '名称',
  `project_url` varchar(255) NOT NULL COMMENT '链接',
  `project_image` varchar(255) DEFAULT NULL COMMENT '预览图',
  `project_des` varchar(255) DEFAULT NULL COMMENT '简介',
  `is_delete` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否为删除记录，1为标记删除',
  `create_time` datetime NOT NULL COMMENT '记录添加时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='其他项目展示表';

-- 表 update_version --
CREATE TABLE IF NOT EXISTS `update_version` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增索引',
  `update_version` varchar(255) NOT NULL COMMENT '更新数据库版本号，递增，初始为1',
  `update_log` varchar(255) NOT NULL COMMENT '更新日志',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `update_version` (`update_version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据库更新记录表';

-- 表 config_info --
CREATE TABLE IF NOT EXISTS `config_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增索引',
  `config_name` varchar(255) NOT NULL COMMENT '配置名称',
  `config_value` varchar(255) DEFAULT NULL COMMENT '属性值',
  `create_time` datetime NOT NULL COMMENT '记录创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配置信息表';

-- 更新日志 --
INSERT INTO `update_version`(`update_version`, `update_log`)
  SELECT '1', '数据库初始化'
  FROM DUAL
  WHERE NOT EXISTS(
      SELECT `update_version`
      FROM `update_version`
      WHERE `update_version` = '1');


-- 预置数据 --
-- 表 other_project_info --
INSERT INTO `other_project_info` (`id`, `project_type`, `project_name`, `project_url`, `project_image`, `project_des`, `create_time`, `update_time`) VALUES
	(1, 0, 'MonkeyBean Blog', 'http://monkeybean.cn/', NULL, NULL, '2018-07-07 16:37:20', '2018-07-07 16:38:56'),
	(2, 0, 'Survival Demo', 'http://monkeybean.cn/h5game_survivaldemo/', NULL, NULL, '2018-07-07 16:39:43', '2018-07-07 16:39:42'),
	(3, 0, 'Jump Rabbit', 'http://monkeybean.cn/h5game_pptu/', NULL, NULL, '2018-07-07 16:42:34', '2018-07-07 16:42:35'),
	(4, 0, 'Intelligent Clock', 'http://monkeybean.cn/h5game_clock/', NULL, NULL, '2018-07-07 16:44:39', '2018-07-07 16:44:40'),
	(5, 0, 'Flappy Bird', 'http://monkeybean.cn/h5game_flappybird/', NULL, NULL, '2018-07-07 16:47:46', '2018-07-07 16:49:26'),
	(6, 0, 'Plane Fight', 'http://monkeybean.cn/h5game_plane/', NULL, NULL, '2018-07-07 16:46:45', '2018-07-07 16:47:40'),
	(7, 0, 'Construct2 Test Spriter', 'http://monkeybean.cn/h5game_testspriter/', NULL, NULL, '2018-07-07 16:49:47', '2018-07-07 16:49:48'),
	(8, 1, '图片压缩TinyPNG', 'https://tinypng.com/', NULL, NULL, '2018-07-07 16:52:31', '2018-07-07 16:53:11'),
	(9, 1, '图床SMMS', 'https://sm.ms/', NULL, NULL, '2018-07-07 16:53:51', '2018-07-07 16:54:00'),
	(10, 1, '在线加解密Crpto-JS', 'http://tool.oschina.net/encrypt?type=2', NULL, NULL, '2018-07-07 16:57:53', '2018-07-07 16:57:54'),
	(11, 1, 'Json处理bejson', 'http://www.bejson.com/', NULL, NULL, '2018-07-07 16:59:06', '2018-07-07 16:59:07'),
	(12, 1, 'cron表达式生成器', 'http://cron.qqe2.com/', NULL, NULL, '2018-07-07 16:59:38', '2018-07-07 16:59:39'),
	(13, 1, '阿里矢量图iconfont', 'http://www.iconfont.cn/collections', NULL, NULL, '2018-07-07 17:00:37', '2018-07-07 17:00:38'),
	(14, 1, 'Unix时间戳', 'http://tool.chinaz.com/Tools/unixtime.aspx', NULL, NULL, '2018-07-07 17:02:11', '2018-07-07 17:02:12'),
	(15, 1, '代码可视化', 'http://www.pythontutor.com/', NULL, NULL, '2018-07-07 17:02:30', '2018-07-07 17:02:31'),
	(16, 1, '在线工具汇总', 'https://tool.lu/', NULL, NULL, '2018-07-07 17:05:55', '2018-07-07 17:21:39'),
	(17, 1, 'Css格式化', 'https://tool.lu/css', NULL, NULL, '2018-07-07 17:06:54', '2018-07-07 17:21:41'),
	(18, 1, 'favicon.ico生成', 'https://tool.lu/favicon/', NULL, NULL, '2018-07-07 17:14:11', '2018-07-07 17:21:43'),
	(19, 1, '后端代码在线运行', 'https://tool.lu/coderunner/', NULL, NULL, '2018-07-07 17:07:32', '2018-07-07 17:21:45'),
	(20, 1, 'Md5摘要', 'https://md5jiami.51240.com/', NULL, NULL, '2018-07-07 17:05:04', '2018-07-07 17:21:47'),
	(21, 1, 'Md5解密', 'https://www.somd5.com/', NULL, NULL, '2018-07-07 17:09:02', '2018-07-07 17:21:49'),
	(22, 1, '图标宝', 'http://ico.58pic.com/', NULL, NULL, '2018-07-07 17:11:42', '2018-07-07 17:21:51'),
	(23, 1, 'ASCII字符画绘制', 'http://asciiflow.com/', NULL, NULL, '2018-07-07 17:16:06', '2018-07-07 17:21:53'),
	(24, 1, 'ASCII字符画生成', 'http://y.qq.com/m/demo/ctools/charimg.html', NULL, NULL, '2018-07-07 17:18:05', '2018-07-07 17:21:54'),
	(25, 1, '汉字转字符字', 'http://life.chacuo.net/convertfont2char', NULL, NULL, '2018-07-07 17:19:06', '2018-07-07 17:21:56'),
	(26, 1, '艺术字生成', 'https://www.qt86.com/', NULL, NULL, '2018-07-07 17:19:30', '2018-07-07 17:22:20'),
	(27, 2, '程序猿电台', 'https://cmd.to/', NULL, NULL, '2018-07-07 17:22:34', '2018-07-07 17:22:35'),
	(28, 2, '绘图weavesilk', 'http://weavesilk.com/', NULL, NULL, '2018-07-07 17:23:40', '2018-07-02 17:23:41'),
	(29, 2, '色彩pick', 'http://nipponcolors.com/#bengara', NULL, NULL, '2018-07-07 17:24:13', '2018-07-07 17:24:13'),
	(30, 2, '假装是个黑客', 'https://www.iplaysoft.com/hackertyper.html', NULL, NULL, '2018-07-07 17:25:18', '2018-07-07 17:25:20'),
	(31, 2, '模拟黑客界面', 'http://www.geektyper.com/', NULL, NULL, '2018-07-07 17:25:18', '2018-07-07 17:25:20'),
	(32, 2, '假装是个病毒', 'https://github.com/bitdust/WamaCry', NULL, NULL, '2018-07-07 17:25:52', '2018-07-07 17:25:53'),
	(33, 2, '涂鸦', 'https://quickdraw.withgoogle.com/', NULL, NULL, '2018-07-07 17:26:30', '2018-07-07 17:26:30'),
	(34, 2, 'elastic官网', 'https://www.elastic.co/products/beats', NULL, NULL, '2018-07-07 17:28:06', '2018-07-07 17:29:38'),
	(35, 2, '信任小游戏', 'https://ncase.me/trust/', NULL, NULL, '2018-07-07 17:29:51', '2018-07-07 17:29:51'),
	(36, 2, '表情创作器', 'https://www.52doutu.cn/diy/', NULL, NULL, '2018-07-07 17:31:22', '2018-07-07 17:31:22'),
	(37, 2, '装B生成器', 'http://g.g.adccd.com/', NULL, NULL, '2018-07-07 17:32:05', '2018-07-07 17:32:38'),
	(38, 2, '2048', 'https://www.52doutu.cn/diy/', NULL, NULL, '2018-07-07 17:31:43', '2018-07-07 17:32:41'),
	(39, 2, '设计分享', 'https://dribbble.com/', NULL, NULL, '2018-07-07 17:33:16', '2018-07-07 17:33:17');


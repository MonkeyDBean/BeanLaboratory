-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.7.18 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 monkey_test 的数据库结构
CREATE DATABASE IF NOT EXISTS `monkey_test` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `monkey_test`;

-- 导出  表 monkey_test.test_record 结构
CREATE TABLE IF NOT EXISTS `test_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增索引',
  `record_data` varchar(50) NOT NULL DEFAULT 'test' COMMENT '记录数据',
  `create_time` datetime NOT NULL COMMENT '记录创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='测试记录表';

-- 正在导出表  monkey_test.test_record 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `test_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_record` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

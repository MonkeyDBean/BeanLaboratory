-- 查看主从状态 --
show master status;
show slave status;

-- 查看操作日志是否开启, 生产环境需设置开启 --
show variables like 'log_bin';

-- 查看定时事件是否开启, 安全考虑, 生产环境需设置关闭 --
show variables like 'event_scheduler';
-- 或 --
select @@event_scheduler;

-- 查看连接设置及当前连接信息 --
show status like '%connections%';
show full processlist;

-- 查看所有储过程 --
show procedure status;

-- 查看所有事件 --
show events;
-- 或 --
select * from mysql.`event`;

-- 开启事件：永久生效为更改my.cnf文件: event_scheduler=ON, 然后重启mysql, 临时更改(mysql重启后失效)如下 --
-- 或 SET GLOBAL event_scheduler = ON / OFF; --
set global event_scheduler = 1;

-- 开启/关闭某个事件: alter event event_name ON COMPLETION PRESERVE ENABLE / DISABLE; --

-- 插入测试数据 --
DROP PROCEDURE IF EXISTS test_data_insert;
DELIMITER //
CREATE PROCEDURE test_data_insert()
BEGIN
  DECLARE i INT;
  SET i = 1000;
  WHILE i < 1020 DO
    INSERT INTO config_info(`config_name`, `config_value`, `create_time`) VALUES("test", concat("test", i), CURRENT_TIMESTAMP);
    SET i = i + 1;
  END WHILE ;
END //
DELIMITER ;
CALL test_data_insert();

-- 删除测试数据 --
DROP PROCEDURE IF EXISTS test_data_delete;
DELIMITER //
CREATE PROCEDURE test_data_delete()
  BEGIN
    DELETE FROM config_info;
  END //
DELIMITER ;
CALL test_data_delete();

-- 事件测试：插入数据 --
DROP EVENT IF EXISTS test_insert_schedule_event;
CREATE EVENT IF NOT EXISTS test_insert_schedule_event ON SCHEDULE EVERY 10 SECOND
  ON COMPLETION PRESERVE ENABLE
DO CALL test_data_insert();

-- 事件测试：删除数据 --
DROP EVENT IF EXISTS test_delete_schedule_event;
CREATE DEFINER=`root`@`%` EVENT `test_delete_schedule_event` ON SCHEDULE AT '2019-04-15 12:30:00' ON COMPLETION NOT PRESERVE ENABLE DO call test_data_delete()
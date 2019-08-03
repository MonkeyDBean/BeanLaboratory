## 动态Banner
SpringBoot2支持许多新特性，其中一个小彩蛋是动态Banner
如果目录src/main/resources下同时存在banner.txt和banner.gif，项目会先将banner.gif每一个画面打印完毕之后，再打印banner.txt中的内容

## windows下安装使用mongodb
1.下载：http://www.mongodb.org/downloads
2.在解压后的路径中(如G:\mongoDB), 创建文件夹G:\mongoDB\data, 创建文件G:\mongoDB\log\mongod.log，分别用于db存储和日志文件存储
3.进入bin目录下，打开cmd, 执行
```
mongod -dbpath "G:\mongoDB\data"
```
可以看到默认端口27017建立，可到mongod.cfg文件中更改默认端口，db存储路径，日志路径等配置
4.测试连接: 新建cmd, 执行如下，可以看到版本号等信息
```
mongo
```
5.当mongod关闭时, 则无法连接到数据库，因此每次使用mongodb数据库都要开启mongod.exe程序，比较麻烦，此时可以将mongoDB安装为windows服务，以管理员身份在cmd下执行
```
mongod --dbpath "G:\mongoDB\data" --logpath "G:\mongoDB\log\mongod.log" --install --serviceName "MongoDB"
net start MongoDB
```
打开任务管理器，可以看到进程已启动
6.关闭服务及删除进程，以管理员身份在cmd下执行
```
net stop MongoDB
mongod --dbpath "G:\mongoDB\data" --logpath "G:\mongoDB\log\mongod.log" --remove --serviceName "MongoDB"
```
7.桌面版可视化操作，下载安装Robo 3T(功能对应mysql的hedisql或navicat), 建立连接

## mongodb常用命令
参考：https://www.cnblogs.com/lecaf/p/mongodb.html
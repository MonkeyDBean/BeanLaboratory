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
7.桌面版可视化操作，下载安装Robo 3T(功能对应mysql的hedisql或navicat), 建立连接。数据视图可选文本视图, 表格视图, 树状视图。

## mongodb
### 简介
MongoDB是NoSql数据库, 数据存储形态与Mysql这类关系型数据库有本质区别。

MongoDB存储的基本对象为document(文档), 若干个document组成了Collection(集合)。

document使用BSON(Binary JSON)结构来存储数据, BSON是二进制形式的JSON文档，比JSON包含更多的数据类型([参考](https://docs.mongodb.com/manual/reference/bson-types/)),
支持的数据类型包括string, integer, boolean, double ,null, array object等基本数据类型，还有data, object id, binary data, regular expression, code等。

document结构是field-and-value pairs(域值对), 是有序的, 不能有重复的域, 域的类型为字符串, 域的名称不能包含\0(空字符串), 也不能包含.和$(有特殊含义); 
值支持任何类型的BSON数据, 还可以是其他document或Collection。数据示例(以当前程序插入结果示例)如下:
```
# 执行如下命令查看User集合所有数据
db.getCollection('user').find({})

# 结果如下(当前仅插入过一条数据)
{
    "_id" : ObjectId("5a9504a167646d057051e229"),
    "username" : 1,
    "phone" : "18812345678",
    "email" : "justtest",
    "birthday" : ISODate("1994-02-04T00:00:00.000Z"),
    "_class" : "com.monkeybean.flux.model.User"
}
```
_id是主键，是保留字段，值必须是独一无二的。

Collections是一组documents, 相当于关系型数据库中的一个表, 多个Collection构成Database。

集合内的document可以有不同的域(field)，所以任何document都可以存在同一个collection里面, 如：
```
{"name": "Jack", "age": 18}
{"name": "Jones", "stock": 32, "contribs: ["firstTest", "secondTest", "thirdTest"]}
```
不推荐同一个Collection存放不同域或不同值类型的的document, 一是方便管理, 二是创建索引时, 一个Collection只放一种类型的document, 可以更加有效的对集合创建索引

### CURD
[命令参考](https://docs.mongodb.com/manual/reference/method/js-collection/)

数据库和Collection都是隐式创建的, 要使用时自动创建, 无需手动创建, 集合显式创建及删除命令如下：
```
# 显示创建集合
db.createCollection("c1")

# 删除集合
db.c1.drop()

# 删除数据库
db.dropDatabase()
```

#### Create
```
db.c1.insert({name:"user1", age:NumberInt(1), testArray: Array(1,2,3,4,5)});
# 或
db.c1.insert({name:"user1", age:NumberInt(1), testArray: [1,2,3,4,5]});
```

#### Update
语法
```
db.collection.update(criteria, objNew, upsert, multi)
```

参数含义
- criteria: update的查询条件, 类似sql update查询内where后面的
- objNew: update的对象和一些更新的操作符(如$,$inc...)等, 可理解为sql update查询内set后面的
- upsert: 如果不存在update的记录，是否插入objNew, true为插入; 默认是false, 表示如果记录不存在, 也不新增
- multi: mongodb默认是false, 只更新找到的第一条记录, 如果这个参数为true, 就把按条件查出来多条记录全部更新

常见操作符
- {$inc: {field : value}}: 为某个字段添加指定值
- {$set : {field : value}}: 为某个字段设置值，当field不存在时，添加字段
- {$unset : {field : value}}: 删除给定的字段
- {$push : {field : value}}: 将值追加到field中, 要求value为array格式
- {$pushAll : {field : value_array}}: 将数组追加到field中
- {$rename: {old_field_name : new_field_name}}: 修改字段名

使用示例
```
db.c1.update({name:"user1"},{$inc:{age:1}})
db.c1.update({name:"user1"},{$set:{age:NumberLong(18)}},false,true)
db.c1.update({name:"user2"},{$set:{age:NumberInt(25)}},true)
```

#### Retrieve
查询某个collection中所有数据
```
db.c1.find()
# 等效于
db.c1.find({})
```
指定查询条件
```
db.c1.find({name:"user2"})
```
指定返回的列
```
db.c1.find({name:"user2"}, {name:1})
```
默认情况下，会返回_id这一列，如果不想返回这一列，使用如下命令
```
db.c1.find({name:"user2"}, {name:1, _id: 0})
```
查询条件限制, 操作符如下
- $gt(greater than): 大于(>)
- $lt(lower than): 小于(<)
- $lte: 小于等于(<=)
- $gte: 大于等于(>=)
- $ne: 不等于
- $exists: 字段是否存在
- $in: 数据存在于
- $nin: 数据不存在于
- $or: 或者
- $all: 全部包含
- $size: 数组长度

查询示例:
```
# 查询年龄小于20的数据
db.c1.find({age:{$lt:20}})

# 指定区间
db.c1.find({age:{$gt:20,$lt:30}})

# 查询存在age字段的数据
db.c1.find({age:{$exists:true}})

# $in操作类似于传统关系型数据库中的in
db.c1.find({age:{$in:[20,25,30]}})
db.c1.find({age:{$nin:[20,25,30]}})

# 或
db.c1.find({$or: [{name:"user1"},{name:"user2"}]});

# $all操作类似于$in,但$all要全数组里面的值全部包含在记录中。返回的记录中字段a中必须要包含[1,2,3]
db.c1.find({testArray:{$all:[1,2,3]}})

# 字段testArray是一个数组, 且该数组有5个元素
db.c1.find({testArray:{$size:5}})

# 数据数量统计
db.c1.find().count()

# 排序, 按age升序排列
db.c1.find().sort({age:1}) 

# 按age降序排列
db.c1.find().sort({age:-1})

# 从0开始输出4个
db.c1.find().limit(4)

# 跳过前5条后再取5条数据, 相当于mysql中的SELECT * FROM USER LIMIT 5,3
db.c1.find().skip(3).limit(5)

# 注意当查询语句中有skip, limit时, 默认情况下, count会忽略这些条件，因此此处必须要设置count(1),否则前面的条件会没有利用上
db.c1.find().skip(2).limit(5).count(1)
db.c1.find().skip(2).limit(5).count()

# 去重
db.c1.distinct("name") 
```

#### Delete
```
# 删除所有记录
db.c1.remove({})

# 删除指定条件记录
db.c1.remove({name:"user1"})
```

### 索引
MongoDB会在 _id字段创建一个唯一索引。
查询语句接.explain可查看执行过程, inputStage中的stage若为COLLSCAN为全表查询(未触发索引), stage为IXSCAN为触发索引。

[索引参考](https://www.runoob.com/mongodb/mongodb-advanced-indexing.html)

#### 单字段索引
除了_id字段, 还可以在document的某个字段上加升序或降序的索引。索引的升序降序对sort操作的升序降序没有影响, 因为MongoDB可以从任一方向遍历索引。
创建索引的命令格式如下
```
db.collection.createIndex(<field>: <type>, <options>)
```
示例document:
```
# 数据格式
{
  "_id":ObjectId("asdfghjkl123456"),
  "score":100,
  "location":{"state":"NY","city":"New York"}
}

# 初始化
db.c2.insert({score:NumberInt(10), location:{state:"BJ", city:"Bei Jing"}});
db.c2.insert({score:NumberInt(100), location:{state:"NY", city:"New York"}});
db.c2.insert({score:NumberInt(1000), location:{state:"P", city:"Paris"}});
```
对示例document单个字段建升序或降序索引:
```
# 1.查看创建的索引
db.getCollection('c2').getIndexes()
# 或
db.c2.getIndexes()

# 2.在score上建升序索引
db.c2.createIndex({score:1})
# 触发索引
db.c2.find({score:10})
db.c2.find({score:{$gt:10}})

# 3.对嵌套的字段建立索引, 如在location.state上建立升序索引
db.c2.createIndex({"location.state":1})
 # 触发索引
db.c2.find({"location.state":"BJ"})
db.c2.find({"location.city":"city","location.state":"NY"})

# 4.对整个嵌套文档建立索引, 如在location上建立索引
db.c2.createIndex({location:1})
# 触发索引, 查询不一定遵循指定索引的顺序，mongodb会自动优化
db.c2.find({location:{city:"New York",state:"NY"}})
db.c2.find({location:{state:"NY",city:"New York"}})
```
#### 复合索引
在多个字段上建立索引。定义复合索引时，字段的顺序非常重要。复合索引最多可以有32个字段。
命令格式为:
```
db.collection.createIndex({<field1>:<type>, <field2>:<type2>, ...})
```
示例文档数据格式:
```
{
    "_id" : ObjectId("5d6902829e9656ae7ef0c13e"),
    "score" : 10,
    "location" : {
        "state" : "BJ",
        "city" : "Bei Jing"
    },
    "index" : 100
}

```
创建索引:
```
# 在score上创建升序索引; 在location上创建降序索引
db.c2.createIndex({"score":1,"index":-1})

# 触发索引
db.c2.find({score:10})
db.c2.find({score:10,index:100})
db.c2.find({index:100,score:10})
db.c2.find().sort({score:1,index:-1})
 
# 不能触发索引(查询不包含score字段, 排序升降序与索引不同)
db.c2.find({index:100})
db.c2.find().sort({score:-1,index:-1});
db.c2.find().sort({index:1,score:1})
```
注：
1.对单字段索引来说，索引类型是升序(1)或降序(-1)是无所谓的，因为MongoDB可以从任一方向遍历索引；但是对于复合索引来说，索引类型关系到索引能否支持一个排序操作。

2.建立复合索引，索引中两字段前后顺序与查询条件字段在数量一致的情况下，顺序不影响使用索引查询。

3.当复合索引中的字段数量与查询条件字段数量不一致情况下，以复合索引字段前置优先规则(最左匹配), 即查询条件以索引中的第一个字段开始，并且查询条件里的字段从左到右的顺序要和索引中的字段从左到右的顺序一致。

4.$nin和$ne查询是不走索引。

#### 多键索引
多键索引可以为数组形式的字段添加索引。如果索引包含数组值的字段，MongoDB会为数组的每个元素创建单独的索引。

多键索引允许通过匹配元素或者数组中的元素来查询文档。

如果索引字段包含数组值，MongoDB会自动决定是否添加多键索引，不需要手动指定多键索引。
命令形式:
```
db.coll.createIndex({<field>:<1 or -1>})
```
示例文档数据格式:
```
{
    "_id" : ObjectId("5d6c77de9e9656ae7ef0c141"),
    "a" : [ 
        1.0, 
        2.0
    ],
    "b" : [ 
        3.0, 
        4.0
    ],
    "category" : "arrays1"
}
```
每个document最多只能有一个值为数组的索引字段。可以添加索引{a:1}或{b:1}，不可以添加索引{a:1,b:1}，如果{a:1,b:1}的索引已经创建了，则a和b当中必定有一个是非array，此时插入一个a和b都是array的文档就会失败。
排序可能影响性能，在对使用了多键索引的数组进行排序时，过程中包括阻塞SORT阶段，即等数据都查出来之后再进行排序。

## 性能优化
- 创建索引
- 限定返回结果条数
- 查询使用到的字段，不查询所有字段
- 使用固定集合
- 使用慢查询日志: db.c1.find({name:"user1"}).explain()

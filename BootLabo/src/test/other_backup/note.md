## 项目结构
当前工程结构，service层为业务逻辑处理，do层为dao的参数封装。
可考虑如下结构：do层下为service层，service关联数据库操作, do层为业务逻辑处理。

## Mybatis代码生成器
可考虑引入maven依赖org.mybatis.generator或使用idea插件MyBatisCodeHelperPro(参考：https://blog.csdn.net/HcJsJqJSSM/article/details/84348966)

## 代码检查
本地代码检查可考虑使用idea插件：SonarLint或CheckStyle-IDEA

## 请求拦截顺序
Req-->Filter-->Interceptor-->Aspect-->Controller
![concept_use](image_backup/concept_use/filter_seq.png)

Filer是Java Web里的, 是获取不到Spring里的Controller信息; Interceptor、Aspect是和spring 相关的，可以获取到Controller的信息
注：在线绘制流程图等UML图, 推荐ProcessOn; 在线图片压缩推荐TinyPng

## 起服脚本
若不用python启动，则如下：
```
nohup java -cp ../dist/*:../lib/*:../resource org.springframework.boot.loader.JarLauncher >/dev/null 2>&1 &
```
debug:
```
nohup java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8096 -cp ../dist/*:../lib/*:../resource  org.springframework.boot.loader.JarLauncher >/dev/null 2>&1 &
```

## mysql创建新用户并授权远程连接等所有权限
root登录mysql后
```
create user 'usrName'@'%' identified by 'password';
grant all privileges on *.* to usrName@'%'identified by 'password';
```

## 若引入Redis作为session存储(EnableRedisHttpSession)
一般情况，cookie作用范围为域名根路径
1.未引入Redis前, 若不设置cookie作用范围, cookie默认作用范围同根路径context-path的设置，需设置如下
```
# 根路径
server.servlet.context-path=/monkey
# cookie作用范围
server.servlet.session.cookie.path=/
```
2.引入Redis, 则使用如下方式指定cookie作用范围
```
@Bean
CookieSerializer cookieSerializer() {
    DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
    defaultCookieSerializer.setCookiePath("/");
    return defaultCookieSerializer;
}
```
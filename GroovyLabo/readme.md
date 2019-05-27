## 简介
Groovy是基于JVM的敏捷开发语言，是一种面向对象的动态语言，既可以用于面向对象编程，又可以用作纯粹的脚本语言。
Groovy不仅结合了Python、Ruby和Smalltalk语言的特性，而且能够与Java很好地结合，可用于扩展现有代码。由于其运行在JVM上的特性，Groovy可使用Java语言编写的库，Java也可以调用Groovy。


## Windows环境配置
Sdk下载：[传送](https://groovy.apache.org/download.html)

1.环境变量
新增GROOVY_HOME，PATH后追加 %GROOVY_HOME%/bin;

2.查看配置结果
打开cmd，输入groovy -v或groovysh

注：若提示Java_HOME is set to an invalid directory, 可能是jdk的安装路径有不识别的字符(如.), 则打开startGroovy.bat, 将if not "%JAVA_HOME%" == "" goto have_JAVA_HOME 注释掉。

3.Groovy自带编辑器 
打开cmd, 输入groovyconsole
第一行代码：println "Hello Groovy" 然后Ctrl+R运行
或者 File --Save as... 保存为.groovy文件, 然后命令行下用groovy xxx.groovy运行Groovy程序

4.实际开发推荐IDE为idea


## 参考文档
官网：[传送](http://www.groovy-lang.org/)

语法简介：[传送](https://www.jianshu.com/p/e8dec95c4326)

配置参考：[传送](https://blog.csdn.net/qq_27828109/article/details/77892295)

SpringBoot使用Groovy, SpringInitializr生成项目：[传送](https://start.spring.io/) 
# g-Rpc & g-Rpc-web
## 概念参考：
1.Http2特性：https://www.cnblogs.com/frankyou/p/6145485.html
2.istio简介：http://www.sohu.com/a/270131876_463994
3.envoy介绍：http://www.servicemesher.com/envoy/intro/what_is_envoy.html
4.grpc官方文档：https://grpc.io/docs/
5.grpc-web概述：https://cloud.tencent.com/developer/news/334792
6.REST的替代者(Envoy+gRPC-Web): https://mp.weixin.qq.com/s/HxEupqk94KtZuy4h4HWGGw
7.浏览器引入gRPC的现况：https://mp.weixin.qq.com/s/45_B3mzVgqlr4Q8Ji19o-w
8.grpc-web github地址:https://github.com/grpc/grpc-web
9.springboot整合grpc参考：
https://github.com/xiaoguangtouqiang/grpc-demo
https://blog.csdn.net/xiaoguangtouqiang/article/details/80492324
10.serviceMesh浅析：
https://www.zhihu.com/people/monkeybean/activities
https://philcalcado.com/2017/08/03/pattern_service_mesh.html

## 示例参考:
1.grpc官方示例：https://github.com/grpc/grpc-java/tree/master/examples
2.grpc简单实践：https://www.jianshu.com/p/46d600e5a1b1
3.前端使用protobuf: https://mp.weixin.qq.com/s/Pd0Kzh0j_7a6igb9_Xn7Mw

## grpc验证步骤
1.proto目录下编写helloworld.proto
2.mvn package
3.生成的target/generated-sources/protobuf目录，将grpc-java以及java目录上右键，Make Directory As Generated Sources Root
4.编写HelloWordServer.java以及HelloWordClient.java
5.编写测试类HelloWordServerTest.java以及HelloWordClientTest.java

## grpc-web验证步骤
### 测试序列化
1.在 https://github.com/protocolbuffers/protobuf/releases 下载 protoc-3.7.0-win64.zip，解压后将bin目录添加到path环境变量中。
```
# 验证配置成功,命令行输入
protoc --version
# 显示如下
libprotoc 3.7.0
```
2.拷贝helloworld.proto文件到test/resource_bk目录，然后命令行输入如下，同级目录生成helloworld_pb.js
```
protoc ./helloworld.proto --js_out=import_style=commonjs,binary:.
```
3.node安装依赖
```
# 对库文件的引用库
npm install -g require

# 用来打包成前端使用js文件的库
npm install -g browserify

# 打包目录下执行如下, 生成protobuf的库文件
npm install google-protobuf
```
4.编写文件exports.js，然后执行如下命令，编译打包, 生成HelloWorld.js文件
```
browserify exports.js > HelloWorld.js
```
5.编写index.html，运行测试，可调用testProtoBuf函数测试序列化
### 测试通信
6.在 https://github.com/grpc/grpc-web/releases 下载 protoc-gen-grpc-web-1.0.3-windows-x86_64.exe
7.运行目录，安装node依赖
```
npm i grpc-web
```
8.在helloworld.proto文件所在目录执行如下，生成helloworld_grpc_web_pb.js文件
```
protoc ./helloworld.proto --plugin=protoc-gen-grpc-web=<path to protoc-gen-grpc-web> --grpc-web_out=import_style=commonjs,mode=grpcwebtext:.
```
如
```
protoc ./helloworld.proto --plugin=protoc-gen-grpc-web=G:\grpc-web\protoc-gen-grpc-web-1.0.3-windows-x86_64.exe --grpc-web_out=import_style=commonjs,mode=grpcwebtext:.
```
9.同步骤四，添加导出模块，打包构建，index.html引入生成文件，添加请求服务端函数，测试

## 备注
1.github问题跟踪:
https://github.com/grpc/grpc-web/issues/490
https://github.com/protocolbuffers/protobuf/issues/5831
https://github.com/grpc/grpc-web/issues/491
2.grpc-web测试步骤中，可下载protoc和grpc-web plugin后，文件一次生成,节省步骤
```
protoc ./helloworld.proto --plugin=protoc-gen-grpc-web=G:\grpc-web\protoc-gen-grpc-web-1.0.3-windows-x86_64.exe --js_out=import_style=commonjs,binary:. --grpc-web_out=import_style=commonjs,mode=grpcwebtext:.
```
3.测试通信暂不可用，error如下，待解决
```
Uncaught TypeError: proto.helloworld.GreeterClient is not a constructor
```
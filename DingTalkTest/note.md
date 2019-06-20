## 钉钉开发文档
```
# E应用(小程序)
开发部署方式类似微信小程序(各种平台的小程序最初都是参考vue设计的)
https://open-doc.dingtalk.com/microapp/bgb96b/gx9vwr

# H5微应用
开发部署方式类似微信公众号
https://open-doc.dingtalk.com/microapp/bgb96b/aw3h75

# 服务端接口
https://open-doc.dingtalk.com/microapp/serverapi2/eev437
```

## 钉钉基础信息配置
```
在钉钉管理后台创建应用时，填写应用首页地址(如http://monkeybeantest.vaiwan.com/index)，PC端首页地址，管理后台地址，服务器出口IP等信息
按官方文档绑定开发人员及可见人员，根据需求开通接口权限
```

## 工具
```
# ide
小程序开发者工具及微应用开发者工具下载地址: https://open-doc.dingtalk.com/microapp/kn6zg7/zunrdk

# 内网穿透工具
下载说明：https://open-doc.dingtalk.com/microapp/kn6zg7/hb7000
下载地址: git clone https://github.com/open-dingtalk/pierced.git
有时clone该项目会报异常，已备份到当前项目的test/backup目录下

# DingDingRc版
已备份到当前项目的test/backup目录下
```

## 内网穿透工具
```
# 内部集成的ngrok
https://open-doc.dingtalk.com/microapp/kn6zg7/hb7000

# 下载后无需更改配置，windows cmd启动命令如下, 微应用调试使用，小程序一般调试无需内网穿透
ding -config=./ding.cfg -subdomain=$yourSubdoamin $yourLocalServicePort
如
ding -config=./ding.cfg -subdomain=monkeybeantest 8080
```

## 微应用调试
```
# 访问测试
$$yourSubdoamin/vaiwan.com/$path
如
monkeybeantest.vaiwan.com/index

# 前端调试
钉钉rc版本打开插件后，浏览器输入localhost:16888调试
```

## 小程序调试
```
小程序官方demo(服务端demo使用的springboot版本较老): https://open-doc.dingtalk.com/microapp/bgb96b/vg36xk
前端ide扫码关联小程序，具体调试步骤见官方文档，较为便捷
```

## 其他
```
钉钉开放平台，sdk包管理有些混乱，无统一下载及追溯的对外链接，命名方式也待规范，需有详细的版本历史(包及release功能说明)，对于jar包，可考虑用上传到maven中心库；使用方可统一管理，上传到内网私库。
```

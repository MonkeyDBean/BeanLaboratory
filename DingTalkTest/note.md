## 钉钉开发文档
```
# E应用
https://open-doc.dingtalk.com/microapp/bgb96b/gx9vwr

# H5微应用
https://open-doc.dingtalk.com/microapp/bgb96b/aw3h75

# 服务端接口
https://open-doc.dingtalk.com/microapp/serverapi2/eev437
```
## 钉钉后基础信息配置
```
# H5微应用为例, 填写应用首页地址，PC端首页地址，管理后台地址，服务器出口IP
# 如应用首页地址，PC端首页地址及管理后台地址配置相同，为如下：
http://monkeybeantest.vaiwan.com/index
```
## 内网穿透工具
```
# 内部集成的ngrok
https://open-doc.dingtalk.com/microapp/kn6zg7/hb7000

# 下载后无需更改配置，windows cmd启动命令如下
ding -config=./ding.cfg -subdomain=$yourSubdoamin $yourLocalServicePort
如
ding -config=./ding.cfg -subdomain=monkeybeantest 8080
```

## 调试
```
# 访问测试
$$yourSubdoamin/vaiwan.com/$path
如
monkeybeantest.vaiwan.com/index

# 前端调试
钉钉rc版本打开插件后，浏览器输入localhost:16888调试
```

## 工具
```

穿透工具pierced(官方github下载报异常)及DingDingRc版，备份在当前项目test/backup目录中
```

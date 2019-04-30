### 基本概念
SpringSecurity是提供安全控制解决方式的框架(即访问权限进行控制), 核心功能即认证和授权。

安全性包括用户认证(Authentication)和用户授权(Authorization)两个部分。

用户认证指的是验证某个用户是否为系统中的合法主体，也就是说用户能否访问该系统。用户认证一般要求用户提供用户名和密码。系统通过校验用户名和密码来完成认证过程。

用户授权指的是验证某个用户是否有权限执行某个操作。在一个系统中，不同用户所具有的权限是不同的。比如对一个文件来说，有的用户只能进行读取，而有的用户可以进行修改。一般来说，系统会为不同的用户分配不同的角色，而每个角色则对应一系列的权限。

引入Security后，访问服务时，会弹出登录页，访问认证。默认用户名为user, 默认密码为程序启动时自动生成的一串字符串，格式如8961e7a8-5f2d-4808-bff1-44bc9491bb45。用户名密码可在配置文件设置。

### 参考链接
[Spring Security参考](https://springcloud.cc/spring-security.html#getting-started)

[Spring Security手册](https://springcloud.cc/spring-security-zhcn.html#getting-started)

[Spring Security基本使用](https://blog.csdn.net/liushangzaibeijing/article/details/81220610)

[Shiro和Spring Security对比](https://blog.csdn.net/liyuejin/article/details/77838868)

[SpringSecurity整合OAuth2](https://blog.csdn.net/weixin_42033269/article/details/80086422)

[基于Spring Boot2 + Spring Security OAuth2 实现单点登陆](https://blog.csdn.net/qq_19671173/article/details/79748422)

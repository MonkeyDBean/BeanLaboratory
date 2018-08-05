# 验证传入参数有效性

# 一、登录接口
## 1.正确请求
curl -v -d "user=13812345678&pwd=51be57979b8897c45240ec41910d75a7&response=hello&stay=false&stime=1533178505066" http://127.0.0.1:8096/monkey/identity/user/login -c

## 2.缺少参数
curl -v -d "user=13812345678&pwd=51be57979b8897c45240ec41910d75a7&stay=false&stime=1533178505066" http://127.0.0.1:8096/monkey/identity/user/login

## 3.参数格式错误
curl -v -d "user=13812345678&pwd=51be57979b8897c45240ec41910d75a7&response=hello&stay=false&stime=153317850" http://127.0.0.1:8096/monkey/identity/user/login

# 二、修改密码
## 1.正确请求
curl -v -d "oldpwd=51be57979b8897c45240ec41910d75a7&newpwd=e9bc0e13a8a16cbb07b175d92a113126&stime=1533178505066" http://127.0.0.1:8096/monkey/identity/password/update -H "Cookie:JSESSIONID=0C6497B32DA89A6B1402AAF66D24FF38"

## 2.参数格式非法
curl -v -d "oldpwd=51be57979b8897c45240ec41910d75a7&newpwd=&stime=1533178505066" http://127.0.0.1:8096/monkey/identity/password/update -H "Cookie:JSESSIONID=0C6497B32DA89A6B1402AAF66D24FF38"

# 三、获取图片列表
## 1.正确请求
curl -v "http://127.0.0.1:8096/monkey/operation/list/image/get?sharetype=0&current=1&size=10&total=0" -H "Cookie:JSESSIONID=0C6497B32DA89A6B1402AAF66D24FF38"

## 2.缺少参数
curl -v "http://127.0.0.1:8096/monkey/operation/list/image/get?sharetype=0&current=1&size=10&" -H "Cookie:JSESSIONID=0C6497B32DA89A6B1402AAF66D24FF38"
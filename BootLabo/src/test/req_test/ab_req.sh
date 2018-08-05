## ulimit：linux可通过命令设置进程资源(window无此参数)，如 ulimit -d 65535 -n 65535 ：每个进程数据段的最大值为65535kbytes，每个进程可同时打开的最大文件数为65535

## 并发测试一，无数据库操作，以请求系统状态接口为例，并发为100，总请求次数为1000
ab -kc 100 -n 1000 http://127.0.0.1:8096/monkey/general/sniff/status

:<<!
测试结果如下:

Server Hostname:        127.0.0.1
Server Port:            8096

Document Path:          /monkey/general/sniff/status
Document Length:        583 bytes

Concurrency Level:      100
Time taken for tests:   2.409 seconds
Complete requests:      1000
Failed requests:        0
Keep-Alive requests:    0
Total transferred:      734000 bytes
HTML transferred:       583000 bytes
Requests per second:    415.02 [#/sec] (mean)
Time per request:       240.950 [ms] (mean)
Time per request:       2.409 [ms] (mean, across all concurrent requests)
Transfer rate:          297.49 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.2      0       1
Processing:     4  234 166.7    211     859
Waiting:        3  202 152.2    182     831
Total:          4  234 166.7    211     860

Percentage of the requests served within a certain time (ms)
  50%    211
  66%    276
  75%    330
  80%    354
  90%    467
  95%    549
  98%    662
  99%    758
 100%    860 (longest request)
!

## 并发测试二，数据库读取数据，以获取信息列表为例
ab -kc 100 -n 1000 -C "JSESSIONID=4D2281DDF74E1438CDC3A490096CB1F0" "http://127.0.0.1:8096/monkey/operation/info/project/other/get?type=2&current=1&size=10&total=0"

:<<!
测试结果如下：

Server Hostname:        127.0.0.1
Server Port:            8096

Document Path:          /monkey/operation/info/project/other/get?type=2&current=1&size=10&
total=0
Document Length:        956 bytes

Concurrency Level:      100
Time taken for tests:   6.925 seconds
Complete requests:      1000
Failed requests:        0
Keep-Alive requests:    0
Total transferred:      1107000 bytes
HTML transferred:       956000 bytes
Requests per second:    144.40 [#/sec] (mean)
Time per request:       692.500 [ms] (mean)
Time per request:       6.925 [ms] (mean, across all concurrent requests)
Transfer rate:          156.11 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.2      0       1
Processing:    20  679 266.5    660    1806
Waiting:       19  654 261.8    634    1805
Total:         20  679 266.5    660    1806

Percentage of the requests served within a certain time (ms)
  50%    660
  66%    752
  75%    828
  80%    887
  90%   1018
  95%   1127
  98%   1330
  99%   1522
 100%   1806 (longest request)
!
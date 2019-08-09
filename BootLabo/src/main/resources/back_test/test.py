# test param print
# encoding: utf-8
import sys
import datetime
import time

concat_str='-----'
all_param=''
for index in range(len(sys.argv)):
    if(index > 0):
        all_param = all_param + concat_str + sys.argv[index]

if(concat_str in all_param):
    all_param = all_param + concat_str

nowTime = datetime.datetime.now()
contentText = "current time is: " + nowTime.strftime("%Y-%m-%d %H:%M:%S") + "; all param concat is: " + all_param
print(contentText)

# 参数单位为秒
# time.sleep(2)
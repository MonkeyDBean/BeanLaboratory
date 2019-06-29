# encoding: utf-8
import datetime
nowTime = datetime.datetime.now()
contentText = "Hello World, time is: " + nowTime.strftime("%Y-%m-%d %H:%M:%S")
print(contentText)
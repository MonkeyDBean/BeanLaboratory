@rem ### test script, param note: %0表示bat文件名; %*表示所有传入参数; %n为第n(范围为1-9)个参数, 如果参数数量大于9, 则用到shift
@rem shift [/n], n的取值是[0,8]的整数; [/n]为可选参数, 当赋予n某个值时, 就意味着命令从第n个参数开始移位; 当n赋予的值为0,1或不传此参数时, 则表示替换参数左移一个位置, 后面的替换参数陆续填补上去, 直至可替换参数为空  ###
@echo off
@rem echo Hello World

@rem 打印所有参数
@rem echo %*
@rem pause
set concat_str=-----
set all_param=
:param
set str=%1
if "%str%"=="" (
  goto end
)
set all_param=%all_param%%concat_str%%str%
shift /0
goto param
:end
echo all param concat is %all_param%%concat_str%

@rem pause
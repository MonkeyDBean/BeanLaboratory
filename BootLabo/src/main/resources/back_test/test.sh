# test param print, 运行前首先进入文件, vim命令行模式下设置编码set ff=unix
#!bin/bash

# 开启回显(默认为开启):
# stty echo
# 关闭回显(在linux下的交互将不显示)
# stty -echo

# 打印参数数量
# echo $#
# 打印所有参数
# echo $*
# echo $@

all_param=
concat_str=-----
for i in $*
do
  all_param=$all_param$concat_str$i
done
if [[ $all_param =~ $concat_str ]]
then
  all_param=$all_param$concat_str
fi
echo 'all param concat is '$all_param
=begin
ruby download: https://rubyinstaller.org/downloads/
windows系统变量->Path中添加安装后的bin目录
=end

#!/usr/bin/ruby

if RUBY_VERSION < '2.0.0'
  abort("requires Ruby 2.0.0 or higher")
end

# 处理指定参数(解析转换后的参数将从ARGV数组移除), 如命令行输入: ruby test.rb -u test_user -t just_test_data
require 'optparse'
options = {}
OptionParser.new do |parser|
  parser.on("-u", "--user [user_name]", "begin execution at user") do |v|
    options["user"] = v
  end
  parser.on("-t", "--test [test_data]", "begin execution at test data") do |v|
    options["test"] = v
  end
end.parse!
if not options["user"] or not options["test"]
  # 解开abort注释, 则将-u和-t参数限定为必传参数
  # abort("not enough parameters")
end
puts "assign param is: #{options["user"]}, #{options["test"]}";

# 输出其他参数
concat_str='-----'
all_param=''
for i in ARGV
  all_param.concat(concat_str).concat(i)
end
if(all_param.include? concat_str)
  all_param.concat(concat_str)
end
# puts "file name is #{__FILE__}";
puts "all param concat is: #{all_param}";

# sleep参数单位为秒
# sleep(2)
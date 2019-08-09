<?php

//单引号串中的内容被认为是普通字符, 内容不会被转义, 执行效率更高; 双引号串中的变量和特殊字符会被转义
$concat_str='-----';
$all_param='';
for ($x=1; $x<$argc; $x++) {
    $all_param=$all_param.$concat_str.$argv[$x];
}
if(strpos($all_param, $concat_str) !== false){
    $all_param=$all_param.$concat_str;
}
echo 'all param concat is '.$all_param;

# 参数单位为秒
# sleep(2)
?>

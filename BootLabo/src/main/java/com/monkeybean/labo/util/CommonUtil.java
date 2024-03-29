package com.monkeybean.labo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 其他通用工具类
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public final class CommonUtil {
    private static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    private CommonUtil() {
    }

    /**
     * 获取数字某位的值
     *
     * @param num   整数数字
     * @param index 非负数, 0:个位, 1:十位, 2:百位...
     * @return 返回数字某位，参数不合法则返回0
     */
    public static int getNum(int num, int index) {
        return (num / (int) Math.pow(10, index)) % 10;
    }

    /**
     * 格式化为小数点两位
     */
    public static String getString2(String str) {
        Double d = Double.parseDouble(str);
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }

    public static String getString2(int value) {
        return getString2(String.valueOf(value));
    }

    public static String getString2(double value) {
        return getString2(String.valueOf(value));
    }

    /**
     * 检查密码是否过于简单, 6-20位，包含字母和数字，该方法可用正则替换
     */
    public static boolean checkPassword(String str) {
        boolean isDigit = false;
        boolean isLetter = false;
        int length = str.length();
        if (length < 6 || length > 20)
            return false;
        for (int i = 0; i < length; i++) {
            if (Character.isDigit(str.charAt(i))) {
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) {
                isLetter = true;
            } else {
                return false;
            }
        }
        return isDigit && isLetter;
    }

    /**
     * 生成cron表达式字符串
     *
     * @param hour   时
     * @param minute 分
     * @param week   每周几
     * @param day    每月几号
     * @param type   类型, 1为每天定点执行, 2为每周定点执行, 3为每月定点执行
     * @return cron表达式
     */
    public static String generateCronStr(int hour, int minute, int week, int day, int type) {
        String cronStr;
        switch (type) {
            case 1:
                cronStr = String.format("0 %s %s * * ?", minute, hour);
                break;
            case 2:
                cronStr = String.format("0 %s %s ? * %s", minute, hour, week);
                break;
            case 3:
                cronStr = String.format("0 %s %s %s * ?", minute, hour, day);
                break;
            default:
                cronStr = "0 0 0 * * ?";
        }
        return cronStr;
    }

    /**
     * 调用脚本, 读取执行结果
     *
     * @param filePath    脚本路径, 脚本文件格式可以为bat, sh, py, rb, php
     * @param paramArray  参数数组, 每个字符串不包含空格, 若字符串有空格, 调用脚本时, 参数需加双引号
     * @param generateLog 是否生成日志, 日志路径为脚本文件同级目录. true为生成日志, 且脚本执行设为异步; false则不生成日志, 阻塞至脚本执行完毕, 读取执行结果
     * @param isWindows   运行环境是否为windows平台
     * @return 失败返回null
     */
    public static String callScript(String filePath, String[] paramArray, boolean generateLog, boolean isWindows) {

        //若为python脚本, 加命令前缀
        //解析python文件，也可考虑引入jython依赖(https://mvnrepository.com/artifact/org.python/jython-standalone), 调用PythonInterpreter
        String format = filePath.substring(filePath.lastIndexOf(".") + 1);
        String command;
        if ("py".equalsIgnoreCase(format)) {
            command = "python " + filePath;
        } else if ("php".equalsIgnoreCase(format)) {
            command = "php " + filePath;
        } else if ("rb".equalsIgnoreCase(format)) {
            command = "ruby " + filePath;
        } else {
            command = filePath;
        }
        command += " ";
        if (paramArray != null && paramArray.length > 0) {
            command += Arrays.stream(paramArray).collect(Collectors.joining(" "));
        }
        if (generateLog) {
            command = command + " > " + String.valueOf(System.currentTimeMillis()) + ".log 2>&1";
        }
        Process proc;
        try {

            //若输出重定向到文件, 即command中有>符号, 需调用exec()的重载方法 或者 将包含重定向指令的command封装在脚本中
            //原因是在exec(str)中, 不能把str完全看作命令行执行的command, 尤其是str中不可包含重定向符号 '<' 或 '>' 以及管道符 '|'
            if (generateLog && !isWindows) {
                proc = Runtime.getRuntime().exec(new String[]{"bash", "-c", command});
            } else {
                proc = Runtime.getRuntime().exec(command);
            }

            //等待子进程执行完成
            //proc.waitFor();

            if (proc.isAlive()) {
                logger.info("callScript, process is alive, time: [{}]", System.currentTimeMillis());
                //杀死子进程, 强制终止
                //proc.destroy();
                //杀死子进程, 强制终止, jdk1.8新增, 作用默认同destroy()方法
                //proc.destroyForcibly();
                //logger.info("callScript, destroyForcibly has been called");
            }
        } catch (Exception e) {
            logger.error("callScript, exec Exception: [{}]", e);
            return null;
        }
        if (!generateLog) {

            //同步
            int exitValue = proc.exitValue();

            //脚本退出值为约定值, 惯例为: 成功为0, 异常为1
            if (exitValue != 0) {
                logger.error("callScript, exitValue Exception: [{}]", exitValue);
                return null;
            }
            StringBuilder originContentSBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                String eachLine = br.readLine();
                while (eachLine != null) {
                    originContentSBuilder.append(eachLine);
                    eachLine = br.readLine();
                }
            } catch (IOException e) {
                logger.error("callScript StreamReader IOException: [{}]", e);
                return null;
            }
            return originContentSBuilder.toString();
        } else {

            //异步, 无需等待脚本执行完毕
            return "call ok";
        }
    }

}

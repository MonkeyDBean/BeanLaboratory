package com.monkeybean.algorithm.other;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by MonkeyBean on 2019/2/13.
 */
public class StupidReplace {
    public static void main(String[] args) throws IOException {
        while (true) {

//            //计算机上所有文件的储存是都是字节（byte）的储存; Java内用Unicode编码存储字符
//            //字节流处理单元为1个字节，操作字节和字节数组; 而字符流处理的单元为2个字节的Unicode字符，分别操作字符、字符数组或字符串
//            //字节流在操作的时候本身是不会用到缓冲区（内存）的，是与文件本身直接操作的; 而字符流在操作的时候是使用到缓冲区的
//            //接收任意长度数据，避免乱码产生，使用BufferedReader类(从缓冲区读取数据)；System.in为字节流，以下为字节流转为字符流
//            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)) ;
//            String originStr = reader.readLine();
//            if("exit".equals(originStr)){
//                return;
//            }
//            String newStr = originStr.replace('你','我').replace('吗',' ').replace('?','!');
//            System.out.println(newStr);

            //JDK1.5之后, Java提供了专门的输入数据类Scanner，此类可以完成BufferedReader类的功能, 比直接使用BufferedReader更加方便
            //将分隔符变成"\n"（默认空格也会截止）
            Scanner scanner = new Scanner(System.in);
            scanner.useDelimiter("\n");
            String originStr = scanner.next();
            if ("exit".equals(originStr)) {
                return;
            }
            String newStr = originStr.replace('你', '我').replace('吗', ' ').replace('?', '!');
            System.out.println(newStr);
        }
    }
}

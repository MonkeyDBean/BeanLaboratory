package com.monkeybean.algorithm.category;

import org.junit.Test;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by MonkeyBean on 2018/8/6.
 */
public class OtherUtilTest {

    //    @Test
    public void printSimpleHeart() throws Exception {
        System.out.println("full heart:");
        OtherUtil.printSimpleHeart(true);
        System.out.println("half heart:");
        OtherUtil.printSimpleHeart(false);
    }

    @Test
    public void printTwoHeart() throws Exception {
        System.out.println("two heart:");
        OtherUtil.printTwoHeart();
    }

    //    @Test
    public void testMapStream() {
        List<Integer> origin = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            origin.add(i);
        }
        origin.add(9);
        origin.add(10);
        System.out.println("list is: ");
        Arrays.stream(origin.toArray()).forEach(System.out::println);
        System.out.println("max is: " + origin.stream().mapToInt(e -> e).max().orElse(0));
        System.out.println("count is: " + origin.stream().mapToInt(e -> e).count());
        System.out.println("sum is: " + origin.stream().mapToInt(e -> e).distinct().sum());
    }

    //    @Test
    public void testForceCast() {
        Object b = null;
        Integer value = (Integer) b;
        System.out.println("testForceCast, value: " + value);
    }

//    @Test
    public void testFileRead() throws Exception {

        //列出文件
        String desktopPathStr = "C:\\Users\\Administrator\\Desktop\\";
        FileStore desktopStore = Files.getFileStore(Paths.get(desktopPathStr));
        System.out.println("Desktop总空间：" + desktopStore.getTotalSpace() / (1024 * 1024.0) + "MB");
        System.out.println("Desktop可用空间：" + desktopStore.getUsableSpace() / (1024 * 1024.0) + "MB");
        System.out.println("Desktop所有文件：");
        Files.list(Paths.get(desktopPathStr)).forEach(System.out::println);

        //复制文件
        String filePathStr = "C:\\Users\\Administrator\\Desktop\\test_file\\testFile.txt";
        String newFilePathStr = "C:\\Users\\Administrator\\Desktop\\test_file\\testFile2.txt";
        Path path = Paths.get(filePathStr);
        Files.copy(path, new FileOutputStream(newFilePathStr));
        System.out.println("filePath, 是否为隐藏文件：" + Files.isHidden(path));

        //读取文件
        Map<String, String> fileContentMap = new HashMap<>();
        Files.lines(Paths.get(newFilePathStr), StandardCharsets.UTF_8).forEach(value -> fileContentMap.put(path.getFileName().toString(), value));
    }

}

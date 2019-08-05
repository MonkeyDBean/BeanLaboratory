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

    @Test
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

    @Test
    public void testForceCast() {
        Object b = null;
        Integer value = (Integer) b;
        System.out.println("testForceCast, value: " + value);
    }

    @Test
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

    @Test
    public void testArrayLength() {
        int[] testArray = {1, 2};
        assert testArray.length == 2;
        testArray = new int[3];
        assert testArray.length == 3;
    }

    @Test
    public void testComparator() {
        List<SortTestClass> dataList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int heavy = random.nextInt(100);
            String des = "testData" + heavy;
            SortTestClass sortTestClass = new SortTestClass();
            sortTestClass.setDes(des);
            sortTestClass.setHeavy(heavy);
            dataList.add(sortTestClass);
        }
        if (!dataList.isEmpty()) {
            dataList.sort(Comparator.comparingInt(SortTestClass::getHeavy));
        }
        for (SortTestClass eachData : dataList) {
            System.out.println("des: " + eachData.getDes() + "\t heavy:" + eachData.getHeavy());
        }
    }

    @Test
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

    /**
     * 测试list remove方法
     */
    @Test
    public void testListRemove() {

        //初始化
        List<Object> list = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            list.add(i);
        }
        list.add(3);
        list.add(3);
        list.add(4);
        Integer aimObject = 3;

        //移除测试
        //1.不可用：正序删除时, 删除当前位置的值，下一个值就会补到当前位置; 只要list中有相邻2个相同的元素，就过滤不完;
//        for(int i = 0; i < list.size(); i ++){
//            if(aimObject.equals(list.get(i))){
//                list.remove(i);
//            }
//        }

        //2.可用: 正序删除, 需执行i--操作
//        for(int i = 0; i < list.size(); i ++){
//            if(aimObject.equals(list.get(i))){
//                list.remove(i);
//                i--;
//            }
//        }
//
//        //3.可用：倒序删除
//        for(int i = list.size() - 1; i >= 0; i --){
//            if(aimObject.equals(list.get(i))){
//                list.remove(i);
//            }
//        }
//
//        //4.可用: 使用迭代器
//        Iterator it = list.iterator();
//        list = new ArrayList<>();
//        while(it.hasNext()){
//            Object value = it.next();
//            if (aimObject.equals(value)) {
//                it.remove();
//            }else{
//                list.add(value);
//            }
//        }
//
        //5.可用：removeIf
        list.removeIf(aimObject::equals);

        //输出移除结果
        for (Object element : list) {
            System.out.println("element is: " + element);
        }
    }

    /**
     * 排序测试类
     */
    private class SortTestClass {

        /**
         * 描述
         */
        private String des;

        /**
         * 权重
         */
        private int heavy;

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public int getHeavy() {
            return heavy;
        }

        public void setHeavy(int heavy) {
            this.heavy = heavy;
        }
    }

}

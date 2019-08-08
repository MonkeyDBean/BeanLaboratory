package com.monkeybean.algorithm.category;

import org.junit.Test;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    /**
     * java8 新特性: Stream, 用声明的方式处理数据
     * Stream使用类似SQL语句从数据库查询数据的直观方式来提供一种对Java高阶抽象的集合运算
     * <p>
     * Stream是来自数据源的元素队列并支持聚合操作：
     * 1.Java中的Stream并不会存储元素(只是提供操作管道抓取数据), 而是按需计算
     * 2.数据源可以是集合, 数组, I/O channel, generator等
     * 3.聚合操作是类似SQL语句的操作, 比如filter, map, reduce, find, match, sorted等
     * <p>
     * 与之前的Collection操作不同, Stream操作还有两个基础的特征
     * 1.流式调用(中间操作都会返回流对象本身)
     * 2.内部迭代(以往的集合遍历使用Iterator或For-Each都是显式外部迭代)
     * <p>
     * 集合接口有两种方式生成流
     * 1.stream() : 为集合创建串行流
     * 2.parallelStream() : 为集合创建并行流
     * <p>
     * reference: https://www.ibm.com/developerworks/cn/java/j-lo-java8streamapi/
     */
    @Test
    public void testStream() {

        //所有Stream操作均以lambda表达式为参数
        //unordered不会打乱流, 它的作用是：消除流中必须保持的有序约束，允许之后的操作不必考虑有序, 可以加快一些方法的执行速度
        Random random = new Random();
        System.out.println("random print: ");
        random.ints().unordered().limit(5).map(Math::abs).forEach(System.out::println);

        //summaryStatistics
        List<Integer> numbers = Arrays.asList(10, 8, 1, 2, 3, 3, 7, 3, 7, 6);
        IntSummaryStatistics stats = numbers.stream().mapToInt(i -> i).summaryStatistics();
        System.out.println("statistics count: " + stats.getCount());
        System.out.println("statistics max: " + stats.getMax());
        System.out.println("statistics min: " + stats.getMin());
        System.out.println("statistics sum: " + stats.getSum());
        System.out.println("statistics average: " + stats.getAverage());

        //parallelStream, parallel
        List<Integer> squaresList = numbers.parallelStream().map(i -> i * i).distinct().collect(Collectors.toList());
        System.out.println("forEach: ");

        //对于并行流, forEach输出的顺序不一定(效率更高), forEachOrdered与元素的顺序严格一致
        Stream.of(squaresList).parallel().forEach(System.out::println);
        //写法不同, 作用同上
        //squaresList.parallelStream().forEach(System.out::println);

        System.out.println("forEachOrdered: ");
        Stream.of(squaresList).parallel().forEachOrdered(System.out::println);
        System.out.println("max is: " + squaresList.stream().mapToInt(i -> i).max().orElse(0));
        System.out.println("average is: " + squaresList.stream().mapToInt(i -> i).average().orElse(0));
        System.out.println("count is: " + squaresList.stream().mapToInt(i -> i).count());
        System.out.println("sum is: " + squaresList.stream().mapToInt(i -> i).sum());

        //filter, joining
        List<String> stringList = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        Arrays.stream(stringList.toArray()).forEach(System.out::println);
        String joinDelimiterStr = stringList.stream().filter(s -> !s.isEmpty()).collect(Collectors.joining(","));
        boolean matchResult = stringList.stream().noneMatch(String::isEmpty);
        System.out.println("joinDelimiterStr: " + joinDelimiterStr + ", testAllMatch: " + matchResult);

        //reduce
        Integer sum = numbers.stream().reduce(0, Integer::sum);
        System.out.println("reduce sum is: " + sum);

        //iterate与reduce作用相似(接收种子值), limit为生成数量, skip扔掉前n个元素, distinct去重, sorted排序
        System.out.println("stream iterate: ");
        Stream.iterate(1, i -> (i + 2) % 5).limit(10).skip(7).distinct().sorted().forEach(System.out::println);

        //generate
        System.out.println("generate uuid: ");
        Stream.generate(UUID.randomUUID()::toString).findFirst().ifPresent(System.out::println);

        //partitioningBy
        Map<Boolean, List<Integer>> resultMap = Stream.generate(new Random()::nextInt).parallel().limit(100).map(Math::abs).collect(Collectors.partitioningBy(i -> i < 2000000000));
        System.out.println(resultMap.size());
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

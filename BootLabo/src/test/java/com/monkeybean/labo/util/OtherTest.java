package com.monkeybean.labo.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static junit.framework.TestCase.*;

/**
 * 单元测试场景: https://tasdikrahman.me/2019/03/13/what-should-and-should-not-be-tested-in-unit-tests/
 * <p>
 * Created by MonkeyBean on 2018/7/26.
 */
public class OtherTest {
    private static Logger logger = LoggerFactory.getLogger(OtherTest.class);

    @Test(expected = NumberFormatException.class)
    public void testNull() {
        Integer.valueOf(null);
    }

    @Test
    public void testBooleanEqual() {
        boolean testB = false;
        assertTrue(Boolean.FALSE.equals(testB));
        assertTrue(!Boolean.TRUE.equals(testB));
    }

    @Test
    public void testLinkedList() {
        LinkedList<String> list = new LinkedList<>();
        list.add("111");
        list.add("222");
        list.add(1, "333");
        list.addAll(1, new ArrayList<>());
        list.addAll(1, new ArrayList<>(Arrays.asList("444", "555")));
        list.addAll(1, new ArrayList<String>() {{
            add("666");
            add("777");
        }});
        list.offer("888");
        list.offerFirst("999");
        list.push("AAA");
        for (int i = 0; i < list.size(); i++) {
            logger.info("element" + i + ": " + list.get(i));
        }
        logger.info("---***---");
        list.remove(1);
        list.remove("333");
        list.pop();
        for (int i = 0; i < list.size(); i++) {
            logger.info("element" + i + ": " + list.get(i));
        }
    }

    @Test
    public void testSplit() {
        String testStr = "test";
        logger.info("split 0 is: [{}]", testStr.split(",")[0]);
    }

    @Test
    public void testStrToList() {
        String[] strArray = {"123", "456", "789"};
        List<String> resList1 = new ArrayList<>();
        Collections.addAll(resList1, strArray);
        for (String eachElement : resList1) {
            logger.info("str2List, use addAll, element: [{}]", eachElement);
        }
        List<Integer> resList2 = Arrays.stream(strArray).unordered().map(Integer::parseInt).collect(Collectors.toList());
        for (Integer eachElement : resList2) {
            logger.info("str2List, use stream, element: [{}]", eachElement);
        }
    }

    @Test
    public void testLastIndex() {
        String origin = "file.11.jpg";
        String result = origin.substring(0, origin.lastIndexOf("."));
        logger.info("origin: " + origin + "\nresult: " + result);
    }

    @Test
    public void testMergeList() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);
        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(4);
        list2.add(5);
        list2.add(6);
        list1.removeAll(list2);
        list1.addAll(list2);
        logger.info("list1Result: " + Arrays.toString(list1.toArray()));

        // 注：通过Arrays.asList()转换的List不可调用addAll()等方法，否则会报UnsupportedOperationException
        // 原因是调用Arrays.asList()方法生成List时返回的是Arrays的静态内部类ArrayList(此ArrayList不是util包下的ArrayList),
        // 它自身并未重写addAll()等方法，而其父类AbstractList实现的addAll()等方法只会抛出UnsupportedOperationException
        // 如果仍想使用addAll()等方法，则用其他方式转换，不可用Arrays.asList()
        List<String> list3 = Arrays.asList("a", "b", "c", "d");
        String list4Str = "a,b ,e ,f";
        List<String> list4 = Arrays.stream(list4Str.split(",")).map(s -> (s.trim())).collect(Collectors.toList());

        //报UnsupportedOperationException
//        list3.addAll(list4);

        list4.addAll(list3);
        list4 = new ArrayList<>(new HashSet<>(list4));
        String list4ResultStr = list4.stream().collect(Collectors.joining(","));
        logger.info("list4Result: [{}]", list4ResultStr);
    }

    @Test
    public void testLang3() {
        String a = "testA";
        String b = "testB";
        String c = "";
        String d = null;
        assertTrue(StringUtils.isAnyEmpty(a, b, c));
        assertTrue(StringUtils.isAnyEmpty(a, b, d));
    }

    @Test
    public void testJsonMap() {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("a", "a123");
        hm.put("c", "cc");
        hm.put("b", 2456);
        hm.put("d", "dd");
        String result = JSON.toJSONString(hm);
        logger.info("JSON.toJSONString, result: [{}]", result);
    }

    @Test
    public void testSha1() {
        String originStr = "{\"nickName\":\"Band\",\"gender\":1,\"language\":\"zh_CN\",\"city\":\"Guangzhou\",\"province\":\"Guangdong\",\"country\":\"CN\",\"avatarUrl\":\"http://wx.qlogo.cn/mmopen/vi_32/1vZvI39NWFQ9XM4LtQpFrQJ1xlgZxx3w7bQxKARol6503Iuswjjn6nIGBiaycAjAtpujxyzYsrztuuICqIM5ibXQ/0\"}";
        String key = "HyVFkGl5F5OQWJZZaNzBBg==";
        String testStr = originStr + key;
        String expiredStr = "75e81ceda165f4ffa64f4068af58c64b8f54b88c";
        String resultStr = DigestUtils.sha1Hex(testStr);
        assertTrue(expiredStr.equalsIgnoreCase(resultStr));
    }

    /**
     * java基本数据类型: byte, short, int, long, float, double, char, boolean, 参数传递为值传递
     * 对象数据类型, 参数传递为引用传递(对象地址的值传递; java无指针，与c++的引用传递不同)
     * 字符串为不可变对象(好处为提高效率、线程安全)
     */
    @Test
    public void testStringStore() {

        //常量赋值，首先校验"123"在字符串常量池是否存在，若不存在，则在常量池创建String对象"123",然后a指向这个内存地址，之后所有的字符串对象(b)常量赋值均指向该地址，java中称为"字符串驻留"
        //所有字符串常量都会在编译之后自动驻留
        String a = "123";
        String b = "123";
        assertTrue(a == b);

        //new构造创建, 至少创建一个对象，也可能为两个。首先在heap中创建"123"字符串对象，然后c指向这个内存地址；然后判断字符串常量池是否有"123"，若无则创建
        String c = new String("123");
        String d = new String("123");
        assertTrue(c != d);
        assertTrue(c.equals(d));

        //intern方法返回常量池中字符串对象的引用
        assertTrue(c.intern() == d.intern());
    }

    /**
     * Vector, 线程安全的数据结构, 方法均由synchronized修饰(同一时间内只有一个线程能访问，效率较低)
     * 可实现自动增长的对象数组(动态数组), 可插入不同类的对象
     * 对于预先不知或者不愿预先定义数组大小, 并且需频繁地进行查找、插入、删除工作, 可考虑使用向量类
     */
    @Test
    public void testVector() {
        Vector vector = new Vector();
        vector.addElement("test");
        vector.addElement(100);
        vector.setElementAt(true, 1);
        vector.addElement("test");
        vector.addElement(24);
        vector.addElement(new Long(12));
        logger.info("vector lastIndexOf 'test' is: [{}]", vector.lastIndexOf("test"));
        Enumeration<String> elements = vector.elements();
        while (elements.hasMoreElements()) {
            logger.info("vector element: [{}]", String.valueOf(elements.nextElement()));
        }
    }

    /**
     * 正则预编译演示, Matcher非线程安全
     * A Matcher is created on on a precompiled regexp, while String.matches must recompile the regexp every time it executes, so it becomes more wasteful the more often you run that line of code.
     * 关于执行效率: https://www.baeldung.com/java-regex-performance
     */
    @Test
    public void testPattern() {
        String regexPattern = "^test\\w*$";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher("");
        String[] values = {"test", "test11", "testAa", "not", "test_", "test.."};
        for (String value : values) {
            matcher.reset(value);
            logger.info("value: [{}], matches: [{}]", value, matcher.matches());
        }
        String performanceTestStr = "test1234";
        matcher.reset(performanceTestStr);
        int executeTimes = 100000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < executeTimes; i++) {
            matcher.matches();
        }
        long intervalPattern = System.currentTimeMillis() - start;
        start = System.currentTimeMillis();
        for (int i = 0; i < executeTimes; i++) {
            performanceTestStr.matches(regexPattern);
        }
        long intervalString = System.currentTimeMillis() - start;
        logger.info("intervalPattern: [{}], intervalString: [{}]", intervalPattern, intervalString);
        assertTrue(intervalPattern < intervalString);
    }

    /**
     * 测试千分位
     * d为digital的缩写, \d为匹配数字字符, \D为匹配非数字字符
     * b为boundaries的缩写, \b为单词边界, \B为非单词边界
     * exp1(?=exp2) 表示 查找exp2前面的exp1
     * exp1(?!exp2) 表示 查找后面不是exp2的exp1
     * 千分位正则匹配为：\B(?=(\d{3})+(?!\d))
     */
    @Test
    public void testReg() {
        String originStr = "66122400600.04";

        //正则匹配
        String regexPattern = "\\B(?=(\\d{3})+(?!\\d))";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(originStr);
        String regResStr = matcher.replaceAll(",");
        String aimStr = "66,122,400,600.04";
        assertTrue(regResStr.equals(aimStr));

        //或使用NumberFormat
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        //设置之后不会有千分位，若不设置，默认有千分位
        //numberFormat.setGroupingUsed(false);
        String numResStr = numberFormat.format(Double.valueOf(originStr));
        assertTrue(numResStr.equals(aimStr));

        //或使用DecimalFormat: 千分位格式，保留两位小数
        DecimalFormat df = new DecimalFormat("###,###.##");
        String decimalResStr = df.format(Double.valueOf(originStr));
        assertTrue(decimalResStr.equals(aimStr));
    }

    /**
     * putIfAbsent为原子操作(不会出现线程安全问题), 用于并发多线程可能出现同一时刻对同一数据进行操作的情况
     * 若指定的键与值已有关联, 则返回关联的值, 否则将指定键与给定值关联并返回null
     */
    @Test
    public void testPutIfAbsent() {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        String key = "testKey";
        Integer firstValue = 111;
        Integer secondValue = 222;
        Integer firstOperateValue = map.putIfAbsent(key, firstValue);
        Integer secondOperateValue = map.putIfAbsent(key, secondValue);
        assert firstOperateValue == null;
        assertEquals(firstValue, secondOperateValue);
        logger.info("testPutIfAbsent, key is: [{}], curValue is : [{}], firstOperateValue: [{}], secondOperateValue: [{}]", key, map.get(key), firstOperateValue, secondOperateValue);
    }

    /**
     * computeIfAbsent, 指定键存在则返回已关联的值, 不存在则将指定键与给定值关联并返回给定值
     * computeIfAbsent为不存在时计算, computeIfPresent为存在时计算, 均为原子操作
     */
    @Test
    public void testComputeIfAbsent() {
        Map<String, List<Integer>> map = new ConcurrentHashMap<>();
        String key = "testKey";
        Integer firstValue = 111;
        Integer secondValue = 222;
        List<Integer> firstOperateList = map.computeIfAbsent(key, k -> new ArrayList<>());
        firstOperateList.add(firstValue);
        List<Integer> secondOperateList = map.computeIfAbsent(key, k -> new ArrayList<>());
        secondOperateList.add(secondValue);
        assert secondOperateList.contains(firstValue) && secondOperateList.contains(secondValue);
    }

    /**
     * computeIfPresent, 指定键存在则执行计算操作并与键关联, 返回计算后的值; 不存在则返回null
     */
    @Test
    public void testComputeIfPresent() {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        String key = "testKey";
        Integer firstValue = 111;
        Integer secondValue = 222;
        Integer firstOperate = map.computeIfPresent(key, (k, v) -> firstValue);
        assertNull(firstOperate);
        map.put(key, firstValue);
        Integer secondOperate = map.computeIfPresent(key, (k, v) -> secondValue);
        assertEquals(secondValue, secondOperate);
    }

    /**
     * 线程安全容器, 写时复制; 适用于读多写少的场景
     * 当向容器添加元素时, 不直接向当前容器添加, 而是先将当前容器进行Copy, 复制出新的容器, 然后向新的容器里添加元素, 添加完元素之后, 再将原容器的引用指向新的容器
     * 优点是可以并发读CopyOnWrite容器元素, 无需加锁实现更高并发, 因为当前容器不会添加任何元素; CopyOnWrite容器是读写分离的思想, 读和写不同的容器
     * 缺点是仅为了保证数据一致性, 添加到拷贝数据时还没有替换, 读的仍然是旧数据; 如果对象比较大, 频繁进行替换会消耗内存, 进而引发GC问题; 此时可考虑换其他容器如ConcurrentHashMap(分段锁)
     */
    @Test
    public void testCopyOnArrayList() {
        List<String> list = new CopyOnWriteArrayList<>();
        list.add("simpleUse");
        logger.info("CopyOnWriteArrayList element 0 is : [{}]", list.get(0));
    }

    /**
     * 读取文件内容并打印
     */
    @Test
    public void testReadFileContent() {
        String filePath = "E:" + File.separator + "desk_store" + File.separator + "test";
        File aimFile = new File(filePath);
        Long fileLength = aimFile.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        try (InputStream in = new FileInputStream(aimFile)) {
            in.read(fileContent);
            String contentStr = new String(fileContent, StandardCharsets.UTF_8);
            logger.info("filePath: [{}], contentStr: [{}]", filePath, contentStr);
        } catch (IOException e) {
            logger.error("testReadFileContent, IOException: [{}]", e);
        }
    }

}

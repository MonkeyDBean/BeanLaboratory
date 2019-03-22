package com.monkeybean.labo.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by MonkeyBean on 2018/7/26.
 */
public class OtherTest {

    @Test(expected = NumberFormatException.class)
    public void testNull() {
        Integer.valueOf(null);
    }

    //    @Test
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
            System.out.println("element" + i + ": " + list.get(i));
        }
        System.out.println("---***---");
        list.remove(1);
        list.remove("333");
        list.pop();
        for (int i = 0; i < list.size(); i++) {
            System.out.println("element" + i + ": " + list.get(i));
        }
    }

    //    @Test
    public void testSplit() {
        String testStr = "test";
        System.out.println("split 0 is: \n" + testStr.split(",")[0]);
    }

    //    @Test
    public void testLastIndex() {
        String origin = "file.11.jpg";
        String result = origin.substring(0, origin.lastIndexOf("."));
        System.out.println("origin: " + origin + "\nresult: " + result);
    }

    //    @Test
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
        System.out.println("list1Result: " + Arrays.toString(list1.toArray()));

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
        System.out.println("list4Result: " + list4ResultStr);
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
        System.out.println("JSON.toJSONString, result: " + result);
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

}

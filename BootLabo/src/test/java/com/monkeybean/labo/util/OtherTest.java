package com.monkeybean.labo.util;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

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
}

package com.monkeybean.labo.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by MonkeyBean on 2018/7/26.
 */
public class OtherTest {

    @Test(expected = NumberFormatException.class)
    public void testNull() {
        Integer.valueOf(null);
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
}

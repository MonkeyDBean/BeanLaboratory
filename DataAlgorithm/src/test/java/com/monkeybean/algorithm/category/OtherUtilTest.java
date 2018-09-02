package com.monkeybean.algorithm.category;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Test
    public void testForceCast() {
        Object b = null;
        Integer value = (Integer) b;
        System.out.println("testForceCast, value: " + value);
    }

}
package com.monkeybean.algorithm.category;

import org.junit.Test;

/**
 * Created by MonkeyBean on 2019/9/2.
 */
public class PalindromeStrTest {

    @Test
    public void verify() throws Exception {
        String str = "a, ; bA";
        int loopTime = 100000;
        System.out.println("originStr is: \"" + str + "\", loopTime is: " + loopTime);
        long curTime = System.currentTimeMillis();
        for (int i = 0; i < loopTime; i++) {
            PalindromeStr.verify(str, 1);
        }
        System.out.println("method1 execute time is: " + (System.currentTimeMillis() - curTime));
        curTime = System.currentTimeMillis();
        for (int i = 0; i < loopTime; i++) {
            PalindromeStr.verify(str, 2);
        }
        System.out.println("method2 execute time is: " + (System.currentTimeMillis() - curTime));
        curTime = System.currentTimeMillis();
        for (int i = 0; i < loopTime; i++) {
            PalindromeStr.verify(str, 3);
        }
        System.out.println("method3 execute time is: " + (System.currentTimeMillis() - curTime));
        curTime = System.currentTimeMillis();
        for (int i = 0; i < loopTime; i++) {
            PalindromeStr.verify(str, 4);
        }
        System.out.println("method4 execute time is: " + (System.currentTimeMillis() - curTime));
    }

}
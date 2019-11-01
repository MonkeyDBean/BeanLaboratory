package com.monkeybean.algorithm.category;

import org.junit.Test;

/**
 * Created by MonkeyBean on 2019/10/13.
 */

public class StringRelatedTest {

    @Test
    public void searchSubstring() {
        String str1 = "www.taobao.com";
        String str2 = "taobao";
        System.out.println("str1: " + str1 + ", str2:" + str2 + ", result: " + StringRelated.searchSubstring(str1, str2));
    }

    @Test
    public void charFrequency() {
        String[] strArray = {"www.taobao.com", "taobao", "asdf123123"};
        StringRelated.charFrequency(strArray);
    }

    @Test
    public void searchMaxLengthMethod() {
        String originStr1 = "01000100101";
        String originStr2 = "1111101";
        String originStr3 = "00000";
        int method1Res1 = StringRelated.searchMaxLengthMethod1(originStr1, 3);
        int method2Res1 = StringRelated.searchMaxLengthMethod2(originStr1, 3);
        int method1Res2 = StringRelated.searchMaxLengthMethod1(originStr1, 2);
        int method2Res2 = StringRelated.searchMaxLengthMethod2(originStr1, 2);
        int method1Res3 = StringRelated.searchMaxLengthMethod1(originStr1, 1);
        int method2Res3 = StringRelated.searchMaxLengthMethod2(originStr1, 1);
        int method1Res4 = StringRelated.searchMaxLengthMethod1(originStr1, 0);
        int method2Res4 = StringRelated.searchMaxLengthMethod2(originStr1, 0);
        int method1Res5 = StringRelated.searchMaxLengthMethod1(originStr2, 1);
        int method2Res5 = StringRelated.searchMaxLengthMethod2(originStr2, 1);
        int method1Res6 = StringRelated.searchMaxLengthMethod1(originStr2, 0);
        int method2Res6 = StringRelated.searchMaxLengthMethod2(originStr2, 0);
        int method1Res7 = StringRelated.searchMaxLengthMethod1(originStr3, 1);
        int method2Res7 = StringRelated.searchMaxLengthMethod2(originStr3, 1);
        int method1Res8 = StringRelated.searchMaxLengthMethod1(originStr3, 0);
        int method2Res8 = StringRelated.searchMaxLengthMethod2(originStr3, 0);
        assert method1Res1 == method2Res1;
        assert method1Res2 == method2Res2;
        assert method1Res3 == method2Res3;
        assert method1Res4 == method2Res4;
        assert method1Res5 == method2Res5;
        assert method1Res6 == method2Res6;
        assert method1Res7 == method2Res7;
        assert method1Res8 == method2Res8;
        System.out.println(method1Res1);
        System.out.println(method1Res2);
        System.out.println(method1Res3);
        System.out.println(method1Res4);
        System.out.println(method1Res5);
        System.out.println(method1Res6);
        System.out.println(method1Res7);
        System.out.println(method1Res8);
    }

    @Test
    public void searchLongestCyclicSubstring() {
        String originStr = "banana";
        StringRelated.searchLongestRepeatedSubstringMethod1(originStr);
        System.out.println("dynamic method, originString is: " + originStr
                + ", result is: " + StringRelated.searchLongestRepeatedSubstringMethod2(originStr));
        String originStr2 = "aaaaa";
        StringRelated.searchLongestRepeatedSubstringMethod1(originStr2);
        System.out.println("dynamic method, originString is: " + originStr2
                + ", result is: " + StringRelated.searchLongestRepeatedSubstringMethod2(originStr2));
    }

    @Test
    public void wordMatch() {
        char[][] charArray = {{'a', 'e', 'f'}, {'d', 'x', 'j'}, {'m', 'q', 'h'}};
        int arrayWidth = 3;
        String testStr1 = "adx";
        String testStr2 = "fjh";
        String testStr3 = "ada";
        String testStr4 = "axh";
        assert StringRelated.wordMatch(charArray, arrayWidth, testStr1);
        assert StringRelated.wordMatch(charArray, arrayWidth, testStr2);
        assert StringRelated.wordMatch(charArray, arrayWidth, testStr3);
        assert !StringRelated.wordMatch(charArray, arrayWidth, testStr4);
    }

}
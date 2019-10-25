package com.monkeybean.algorithm.category;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;

/**
 * 处理字符串相关
 * <p>
 * Created by MonkeyBean on 2019/9/27.
 */
public class StringRelated {

    /**
     * 奇偶打印计数
     */
    private static volatile int i = 1;

    public static void main(String[] args) {

        // 奇偶打印
        printOddAndEvenInorderByTwoThread();
    }

    /**
     * 两个线程交替打印1-100的整数，一个打印奇数，一个打印偶数，要求输出结果有序
     */
    public static void printOddAndEvenInorderByTwoThread() {
        final Object object = new Object();

        Runnable runnable = () -> {
            synchronized (object) {
                for (; i <= 100; ) {
                    System.out.println(Thread.currentThread().getName() + "\t" + (i++));
                    try {
                        object.notify();
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                object.notifyAll();
            }
        };

        Thread thread1 = new Thread(runnable, "odd Thread");
        Thread thread2 = new Thread(runnable, "even Thread");
        thread1.start();
        thread2.start();
    }

    /**
     * 查找子串在给定字符串第一次出现的位置
     * 输入：字符串str1，字符串str2
     * 输出：字符串str2在str1中第一次出现的位置，如果没有则返回-1
     * 例如：str1="www.taobao.com" str2="taobao", 输出为4
     *
     * @param str1 待查找的字符串
     * @param str2 子串
     * @return 查找成功则返回第一次出现的位置，失败则返回-1
     */
    public static int searchSubstring(String str1, String str2) {
        if (str1 == null || str2 == null || "".equals(str1) || str1.length() < str2.length()) {
            return -1;
        }
        int methodFlag = 1;
        int res1 = -1;
        int res2 = -1;
        try {
            res1 = (int) StringRelated.class.getMethod("searchMethod" + methodFlag, String.class, String.class).invoke(StringRelated.class, str1, str2);
            methodFlag = 2;
            res2 = (int) StringRelated.class.getMethod("searchMethod" + methodFlag, String.class, String.class).invoke(StringRelated.class, str1, str2);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        assert res1 == res2;
        return res1;
    }

    /**
     * 查找子串位置
     *
     * @param str1 待查找的字符串
     * @param str2 子串
     * @return 查找成功则返回第一次出现的位置，失败则返回-1
     */
    public static int searchMethod1(String str1, String str2) {
        Matcher matcher = Pattern.compile(str2).matcher(str1);
        if (matcher.find()) {
            return matcher.start();
        } else {
            return -1;
        }
    }

    /**
     * 查找子串位置(jdk8 indexOf实现方式)
     *
     * @param str1 待查找的字符串
     * @param str2 子串
     * @return 查找成功则返回第一次出现的位置，失败则返回-1
     */
    public static int searchMethod2(String str1, String str2) {
        char[] charArray1 = str1.toCharArray();
        char[] charArray2 = str2.toCharArray();
        int max = charArray1.length - charArray2.length;

        for (int i = 0; i <= max; i++) {

            // 查找首个相等字符的位置
            if (charArray1[i] != charArray2[0]) {
                while (i++ <= max && charArray1[i] != charArray2[0]) ;
            }

            // 匹配剩余字符
            if (i <= max) {
                int j = i + 1;
                int end = j + charArray2.length - 1;
                for (int k = 1; j < end && charArray1[j] == charArray2[k]; j++, k++) ;
                if (j == end) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 打印字符串出现频率
     * 输入：字符串数组 str[], 假设字符范围为ASCII码
     * 输出：这些字符串中字符的分布情况，按照降序排列
     *
     * @param strArray 给定的字符串数组
     */
    public static void charFrequency(String[] strArray) {
        if (strArray == null || strArray.length == 0) {
            return;
        }
        for (int i = 0; i < strArray.length; i++) {
            Map<Character, Integer> frequencyMap = new HashMap<>();
            char[] charArray = strArray[i].toCharArray();
            for (int j = 0; j < charArray.length; j++) {
                if (frequencyMap.get(charArray[j]) == null) {
                    frequencyMap.put(charArray[j], 1);
                } else {
                    frequencyMap.put(charArray[j], frequencyMap.get(charArray[j]) + 1);
                }
            }

            // 按值降序
            Map<Character, Integer> sortedMap = frequencyMap
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(comparingByValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                            LinkedHashMap::new));

            // 打印字符及频率
            System.out.println("\nstring is: " + strArray[i]);
            for (Map.Entry<Character, Integer> entry : sortedMap.entrySet()) {
                System.out.println("char is: " + entry.getKey() + ", frequency is: " + entry.getValue());
            }
        }
    }

    /**
     * 查找给定字符串连续1的最大长度
     * 序列索引差值, 推荐
     *
     * @param s 给定字符串, 格式为二进制形式, 如1000100101
     * @param k 可以将0翻转为1的个数
     * @return 返回最大长度
     */
    public static int searchMaxLengthMethod1(String s, int k) {

        //参数合法性判断
        if (s == null || s.isEmpty()) {
            return 0;
        }
        k = k > 0 ? k : 0;
        if (s.length() <= k) {
            return s.length();
        }

        //存储字符为0的索引位置及字符串两端外的索引位置(-1, n)
        List<Integer> indexList = new ArrayList<>();
        indexList.add(-1);
        int maxLength = 0;
        for (int i = 0; i <= s.length(); i++) {
            if (i == s.length() || s.charAt(i) == '0') {
                indexList.add(i);
            }
            if (indexList.size() >= k + 2) {

                //两端的索引间隔, 间隔值越大, 包含的字符1就越多
                int interval = indexList.get(indexList.size() - 1) - indexList.get(indexList.size() - k - 2) - 1;
                if (interval > maxLength) {
                    maxLength = interval;
                }
            } else if (i == s.length()) {
                maxLength = s.length();
            }
        }
        return maxLength;
    }

    /**
     * 查找给定字符串中连续1的最大长度
     * 双游标, 时间复杂度为O(n)
     *
     * @param s 给定字符串, 格式为二进制形式, 如1000100101
     * @param k 可以将0翻转为1的个数
     * @return 返回最大长度
     */
    public static int searchMaxLengthMethod2(String s, int k) {

        //参数合法性判断
        if (s == null || s.isEmpty() || k < 0) {
            return 0;
        }
        if (s.length() <= k) {
            return s.length();
        }

        //连续为1的子串最大长度
        int maxLength = k;

        //游标位置
        int head = -1;
        int tail = -1;

        //将0翻转为1的剩余次数
        int remainder = k;

        //记录字符为0的元素位置，用于尾游标的赋值
        Queue<Integer> markQueue = new ArrayDeque<>();

        //初始化head游标位置
        while (remainder > 0 && head + 1 < s.length()) {
            if (s.charAt(head + 1) == '0') {
                remainder--;
                markQueue.offer(head + 1);
            }
            head++;
        }
        while (head + 1 < s.length()) {
            if (s.charAt(head + 1) == '1') {
                head++;
            } else {
                break;
            }
        }
        if (head - tail > maxLength) {
            maxLength = head - tail;
        }

        //双游标移动
        while (head + 1 < s.length()) {
            head++;
            if (s.charAt(head) == '0') {
                if (markQueue.peek() != null) {
                    tail = markQueue.poll();
                    markQueue.offer(head);
                } else {
                    tail = head;
                }
                while (head + 1 < s.length() && s.charAt(head + 1) == '1') {
                    head++;
                }
            }
            if (head - tail > maxLength) {
                maxLength = head - tail;
            }
        }
        return maxLength;
    }

    /**
     * 查找最长重复子串
     * 示例: banana的最长重复子串为ana; aaaa的最长重复子串为aaa
     * 双指针查找, 滑动窗口
     *
     * @param originStr 给定字符串
     */
    public static void searchLongestRepeatedSubstringMethod1(String originStr) {
        if (originStr == null || originStr.length() <= 1) {
            return;
        }
        String aimSubstring = "";
        int start = 0;
        int end = 1;
        int cyclicCount = 0;
        while (start < originStr.length() - 1 && aimSubstring.length() < originStr.length() - start) {
            String searchSubStr = originStr.substring(start, end);
            if (originStr.indexOf(searchSubStr, start + 1) != -1 || end > originStr.length()) {
                end++;
                if (searchSubStr.length() > aimSubstring.length()) {
                    aimSubstring = searchSubStr;
                }
            } else {
                start++;
                end = start + 1;
            }
            cyclicCount++;
        }
        System.out.println("originStr is: " + originStr + ", aimSubstring is: " + aimSubstring
                + ", substringLength is: " + aimSubstring.length() + ", cyclicCount is: " + cyclicCount);
    }

    /**
     * 查找最长重复子串
     * 动态规划(此题目在leetCode对应编号为1044)
     * 后缀数组(可使用倍增算法或DC3算法构建), 本例使用倍增算法构建
     * reference: https://www.cnblogs.com/jinkun113/p/4743694.html, https://www.cnblogs.com/victorique/p/8480093.html
     *
     * @param originStr 给定字符串
     * @return 最长重复子串
     */
    public static String searchLongestRepeatedSubstringMethod2(String originStr) {
        if (originStr == null || originStr.length() <= 1) {
            return "";
        }
        int[] sa = getSuffixArray(originStr);
        int maxCommonLength = 0;
        int maxCommonLengthOffset = 0;

        //比较LCP(longest common prefix)
        for (int i = 1; i < sa.length; i++) {
            int offset1 = sa[i];
            int offset2 = sa[i - 1];
            int commonLength = 0;
            while (offset1 < originStr.length() && offset2 < originStr.length() && originStr.charAt(offset1) == originStr.charAt(offset2)) {
                offset1++;
                offset2++;
                commonLength++;
            }
            if (commonLength > maxCommonLength) {
                maxCommonLength = commonLength;
                maxCommonLengthOffset = sa[i];
            }
        }
        return originStr.substring(maxCommonLengthOffset, maxCommonLengthOffset + maxCommonLength);
    }

    /**
     * 获取后缀数组(后缀子串的有序数组)
     * 后缀数组排序过程图见image目录下的suffix_array.png
     *
     * @param originStr 给定字符串
     */
    private static int[] getSuffixArray(String originStr) {
        char[] charArr = originStr.toCharArray();

        //记录字符序列个数的数组, 初始化默认为0, 此处无需调用Arrays.fill(bucket, 0);
        int[] bucket = new int[255];

        //排名数组, 索引为后缀串首字符位置, 值为该串的排名分值(对于相同的字符, 排名分值相同)
        int[] rank = new int[charArr.length];

        //计算所有后缀串的排名分值
        for (int i = 0; i < charArr.length; i++) {
            bucket[charArr[i]]++;
        }
        for (int i = 1; i < bucket.length; i++) {
            bucket[i] = bucket[i - 1] + bucket[i];
        }
        for (int i = 0; i < charArr.length; i++) {
            rank[i] = bucket[charArr[i]] - 1;
        }

        //rank2存放后半部分后缀串的排名分值, 长度不够的设置为0
        int[] rank2 = new int[charArr.length];
        bucket = new int[charArr.length];

        //后半部分的排序, 索引为名次, 值为后缀串
        int[] sa2 = new int[charArr.length];

        //整体的排序
        int[] sa = new int[charArr.length];

        //对整体排序要赋一个排名分值
        int[] newRank = new int[charArr.length];

        for (int k = 1; k < charArr.length; k *= 2) {

            //后半部分的排名分值, 从前半部分得到
            for (int i = 0; i < charArr.length; i++) {
                rank2[i] = (i + k) < charArr.length ? rank[i + k] : 0;
            }

            //后半部分排序
            Arrays.fill(bucket, 0);
            for (int i = 0; i < charArr.length; i++) {
                bucket[rank2[i]]++;
            }
            for (int i = 1; i < bucket.length; i++) {
                bucket[i] = bucket[i - 1] + bucket[i];
            }
            for (int i = charArr.length - 1; i >= 0; i--) {
                int rk = --bucket[rank2[i]];
                sa2[rk] = i;
            }

            //对前半部分进行排序
            Arrays.fill(bucket, 0);
            for (int i = 0; i < charArr.length; i++) {
                bucket[rank[i]]++;
            }
            for (int i = 1; i < bucket.length; i++) {
                bucket[i] = bucket[i - 1] + bucket[i];
            }
            for (int i = charArr.length - 1; i >= 0; i--) {

                //顺序值是根据后半部分计算, 基数排序, 排好了个位数, 现在排十位数，如果十位数相同, 那么已排好的个位数顺序也不会乱
                int rk = --bucket[rank[sa2[i]]];
                sa[rk] = sa2[i];
            }

            //计算新的排序分值, 如果前半部分和后半部分的排序分值都不一样, 则终止
            newRank[sa[0]] = 0;
            boolean uniqueRank = true;
            for (int i = 1; i < charArr.length; i++) {
                newRank[sa[i]] = newRank[sa[i - 1]];
                if (rank[sa[i]] == rank[sa[i - 1]] && rank2[sa[i]] == rank2[sa[i - 1]]) {
                    uniqueRank = false;
                } else {
                    newRank[sa[i]] += 1;
                }
            }
            if (uniqueRank) {
                break;
            }
            System.arraycopy(newRank, 0, rank, 0, charArr.length);
        }
        return sa;
    }

}
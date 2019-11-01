package com.monkeybean.algorithm.category;

import java.util.*;

/**
 * 简单数据处理
 * <p>
 * Created by MonkeyBean on 2018/8/2.
 */
public class DataUtil {

    /**
     * 动态规划(Dynamic Programming)是多阶段最优化决策解决的过程, 每次决策依赖于当前状态, 又随即引起状态的转移, 一个决策序列是在变化的过程中产生。
     * 动态规划是一系列以空间换取时间的算法, 最优原理是原问题的最优解包含子问题的最优解。
     * 基本方法是为了节约重复求相同子问题的时间, 引入数组, 不管它们对最终解是否有用, 把所有子问题的解存于该数组中。
     * <p>
     * M*N矩阵中有不同的正整数, 经过这个格子, 就能获得相应价值的奖励, 从左上走到右下, 只能向下向右走, 求能够获得的最大价值。
     *
     * @param matrix 矩阵数据
     * @param m      矩阵行数
     * @param n      矩阵列数
     * @return 最大权值
     */
    public static int getMatrixHeavy(int[][] matrix, int m, int n) {

        //int m = matrix.length;
        //动态规划记录数组
        int[][] dpArray = new int[m][n];
        int sum = 0;
        for (int i = 0; i < m; i++) {
            sum += matrix[i][0];
            dpArray[i][0] = sum;
        }
        sum = 0;
        for (int j = 0; j < n; j++) {
            sum += matrix[0][j];
            dpArray[0][j] = sum;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {

                //到当前节点的最大权值 = 当前节点权重 + 到上一节点的最大权值
                dpArray[i][j] = matrix[i][j] + Math.max(dpArray[i - 1][j], dpArray[i][j - 1]);
            }
        }
        return dpArray[m - 1][n - 1];
    }

    /**
     * 给定n个整数(可能包含负数)组成的序列, 求该序列的最大子段和
     * 动态规划
     *
     * @param array 给定数组
     * @return 最大字段和
     */
    public static int maxSequenceSum(int[] array) {
        int[] dpArray = new int[array.length];
        dpArray[0] = array[0];
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            dpArray[i] = Math.max(array[i], array[i] + dpArray[i - 1]);
            max = dpArray[i] > max ? dpArray[i] : max;
        }
        return max;
    }

    /**
     * 斐波那契数列: 楼梯共N阶, 初始在第一阶, 每次只能上一阶或两阶, 走到第N阶共有多少走法
     * 递归
     *
     * @param n 阶数
     * @return 总情况数
     */
    public static int fibonacciRecursive(int n) {
        if (n <= 0) {
            return 0;
        } else if (n == 1) {
            return 1;
        } else {
            return fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2);
        }
    }

    /**
     * 斐波那契数列: 楼梯阶梯问题, 初始在第一阶
     * 动态规划
     *
     * @param n 阶数
     * @return 总情况数
     */
    public static int fibonacci(int n) {
        if (n <= 0) {
            return 0;
        } else if (n == 1 || n == 2) {
            return 1;
        } else {

            //动态规划子集, 开辟n+1长度内存, 实际也可开辟n长度的内存(阶数存储位置向左偏移1)
            int[] dpArray = new int[n + 1];

            //初始在第一阶, 到达第二阶情况数为1, 第三阶情况数为3
            dpArray[2] = 1;
            dpArray[3] = 2;
            for (int i = 4; i <= n; i++) {
                dpArray[i] = dpArray[i - 1] + dpArray[i - 2];
            }
            return dpArray[n];
        }
    }

    /**
     * 数组不相邻元素最大值, 数组元素均为正整数
     * 动态规划, 对应leetcode编号为198
     *
     * @param array 给定数组
     * @return 和的最大值
     */
    public static int findMaxUnContinuousSum(int[] array) {
        if (array == null || array.length == 0) {
            return 0;
        }
        int[] dp = new int[array.length + 1];
        dp[0] = 0;
        dp[1] = array[0];
        for (int i = 2; i <= array.length; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + array[i - 1]);
        }
        return dp[array.length];
    }

    /**
     * 查找所有连续自然数和为n的情况, 如15=1+2+3+4+5; 15=4+5+6; 15=7+8
     * 遍历查找
     * 时间复杂度较高, 不推荐
     *
     * @param n 给定正整数
     */
    public static void findContinuousSequenceMethod1(int n) {
        if (n < 3) {
            return;
        }
        for (int i = 1; i <= (n + 1) / 2; i++) {
            int sum = i;
            for (int j = i + 1; j <= (n + 1) / 2; j++) {
                sum += j;
                if (sum == n) {
                    printContinuousSequence(i, j, n);
                } else if (sum > n) {
                    break;
                }
            }
        }
    }

    /**
     * 查找所有连续自然数和为n的情况
     * 区间值累加
     *
     * @param n 给定正整数
     */
    public static void findContinuousSequenceMethod2(int n) {
        if (n < 3) {
            return;
        }

        //begin及end表示和为n的连续正数区间,middle表示n的中间数
        int begin = 1;
        int end = 2;
        int middle = (n + 1) / 2;
        int sum = begin + end;
        while (begin < middle) {
            if (sum == n) {
                printContinuousSequence(begin, end, n);

                //从begin+1开始重新计算sum的值
                begin++;
                end = begin + 1;
                sum = begin + end;
            } else if (sum > n) {

                //begin右移，减去最左边的数
                sum -= begin;
                begin++;
            } else {

                //end右移，添加一个数
                end++;
                sum += end;
            }
        }
    }

    /**
     * 查找所有连续自然数和为n的情况
     * 利用等差数列求和公式(a1表示首项值, n表示项数, d表示公差): S = n*a1 + n*(n-1)*d/2
     *
     * @param n 给定正整数
     */
    public static void findContinuousSequenceMethod3(int n) {
        if (n < 3) {
            return;
        }
        for (int i = 1; i <= (n + 1) / 2; i++) {

            //j为连续序列的项数
            for (int j = 1; j < (n + 1) / 2; j++) {

                //sum表示以i开头，到i后面j项为止的等差数列和
                int sum = i * j + (j * (j - 1) / 2);
                if (sum == n) {
                    printContinuousSequence(i, i + j - 1, n);
                } else if (sum > n) {
                    break;
                }
            }
        }
    }

    /**
     * 查找所有连续自然数和为n的情况
     * 利用等差数列求和公式(a1表示首项值, an表示末项值, n表示项数, d表示公差): S = (a1 + an) / 2 = (a1 + a1 + (n - 1)*d) / 2 = (2*a1 + n - 1) / 2, 即: ((2*S)/n - n + 1)/2 = a1
     * 此处参数n表示和S, 为确定值, 连续自然数项数最小为2, 最大不超过(n+1)/2, 循环项数即可
     * 时间复杂度低, 推荐
     *
     * @param n 给定正整数
     */
    public static void findContinuousSequenceMethod4(int n) {
        if (n < 3) {
            return;
        }
        for (int i = 2; i <= (n + 1) / 2; i++) {

            //((2*n)/i - i + 1)/2 = a1
            int temp = n * 2;
            if (temp % i != 0) {
                continue;
            }
            temp = temp / i - i + 1;
            if (temp == 0 || temp % 2 != 0) {
                continue;
            }
            int start = temp / 2;
            int end = start + i - 1;
            printContinuousSequence(start, end, n);
        }
    }

    /**
     * 格式化输出连续自然数
     *
     * @param begin 起始数字
     * @param end   结束数字
     * @param sum   自然数和
     */
    private static void printContinuousSequence(int begin, int end, int sum) {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(sum).append("=");
        for (int i = begin; i < end; i++) {
            sBuilder.append(i).append("+");
        }
        System.out.println(sBuilder.append(end).toString());
    }

    /**
     * 从数组选n个数, 和为m
     * 数组各元素值唯一, 无重复元素
     * 打印符合条件的数组
     *
     * @param originArray 给定数组
     * @param n           元素个数
     * @param m           元素和
     * @param selectArray 存放选出元素的数组, 长度为n
     * @param startIndex  起始索引, 上次选出的元素位置向后偏移1, 初始为0
     * @param inorder     数组是否有序(升序)
     */
    public static void pickNum(int[] originArray, int n, int m, int[] selectArray, int startIndex, boolean inorder) {
        if (originArray == null || originArray.length == 0 || n < 1) {
            return;
        }

        //提前终止部分情况
        if (inorder) {
            if (originArray[startIndex] > m) {
                return;
            }
            if (m < 0 && originArray[originArray.length - 1] < m) {
                return;
            }
        }

        //正常终止条件, 打印
        if (n == 1) {
            for (int i = startIndex; i < originArray.length; i++) {
                if (originArray[i] == m) {
                    selectArray[selectArray.length - n] = originArray[i];
                    System.out.println("one of successful result is: ");
                    for (int j = 0; j < selectArray.length; j++) {
                        System.out.print(selectArray[j] + "\t");
                    }
                    System.out.println("\n");
                    return;
                }
            }
            return;
        }

        //递归调用
        for (int i = startIndex; i <= originArray.length - n; i++) {
            selectArray[selectArray.length - n] = originArray[i];
            pickNum(originArray, n - 1, m - originArray[i], selectArray, i + 1, inorder);
        }
    }

    /**
     * 二分查找
     *
     * @param array 有序源数组
     * @param key   要查找的元素
     * @return 元素存在，则返回下标索引，不存在则返回-1
     */
    public static int binarySearch(int[] array, int key) {
        int left = 0;
        int right = array.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (array[mid] == key) {
                return mid;
            } else if (array[mid] < key) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    /**
     * 二分查找变种，查找第一个与key相等的元素
     *
     * @param array 有序源数组
     * @param key   要查找的元素
     * @return 元素存在，则返回下标索引，不存在则返回-1
     */
    public static int findFirstEqual(int[] array, int key) {
        int left = 0;
        int right = array.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (array[mid] >= key) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        if (left < array.length && array[left] == key) {
            return left;
        }
        return -1;
    }

    /**
     * 二分查找变种，查找第一个大于等于key的元素
     *
     * @param array 有序源数组
     * @param key   要查找的元素
     * @return 元素存在，则返回下标索引，不存在则返回-1
     */
    public static int findFirstEqualLarger(int[] array, int key) {
        int left = 0;
        int right = array.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (array[mid] >= key) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        if (left < array.length && array[left] >= key) {
            return left;
        }
        return -1;
    }

    /**
     * 二分查找变种，查找第一个大于key的元素
     *
     * @param array 有序源数组
     * @param key   要查找的元素
     * @return 元素存在，则返回下标索引，不存在则返回-1
     */
    public static int findFirstLarger(int[] array, int key) {
        int left = 0;
        int right = array.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (array[mid] > key) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        if (left < array.length && array[left] > key) {
            return left;
        }
        return -1;
    }

    /**
     * 二分查找变种，查找最后一个与key相等的元素
     *
     * @param array 有序源数组
     * @param key   要查找的元素
     * @return 元素存在，则返回下标索引，不存在则返回-1
     */
    public static int findLastEqual(int[] array, int key) {
        int left = 0;
        int right = array.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (array[mid] <= key) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        if (right >= 0 && array[right] == key) {
            return right;
        }
        return -1;
    }

    /**
     * 二分查找变种，查找最后一个小于key的元素
     *
     * @param array 有序源数组
     * @param key   要查找的元素
     * @return 元素存在，则返回下标索引，不存在则返回-1
     */
    public static int findLastEqualSmaller(int[] array, int key) {
        int left = 0;
        int right = array.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (array[mid] < key) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        if (right >= 0 && array[right] < key) {
            return right;
        }
        return -1;
    }

    /**
     * 列表元素乱序，抽取元素
     *
     * @param origin 原数组列表
     * @return 乱序后的数组列表
     */
    public static List<Integer> shufflePump(List<Integer> origin) {
        List<Integer> result = new ArrayList<>();
        Random random = new Random();
        for (int i = 0, tmpIndex, cycleNum = origin.size(); i < cycleNum; i++) {
//            tmpIndex = (int) (Math.random() * origin.size());
            tmpIndex = random.nextInt(origin.size());
            result.add(origin.remove(tmpIndex));
        }
        return result;
    }

    /**
     * 列表元素乱序，随机元素互换
     *
     * @param origin 原数组列表
     * @return 乱序后的数组列表
     */
    public static List<Integer> shuffleSwap(List<Integer> origin) {
        Random random = new Random();
        for (int i = 0, tmpElement, tmpIndex = random.nextInt(origin.size()); i < origin.size(); i++, tmpIndex = random.nextInt(origin.size())) {
            if (tmpIndex != i) {
                tmpElement = origin.get(tmpIndex);
                origin.set(tmpIndex, origin.get(i));
                origin.set(i, tmpElement);
            }
        }
        return origin;
    }

    /**
     * 数组乱序，随机元素互换
     * 数组第一位开始，随机一个数字，第一位和随机出的那位交换，依次遍历这个过程到数组最后一位。时间复杂度O(n)，空间复杂度O(1)
     * 与抽取元素方法相比：随机性更强，已随机的位数仍会与后面随机的数位交换；无需开辟新数组，空间复杂度降低
     *
     * @param origin 原数组
     * @return 乱序后的数组
     */
    public static int[] shuffleSwap(int[] origin) {
        Random random = new Random();
        for (int i = 0, tmpIndex; i < origin.length; i++) {
            tmpIndex = random.nextInt(origin.length);
            if (tmpIndex != i) {
                origin[tmpIndex] = origin[tmpIndex] ^ origin[i];
                origin[i] = origin[i] ^ origin[tmpIndex];
                origin[tmpIndex] = origin[tmpIndex] ^ origin[i];
            }
        }
        return origin;
    }

    /**
     * 输出a/b, 精确小数点后n位数
     *
     * @param a 分子
     * @param b 分母， 非零
     * @param n 小数位数, 非负数
     * @return 返回相除结果
     * @throws IllegalArgumentException 参数非法异常
     */
    public static String getAccurateDivide(int a, int b, int n) {
        if (b == 0 || n < 0) {
            throw new IllegalArgumentException();
        }
        String symbol = "";
        if (a > 0 && b < 0 || a < 0 && b > 0) {
            symbol = "-";
        }
        StringBuilder sBuilder = new StringBuilder(symbol);
        int intPrefix = a / b;
        a = Math.abs(a);
        b = Math.abs(b);
        if (n == 0) {
            if (a % b * 10 / b >= 5) {
                sBuilder.append(intPrefix + 1);
            } else {
                sBuilder.append(intPrefix);
            }
        } else {
            sBuilder.append(intPrefix).append(".");
            if (n == 1) {
                a = a % b * 10;
            } else {
                for (int i = 0; i < n - 1; i++) {
                    a = a % b * 10;
                    sBuilder.append(a / b);
                }
            }
            int c = a / b;
            a = a % b * 10;
            if (a / b >= 5) {
                sBuilder.append(c + 1);
            } else {
                sBuilder.append(c);
            }
        }
        return sBuilder.toString();
    }

    /**
     * 选质数，低效
     *
     * @param range 范围
     * @return 质数集合
     */
    public static Set<Integer> getPrimeNumInefficient(int range) {
        Set<Integer> result = new TreeSet<>();
        for (int i = 2; i <= range; i++) {
            boolean isPrime = true;
            for (int j = 2; j <= Math.sqrt(i); j++) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) result.add(i);
        }
        return result;
    }

    /**
     * 选质数，高效
     *
     * @param range 范围
     * @return 质数集合
     */
    public static Set<Integer> getPrimeNumEfficient(int range) {
        int[] flag = new int[range + 1];
        for (int i = 2; i <= Math.sqrt(range); i++) {
            for (int j = i; i * j <= range; j++) {
                flag[i * j] = 1;
            }
        }
        Set<Integer> result = new TreeSet<>();
        for (int i = 2; i <= range; i++) {
            if (flag[i] == 0) {
                result.add(i);
            }
        }
        return result;
    }

    /**
     * 获取传入时间与基准时间的相差天数, 基准时间设为1970-01-01; 传入时间在基准时间之后
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 返回相差天数，-1为参数不合法
     */
    public static int getIntervalDays(int year, int month, int day) {

        //参数合法性判断
        if (year >= 1970 && month > 0 && month <= 12 && day > 0 && day <= 31) {

            //小月份30天
            int[] minMonths = {4, 6, 9, 11};
            for (int element : minMonths) {
                if (month == element && day == 31) {
                    return -1;
                }
            }

            //闰年, 判断二月份天数
            boolean isLeap = year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
            if (month == 2) {
                if (day > 29) {
                    return -1;
                }
                if (isLeap && day == 29) {
                    return -1;
                }
            }

            //获取年间隔天数
            int yearDays = 0;
            int monthDays = 0;
            if (year != 1970) {
                yearDays = getYearIntervalDays(year - 1);
            }
            if (month != 1) {
                monthDays = getMonthIntervalDays(month - 1, isLeap);
            }
            return yearDays + monthDays + day - 1;
        }
        return -1;
    }

    /**
     * 获取传入年与基准年相差天数
     *
     * @param year 年
     */
    private static int getYearIntervalDays(int year) {
        if (year < 1970) {
            return 0;
        }
        return getYearDays(year) + getYearIntervalDays(year - 1);
    }

    /**
     * 获取某一年的总天数
     *
     * @param year 年
     */
    private static int getYearDays(int year) {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            return 366;
        } else {
            return 365;
        }
    }

    /**
     * 获取传入月份与基准月相差天数
     *
     * @param month  月
     * @param isLeap 是否为闰年
     */
    private static int getMonthIntervalDays(int month, boolean isLeap) {
        if (month < 1) {
            return 0;
        }
        return getMonthDays(month, isLeap) + getMonthIntervalDays(month - 1, isLeap);
    }

    /**
     * 获取某一月的总天数
     *
     * @param month  月
     * @param isLeap 是否为闰年
     */
    private static int getMonthDays(int month, boolean isLeap) {
        if (month == 2) {
            if (isLeap) {
                return 29;
            } else {
                return 28;
            }
        }
        int[] maxMonths = {1, 3, 5, 7, 8, 10, 12};
        for (int element : maxMonths) {
            if (month == element) {
                return 31;
            }
        }
        return 30;
    }

    /**
     * 简单分页
     *
     * @param n 待分页记录总数
     * @param m 每页记录数
     * @return 成功返回分页大小. 参数错误返回-1
     */
    public static int pageHelper(int n, int m) {
        if (n < 1 || m < 1) {
            return -1;
        }
        //return n % m == 0 ? n / m : n / m + 1;
        //或
        return (n - 1) / m + 1;
    }

    /**
     * 判断一个数是否为奇数
     *
     * @return true则为基数
     */
    public static boolean isOdd(int num) {
        //return num % 2 != 0;
        //或
        return (num & 1) == 1;
    }

    /**
     * 循环双向链表
     */
    public class DbLinkedList<T> {

        /**
         * 头元素引用
         */
        private Node<T> head;

        /**
         * 尾元素引用
         */
        private Node<T> tail;

        /**
         * 链表长度
         */
        private int size;

        public DbLinkedList() {
            this.head = this.tail = null;
            this.size = 0;
        }

        public DbLinkedList(Collection<T> collection) {
            this();
            addMultiX(collection);
        }

        /**
         * 头部添加元素
         */
        public void pushX(Node<T> node) {
            if (this.head == null) {
                this.head = this.tail = node;
            } else {
                this.tail.next = this.head.pre = node;
            }
            node.pre = this.tail;
            node.next = this.head;
            this.head = node;
            this.size++;
        }

        /**
         * 尾部添加元素
         */
        public void addX(Node<T> node) {
            if (this.tail == null) {
                this.head = this.tail = node;
            } else {
                this.tail.next = this.head.pre = node;
            }
            node.pre = this.tail;
            node.next = this.head;
            this.tail = node;
            this.size++;
        }

        /**
         * 添加多个元素
         */
        public void addMultiX(Collection<T> collection) {
            if (collection == null || collection.isEmpty()) {
                return;
            }
            Iterator<T> it = collection.iterator();
            while (it.hasNext()) {
                this.addX(new Node<>(it.next()));
            }
        }

        /**
         * 头部删除元素
         *
         * @return 删除成功返回原头部元素值，失败返回null
         */
        public T popX() {
            if (this.size > 0) {
                T element = this.head.value;
                this.head.next.pre = this.head.pre;
                this.head.pre.next = this.head.next;
                Node<T> tmp = this.head.next;
                this.head.next = this.head.pre = null;
                this.head = tmp;
                this.size--;
                if (this.size == 0) {
                    this.head = this.tail = null;
                }
                return element;
            }
            return null;
        }

        /**
         * 尾部删除元素
         *
         * @return 删除成功返回原尾部元素值，失败返回null
         */
        public T removeX() {
            if (this.size > 0) {
                T element = this.tail.value;
                this.tail.pre.next = this.tail.next;
                this.tail.next.pre = this.tail.pre;
                Node<T> tmp = this.tail.pre;
                this.tail.next = this.tail.pre = null;
                this.tail = tmp;
                this.size--;
                if (this.size == 0) {
                    this.head = this.tail = null;
                }
                return element;
            }
            return null;
        }

        /**
         * 删除索引位置元素
         *
         * @return 删除成功返回true, 索引超出链表长度返回false
         */
        public boolean deleteX(int index) {
            if (index >= this.size || index < 0) {
                return false;
            }
            if (index == 0) { //头元素
                this.popX();
            } else if (index == this.size - 1) { //尾元素
                this.removeX();
            } else {
                Node<T> temp;
                if (index < this.size / 2) { //前1/2元素
                    temp = this.head;
                    while (index-- > 0) {
                        temp = temp.next;
                    }
                } else { //后1/2元素
                    temp = this.tail;
                    index = this.size - 1 - index;
                    while (index-- > 0) {
                        temp = temp.pre;
                    }
                }
                temp.next.pre = temp.pre;
                temp.pre.next = temp.next;
                temp.pre = temp.next = null;
                this.size--;
            }
            return true;
        }

        /**
         * 去重
         */
        public void filterDuplicate() {
            if (this.size <= 1) {
                return;
            }
            Node<T> node = this.head;
            do {
                T nodeValue = node.value;
                int index = indexOf(nodeValue);
                int lastIndex = lastIndexOf(nodeValue);
                while (lastIndex != index) {
                    deleteX(lastIndex);
                    lastIndex = lastIndexOf(nodeValue);
                }
                node = node.next;
            } while (node != this.head);
        }

        /**
         * 获取索引位置元素
         *
         * @return 索引超出链表长度则返回null
         */
        public T get(int index) {
            if (index >= this.size || index < 0) {
                return null;
            }
            Node<T> temp = this.head;
            while (index-- > 0) {
                temp = temp.next;
            }
            return temp.value;
        }

        /**
         * 判断某个元素是否存在
         */
        public boolean isExist(T x) {
            if (this.size == 0) {
                return false;
            }
            Node<T> temp = this.head;
            do {
                if (temp.value == x) {
                    return true;
                }
                temp = temp.next;
            } while (temp != this.head);
            return false;
        }

        /**
         * 获取某个元素首次出现的的索引位置
         *
         * @return 返回-1为元素不存在
         */
        public int indexOf(T x) {
            if (this.size == 0) {
                return -1;
            }
            Node<T> temp = this.head;
            int index = 0;
            do {
                if (temp.value == x) {
                    return index;
                }
                temp = temp.next;
                index++;
            } while (temp != this.head);
            return -1;
        }

        /**
         * 获取某个元素最后一次出现的的索引位置
         *
         * @return 返回-1为元素不存在
         */
        public int lastIndexOf(T x) {
            if (this.size == 0) {
                return -1;
            }
            Node<T> temp = this.tail;
            int index = this.size - 1;
            do {
                if (temp.value == x) {
                    return index;
                }
                temp = temp.pre;
                index--;
            } while (temp != this.tail);
            return -1;
        }

        /**
         * 列表翻转
         */
        public void reverse() {
            if (this.size > 1) {
                Node<T> temp = this.head;
                do {
                    Node<T> nextTemp = temp.next;
                    Node<T> curTemp = temp;
                    temp.next = temp.pre;
                    temp.pre = curTemp;
                    temp = nextTemp;
                } while (temp != this.head);
                Node<T> tempRef = this.head;
                this.head = this.tail;
                this.tail = tempRef;
            }
        }

        /**
         * 输出元素
         */
        public void print() {
            if (this.size == 0) {
                return;
            }
            Node<T> temp = this.head;
            System.out.println("list size is " + this.size);
            do {
                System.out.println(temp.value);
                temp = temp.next;
            } while (temp != this.head);
        }

        /**
         * 获取链表长度
         */
        public int size() {
            return this.size;
        }

        /**
         * 清空链表
         */
        public void clear() {
            if (this.size == 0) {
                return;
            }
            Node<T> temp = this.head.next;
            while (temp != this.head) {
                Node<T> nextTemp = temp.next;
                temp.next = temp.pre = null;
                temp = nextTemp;
            }
            this.tail = this.head = this.head.next = this.head.pre = null;
            this.size = 0;
        }

    }

    /**
     * 链表节点
     */
    public class Node<T> {
        private Node<T> pre;
        private Node<T> next;
        private T value;

        public Node(T value) {
            this.value = value;
        }
    }

}

package com.monkeybean.algorithm.category;

import java.util.*;

/**
 * 简单数据处理
 * <p>
 * Created by MonkeyBean on 2018/8/2.
 */
public class DataUtil {

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
        Random random = new Random(System.currentTimeMillis());
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
        Random random = new Random(System.currentTimeMillis());
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
        Random random = new Random(System.currentTimeMillis());
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

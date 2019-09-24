package com.monkeybean.algorithm.category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 排序算法
 * <p>
 * Created by MonkeyBean on 2018/8/2.
 */
public class SortUtil {

    /**
     * 是否打印数组元素
     */
    private static boolean isPrint = true;

    public static void setIsPrint(boolean isPrint) {
        SortUtil.isPrint = isPrint;
    }

    /**
     * 交换数组元素
     *
     * @param data   源数组
     * @param indexA 要交换的数组索引A
     * @param indexB 要交换的数组索引B
     */
    private static void swap(int[] data, int indexA, int indexB) {
        data[indexA] = data[indexA] ^ data[indexB];
        data[indexB] = data[indexB] ^ data[indexA];
        data[indexA] = data[indexA] ^ data[indexB];
    }

    /**
     * 初始化数组元素
     *
     * @param n 数组长度
     */
    public static int[] initArray(final int n) {
        Random random = new Random();
        int field = n * 10;
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(field);
        }
        return array;
    }

    /**
     * 输出数组元素
     */
    public static void print(int[] data) {
        if (SortUtil.isPrint) {
            System.out.println("array is: ");
            for (int each : data) {
                System.out.println(each);
            }
        }
    }

    /**
     * 直接选择排序
     *
     * @param data 源数组
     */
    public static void selectSort(int[] data) {
        if (data == null || data.length < 2) {
            return;
        }
        for (int p = 0; p < data.length - 1; p++) {
            int minIndex = p;
            for (int i = p + 1; i < data.length; i++) {
                if (data[i] < data[minIndex]) {
                    minIndex = i;
                }
            }
            if (minIndex != p) {
                swap(data, p, minIndex);
            }
        }
    }

    /**
     * 直接插入排序
     *
     * @param data 源数组
     */
    public static void insertSort(int[] data) {
        if (data == null || data.length < 2) {
            return;
        }
        for (int p = 1; p < data.length; p++) {
            int i = 0;
            while (i < p) {
                if (data[p] < data[i]) {
                    break;
                }
                i++;
            }
            if (i < p) {
                int temp = data[p];
                int q = p;
                while (q > i) {
                    data[q] = data[q - 1];
                    q--;
                }
                data[i] = temp;
            }
        }
    }

    /**
     * 折半(二分)插入排序
     *
     * @param data 源数组
     */
    public static void binaryInsertionSort(int[] data) {
        if (data == null || data.length < 2) {
            return;
        }
        for (int p = 1; p < data.length; p++) {
            int temp = data[p];
            int left = 0;
            int right = p - 1;
            while (left <= right) {
                int mid = (left + right) / 2;
                if (data[mid] > data[p]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            for (int i = p - 1; i >= left; i--) {
                data[i + 1] = data[i];
            }
            data[left] = temp;
        }
    }

    /**
     * 希尔排序
     *
     * @param data 源数组
     */
    public static void shellSort(int[] data) {
        if (data == null || data.length < 2) {
            return;
        }
        int d = data.length / 2;
        while (d >= 1) {
            for (int k = 0; k < d; k++) {
                for (int i = k + d; i < data.length; i += d) {
                    int temp = data[i];
                    int j = i - d;
                    while (j >= k && data[j] > temp) {
                        data[j + d] = data[j];
                        j -= d;
                    }
                    data[j + d] = temp;
                }
            }
            d = d / 2;
        }
    }

    /**
     * 冒泡排序
     *
     * @param data 源数组
     */
    public static void bubbleSort(int[] data) {
        if (data == null || data.length < 2) {
            return;
        }
        for (int i = 0; i < data.length - 1; i++) {
            for (int j = 1; j < data.length - i; j++) {
                if (data[j] < data[j - 1]) {
                    swap(data, j, j - 1);
                }
            }
        }
    }

    /**
     * 数据分段
     *
     * @param data  源数组
     * @param left  起始位索引
     * @param right 结束位索引
     */
    private static int partition(int[] data, int left, int right) {
        int pivot = data[left];
        while (left < right) {
            while (left < right && data[right] > pivot) {
                right--;
            }
            data[left] = data[right];
            while (left < right && data[left] <= pivot) {
                left++;
            }
            data[right] = data[left];
        }
        data[left] = pivot;
        return left;
    }

    /**
     * 快速排序
     *
     * @param data 源数组
     */
    public static void quickSort(int[] data) {
        if (data == null || data.length < 2) {
            return;
        }
        int left = 0;
        int right = data.length - 1;
        if (left < right) {
            int p = partition(data, left, right);
            quickSort(data, left, p - 1);
            quickSort(data, p + 1, right);
        }
    }

    private static void quickSort(int[] data, int left, int right) {
        if (left < right) {
            int p = partition(data, left, right);
            quickSort(data, left, p - 1);
            quickSort(data, p + 1, right);
        }
    }

    /**
     * 数组有序段合并
     *
     * @param data  原数组
     * @param first 要合并的起始索引
     * @param mid   要合并的中间索引
     * @param last  要合并的结束索引
     * @param temp  临时数组
     */
    private static void mergeArray(int[] data, int first, int mid, int last, int[] temp) {
        int i = first;
        int j = mid + 1;
        int k = 0;
        while (i <= mid && j <= last) {
            if (data[i] <= data[j]) {
                temp[k++] = data[i++];
            } else {
                temp[k++] = data[j++];
            }
        }
        while (i <= mid) {
            temp[k++] = data[i++];
        }
        while (j <= last) {
            temp[k++] = data[j++];
        }
        for (i = 0; i < k; i++) {
            data[first + i] = temp[i];
        }
    }

    /**
     * 归并排序, 分而治之
     * Arrays.sort底层实现：jdk1.7以前使用归并排序，1.7后替换为Tim排序(归并排序的优化，归并排序和插入排序的混合排序算法)
     *
     * @param data 源数组
     */
    public static void mergeSort(int[] data) {
        if (data == null || data.length < 2) {
            return;
        }
        int[] temp = new int[data.length];
        int mid = (data.length - 1) / 2;

        //左边有序
        mergeSort(data, 0, mid, temp);

        //右边有序
        mergeSort(data, mid + 1, data.length - 1, temp);

        //合并两个有序数列
        mergeArray(data, 0, mid, data.length - 1, temp);

    }

    private static void mergeSort(int[] data, int first, int last, int[] temp) {
        if (first < last) {
            int mid = (first + last) / 2;
            mergeSort(data, first, mid, temp);
            mergeSort(data, mid + 1, last, temp);
            mergeArray(data, first, mid, last, temp);
        }
    }

    /**
     * 堆排序, 升序
     *
     * @param data 源数组
     */
    public static void heapSort(int[] data) {
        if (data == null || data.length < 2) {
            return;
        }

        //构建大顶堆，从第一个非叶子节点从下到上，从左到右调整结构
        for (int i = (data.length - 2) / 2; i >= 0; i--) {
            adjustHeap(data, i, data.length);
        }

        //交换堆顶元素与末尾元素，调整堆结构
        for (int j = data.length - 1; j > 0; j--) {
            swap(data, 0, j);
            adjustHeap(data, 0, j);
        }
    }

    /**
     * 调整大顶堆
     *
     * @param data   源数组
     * @param i      节点索引
     * @param length 需调整堆元素的数量
     */
    private static void adjustHeap(int[] data, int i, int length) {
        int temp = data[i];

        //从i节点的左节点开始
        for (int k = 2 * i + 1; k < length; k = 2 * k + 1) {

            //左节点小于右节点，k指向右节点
            if (k + 1 < length && data[k] < data[k + 1]) {
                k++;
            }

            //子节点大于父节点，子节点值复制给父节点；否则，跳出循环
            if (data[k] > temp) {
                data[i] = data[k];
                i = k;
            } else {
                break;
            }
        }
        data[i] = temp;
    }

    /**
     * 基数排序
     * 用于数据分布均匀的场景
     *
     * @param data   源数组
     * @param maxBit 数组最大数的位数
     */
    public static void radixSort(int[] data, int maxBit) {
        final int digitTypeNum = 10;

        //临时存放数据的数组(桶)
        int[] bucket = new int[data.length];

        //计数数组
        int[] count = new int[digitTypeNum];

        //k表示第几位，1表示个位，2表示十位，3表示百位
        for (int k = 1; k <= maxBit; k++) {

            //每次循环前, 将count置空，防止上次循环数据影响本次计数
            for (int i = 0; i < digitTypeNum; i++) {
                count[i] = 0;
            }

            //统计每个桶中的数据数量(第k位是0,1,2,3,4,5,6,7,8,9的数量)
            for (int i = 0; i < data.length; i++) {
                count[getFigure(data[i], k)]++;
            }

            //利用count[i]来确定数据放置的位置
            for (int i = 1; i < digitTypeNum; i++) {
                count[i] = count[i] + count[i - 1];
            }

            //将数据从后往前装入各个桶中
            for (int i = data.length - 1; i >= 0; i--) {
                int j = getFigure(data[i], k);
                bucket[count[j] - 1] = data[i];
                count[j]--;
            }

            //将桶中的数据取出，赋值给data
            for (int i = 0; i < data.length; i++) {
                data[i] = bucket[i];
            }
        }
    }

    /**
     * 获取整数的位数, 如1为1位, 100为3位, 2000为4位
     *
     * @param data 传入整数, int类型数据最大为10位
     * @return 整数的位数
     */
    public static int getDigitBit(int data) {

        //位数
        int j = 1;

        //int范围为-2147483648 ~ 2147483647
        for (long i = 10; i <= Integer.MAX_VALUE; i *= 10, j++) {
            if (i > data) {
                break;
            }
        }
        return j;
    }

    /**
     * 获取整数第n位的数据
     *
     * @param data 传入整数
     * @param n    位数; 如123, 第1位为3, 第2位为2, 第3位为1
     * @return 成功则返回整数的第n位; 参数不合法则返回-1; 位数不足补0, 如数字3看做03, 第2位为0
     */
    public static int getFigure(int data, int n) {
        if (n < 1 || n > 10) {
            return -1;
        }
        int divisor = 1;
        for (int i = 2; i <= n; i++) {
            divisor *= 10;
        }
        if (data < divisor) {
            return 0;
        }
        if (n < 10) {
            data = data % (divisor * 10);
        }
        return data / divisor;
    }

    /**
     * 多条件排序
     *
     * @param num    元素数量
     * @param ascend 是否升序，true为升序, false为降序
     * @return 排序后的列表
     */
    public List<SortObject<Double>> multiSort(int num, boolean ascend) {
        List<SortObject<Double>> resultList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            SortObject<Double> eachObject = new SortObject<>("element" + (i + 1), (int) (Math.random() * num) + 1, Math.random());
            resultList.add(eachObject);
        }
        resultList.sort((o1, o2) -> {
            if (o1.heavy == o2.heavy) {
                return o1.accurate - o2.accurate > 0 ? 1 : -1;
            }
            return o1.heavy - o2.heavy;
        });
        if (!ascend) {
            Collections.reverse(resultList);
        }
        return resultList;
    }

    /**
     * 测试多条件的排序对象
     */
    public class SortObject<T> {
        private String des;
        private int heavy;
        private T accurate;

        SortObject(String des, int heavy, T accurate) {
            this.des = des;
            this.heavy = heavy;
            this.accurate = accurate;
        }

        public String toString() {
            return "des=" + des + " &heavy=" + heavy + " &accurate=" + accurate;
        }
    }

}

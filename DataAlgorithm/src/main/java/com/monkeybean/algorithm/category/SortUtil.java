package com.monkeybean.algorithm.category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 排序算法
 * <p>
 * Created by MonkeyBean on 2018/8/2.
 */
public class SortUtil {

    //效率(时间复杂度和空间复杂度)，适用场景

    //插入排序

    //二分排序

    //希尔排序

    //归并排序

    //快速排序

    //堆排序

    //Tim排序（Arrays.sort底层实现：jdk1.7以前使用归并排序，1.7后替换为Tim排序）
    //以下为简单实现原理，jdk实际中加入了大量优化

    /**
     * 多条件排序
     *
     * @param num    元素数量
     * @param ascend 是否升序，ture为升序, false为降序
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

        public SortObject() {
        }

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

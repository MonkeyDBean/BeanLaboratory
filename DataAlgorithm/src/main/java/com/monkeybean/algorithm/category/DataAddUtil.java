package com.monkeybean.algorithm.category;

import java.util.Stack;

/**
 * 两数相加工具
 *
 * 给定两个非空链表来表示两个非负整数。位数按照正序方式存储，它们的每个节点只存储单个数字。将两数相加返回一个新的链表。
 * 你可以假设除了数字 0 之外，这两个数字都不会以零开头。
 * 示例：
 * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
 * 输出：8 -> 0 -> 7
 * 原因：243 + 564 = 807
 **/
public class DataAddUtil {

    public static void main(String[] args) {

        // 入参构建
        DataNode firstListNodeHead = new DataNode(2);
        DataNode firstListNode2 = new DataNode(4);
        firstListNodeHead.next = firstListNode2;
        DataNode firstListNode3 = new DataNode(3);
        firstListNode2.next = firstListNode3;
        DataNode firstTmp = firstListNodeHead;
        while (firstTmp != null) {
            System.out.print(firstTmp.value);
            firstTmp = firstTmp.next;
        }
        System.out.println();
        DataNode secondListNodeHead = new DataNode(5);
        DataNode secondListNode2 = new DataNode(6);
        secondListNodeHead.next = secondListNode2;
        DataNode secondListNode3 = new DataNode(4);
        secondListNode2.next = secondListNode3;
        DataNode secondTmp = secondListNodeHead;
        while (secondTmp != null) {
            System.out.print(secondTmp.value);
            secondTmp = secondTmp.next;
        }
        System.out.println();

        // 两数相加
        DataNode resultNodeHead = bitNumberNodeAdd(firstListNodeHead, secondListNodeHead);

        // 结果输出
        DataNode resTmp = resultNodeHead;
        while (resTmp != null) {
            System.out.print(resTmp.value);
            resTmp = resTmp.next;
        }
        System.out.println();

        // 两数组元素差值的绝对值最小
        int[] a = new int[3];
        a[0] = 3;
        a[1] = 6;
        a[2] = 7;
        int[] b = {1, 13, 4, 9, 10};
        System.out.println("absSmallest: " + absSmallest(a, b));
    }

    /**
     * 链表节点对应位数的两数相加: 栈方式
     *
     * m, n分别为两个链表长度
     * 时间复杂度: O(max(m, n))
     * 空间复杂度: O(m+n)
     */
    public static DataNode bitNumberNodeAdd(DataNode aNode, DataNode bNode) {

        // 压栈
        Stack<Integer> firstStack = new Stack<>();
        Stack<Integer> secondStack = new Stack<>();
        while (aNode != null) {
            firstStack.push(aNode.getValue());
            aNode = aNode.next;
        }
        while (bNode != null) {
            secondStack.push(bNode.getValue());
            bNode = bNode.next;
        }

        // 进位
        int carry = 0;
        DataNode headNode = null;
        while (!firstStack.isEmpty() || !secondStack.isEmpty() || carry > 0) {
            int sum = carry;
            sum += firstStack.isEmpty() ? 0 : firstStack.pop();
            sum += secondStack.isEmpty() ? 0 : secondStack.pop();
            DataNode curNode = new DataNode(sum % 10);
            curNode.next = headNode;
            headNode = curNode;
            carry = sum / 10;
        }
        return headNode;
    }

    /**
     * 两个升序的正整数数组int[] A 和 int[] B, 从A中选取一个数a, 从B中选取一个数b，求 ｜a - b | 的绝对值最小
     * 要求时间复杂度最低;
     */
    public static int absSmallest(int[] a,  int[] b) {
        int i = 0;
        int j = 0;
        int min = Integer.MAX_VALUE;
        while (i < a.length && j < b.length) {
            if (a[i] == b [j]) {
                min = 0;
                break;
            } else if (a[i] > b [j]) {
                if (a[i] - b[j] < min) {
                    min = a[i] - b[j];
                }
                j ++;
            } else {
                if (b[j] - a[i] < min) {
                    min = b[j] - a[i];
                }
                i ++;
            }
        }
        return min;
    }

}
package com.monkeybean.algorithm.category;

/**
 * 链表数据节点
 **/
public class DataNode {

    /**
     * 数值
     */
    int value;

    /**
     * 结点引用
     */
    DataNode next;

    DataNode() {}

    DataNode(int value) { this.value = value; }

    DataNode(int value, DataNode next) {
        this.value = value;
        this.next = next;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public DataNode getNext() {
        return next;
    }

    public void setNext(DataNode next) {
        this.next = next;
    }

}

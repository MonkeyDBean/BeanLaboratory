package com.monkeybean.algorithm.category.tree;

/**
 * 二叉树简单节点
 * <p>
 * Created by MonkeyBean on 2019/9/28.
 */
public class Node {

    /**
     * 数据
     */
    private int data;

    /**
     * 权重
     */
    private int heavy;

    /**
     * 左子节点引用
     */
    private Node lChild;

    /**
     * 右子节点引用
     */
    private Node rChild;

    public Node(int data) {
        this.data = data;
    }

    public Node(int data, int heavy) {
        this.data = data;
        this.heavy = heavy;
    }

    public Node(int data, Node lChild, Node rChild) {
        this.data = data;
        this.lChild = lChild;
        this.rChild = rChild;
    }

    public Node(int data, int heavy, Node lChild, Node rChild) {
        this.data = data;
        this.heavy = heavy;
        this.lChild = lChild;
        this.rChild = rChild;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getHeavy() {
        return heavy;
    }

    public void setHeavy(int heavy) {
        this.heavy = heavy;
    }

    public Node getlChild() {
        return lChild;
    }

    public void setlChild(Node lChild) {
        this.lChild = lChild;
    }

    public Node getrChild() {
        return rChild;
    }

    public void setrChild(Node rChild) {
        this.rChild = rChild;
    }
}

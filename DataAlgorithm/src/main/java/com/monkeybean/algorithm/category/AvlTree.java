package com.monkeybean.algorithm.category;

import java.util.Stack;

/**
 * AVL树(相比BST, 每个节点的左子树和右子树的高度差至多为1), 为解决BST极端情况下退化为链表而导致查找时间复杂度由O(logN)变成O(N)
 * <p>
 * AVL树插入节点时, 可能出现四种情况破坏平衡, 单旋或双旋解决:
 * 1.左-左型: 右旋调整
 * 2.右-右型: 左旋调整
 * 3.左-右型: 先左旋(调整后为第一种情况)后右旋
 * 4.右-左型: 先右旋(调整后为第二种情况)后左旋
 * <p>
 * Created by MonkeyBean on 2019/9/3.
 */
public class AvlTree {

    /**
     * 树的根节点引用
     */
    private AvlNode root = null;

    /**
     * 已知前序遍历和中序遍历, 可推导出二叉树各节点位置(前序遍历确定根节点, 中序遍历确定左右子树), 可得后序遍历
     * 已知中序遍历和后序遍历, 可推导出二叉树各节点位置后序遍历确定根节点, 中序遍历确定左右子树(后序遍历确定根节点, 中序遍历确定左右子树), 可得前序遍历
     * 已知前序遍历和后序遍历, 无法确定唯一的二叉树
     */
    public static void main(String[] args) {
        AvlTree tree = new AvlTree();
        for (int i = 1; i <= 10; i++) {
            tree.insert(i);
        }
        System.out.println("the data of root node is: " + tree.getRootData());

        System.out.println("the data of min node is: " + tree.getMinNodeValue());

        System.out.println("the data of max node is: " + tree.getMaxNodeValue());

        System.out.println("--- frontTraversalRecursive ---");
        tree.frontTraversalRecursive(tree.getRootNode());

        System.out.println("--- frontTraversalLoop ---");
        tree.frontTraversalLoop();

        System.out.println("--- inOrderTraversalRecursive ---");
        tree.inOrderTraversalRecursive(tree.getRootNode());

        System.out.println("--- inOrderTraversalLoop ---");
        tree.inOrderTraversalLoop();

        System.out.println("--- backTraversalRecursive ---");
        tree.backTraversalRecursive(tree.getRootNode());

        System.out.println("--- backTraversalLoop ---");
        tree.backTraversalLoop();
    }

    /**
     * 插入新节点数据
     *
     * @param data 待插入数据
     */
    public void insert(int data) {
        insert(data, root, false);
    }

    /**
     * 插入节点
     *
     * @param data        节点数据
     * @param T           根节点引用
     * @param isRecursive 是否为递归调用
     * @return 插入成功返回新节点或递归过程中的根节点
     */
    private AvlNode insert(int data, AvlNode T, boolean isRecursive) {
        boolean dataExist = false;
        if (T == null) {
            T = new AvlNode(data);
        } else if (data < T.data) {

            //向左子树递归插入
            T.lChild = insert(data, T.lChild, true);

            //若左子树的高度比右子树高度大2, 进行平衡调整
            if (getNodeHeight(T.lChild) - getNodeHeight(T.rChild) == 2) {

                //左-左型
                if (data < T.lChild.data) {
                    T = r_Rotate(T);
                } else {

                    //右-左型
                    T = r_L_Rotate(T);
                }
            }
        } else if (data > T.data) {

            //向右子树递归插入
            T.rChild = insert(data, T.rChild, true);

            //若右子树的高度比左子树高度大2, 进行平衡调整
            if (getNodeHeight(T.rChild) - getNodeHeight(T.lChild) == 2) {

                //右-右型
                if (data > T.rChild.data) {
                    T = l_Rotate(T);
                } else {

                    //左-右型
                    T = l_R_Rotate(T);
                }
            }
        } else {
            dataExist = true;
            System.out.println("data is exist, data is: " + data + ", node height is: " + T.height);
        }

        //插入前, 数据是否已存在
        if (!dataExist) {

            //重新计算T的高度
            setNodeHeight(T);

            //非递归过程, 即每次手动插入数据时, 树的根节点引用需重新指向
            if (!isRecursive) {
                root = T;
            }
        }
        return T;
    }

    /**
     * 左-右型，先左旋，再右旋
     *
     * @param oldRoot 不平衡位置的根节点(过程中)引用
     * @return 调整后的根节点(过程中引用
     */
    private AvlNode l_R_Rotate(AvlNode oldRoot) {

        //先对其左子树进行左旋
        oldRoot.lChild = l_Rotate(oldRoot.lChild);

        //再对当前节点右旋
        return r_Rotate(oldRoot);
    }

    /**
     * 右-左型，先右旋，再左旋
     *
     * @param oldRoot 不平衡位置的根节点(过程中引用
     * @return 调整后的根节点(过程中引用
     */
    private AvlNode r_L_Rotate(AvlNode oldRoot) {

        //先对其右子树进行右旋
        oldRoot.rChild = r_Rotate(oldRoot.rChild);

        //再对当前节点进行左旋
        return l_Rotate(oldRoot);
    }

    /**
     * 左旋
     *
     * @param oldRoot 不平衡位置的根节点(过程中引用
     * @return 调整后的根节点(过程中引用
     */
    private AvlNode l_Rotate(AvlNode oldRoot) {
        AvlNode newRoot;

        //旋转
        newRoot = oldRoot.rChild;
        oldRoot.rChild = newRoot.lChild;
        newRoot.lChild = oldRoot;

        //重新计算节点的高度
        setNodeHeight(oldRoot);
        setNodeHeight(newRoot);
        return newRoot;
    }

    /**
     * 右旋
     *
     * @param K2 不平衡位置的根节点(过程中引用
     * @return 调整后的根节点(过程中引用
     */
    private AvlNode r_Rotate(AvlNode K2) {
        AvlNode K1;

        //旋转
        K1 = K2.lChild;
        K2.lChild = K1.rChild;
        K1.rChild = K2;

        //重新计算节点的高度
        setNodeHeight(K2);
        setNodeHeight(K1);
        return K1;
    }

    /**
     * 设置节点高度
     *
     * @param T 节点引用
     */
    private void setNodeHeight(AvlNode T) {
        T.height = Math.max(getNodeHeight(T.lChild), getNodeHeight(T.rChild)) + 1;
    }

    /**
     * 获取节点的高度
     *
     * @param T 节点引用
     * @return T为null返回-1; T不为null返回实际高度
     */
    private int getNodeHeight(AvlNode T) {
        if (T == null) {
            return -1;
        } else {
            return T.height;
        }
    }

    /**
     * 前序遍历(先打印根节点, 再打印左子树, 最后打印右子树), 递归方式
     *
     * @param root 根节点(过程中)引用
     */
    public void frontTraversalRecursive(AvlNode root) {
        if (root == null) {
            return;
        }

        //打印当前节点数据
        printNodeData(root);

        //递归遍历左子树
        if (root.lChild != null) {
            frontTraversalRecursive(root.lChild);
        }

        //递归遍历右子树
        if (root.rChild != null) {
            frontTraversalRecursive(root.rChild);
        }
    }

    /**
     * 前序遍历(先打印根节点, 再打印左子树, 最后打印右子树), 循环方式
     */
    public void frontTraversalLoop() {
        if (root == null) {
            return;
        }
        Stack<AvlNode> stack = new Stack<>();
        AvlNode curNode = root;
        stack.push(curNode);
        while (curNode != null || !stack.isEmpty()) {
            if (curNode != null) {
                printNodeData(curNode);
                curNode = curNode.lChild;
            } else {
                curNode = stack.pop();
                curNode = curNode.rChild;
            }
            if (curNode != null) {
                stack.push(curNode);
            }
        }
    }

    /**
     * 中序(顺序)遍历(先打印左子树, 再打印根节点, 最后打印右子树), 递归方式
     *
     * @param root 根节点(过程中)引用
     */
    public void inOrderTraversalRecursive(AvlNode root) {
        if (root == null) {
            return;
        }

        //递归遍历左子树
        if (root.lChild != null) {
            inOrderTraversalRecursive(root.lChild);
        }

        //打印当前节点数据
        printNodeData(root);

        //递归遍历右子树
        if (root.rChild != null) {
            inOrderTraversalRecursive(root.rChild);
        }
    }

    /**
     * 中序(顺序)遍历(先打印左子树, 再打印根节点, 最后打印右子树), 循环方式
     */
    public void inOrderTraversalLoop() {
        if (root == null) {
            return;
        }
        Stack<AvlNode> stack = new Stack<>();
        AvlNode curNode = root;

        //先把root压入栈顶
        stack.push(curNode);

        //如果node为空，则判断stack元素是否为空
        while (curNode != null || !stack.isEmpty()) {
            if (curNode != null) {
                curNode = curNode.lChild;
            } else {
                curNode = stack.pop();
                printNodeData(curNode);
                curNode = curNode.rChild;
            }
            if (curNode != null) {
                stack.push(curNode);
            }
        }
    }

    /**
     * 后序遍历(先打印左子树, 再打印右子树, 最后打印根节点), 递归方式
     *
     * @param root 根节点(过程中)引用
     */
    public void backTraversalRecursive(AvlNode root) {
        if (root == null) {
            return;
        }

        //递归遍历左子树
        if (root.lChild != null) {
            backTraversalRecursive(root.lChild);
        }

        //递归遍历右子树
        if (root.rChild != null) {
            backTraversalRecursive(root.rChild);
        }

        //打印当前节点数据
        printNodeData(root);
    }

    /**
     * 后序遍历(先打印左子树, 再打印右子树, 最后打印根节点), 循环方式
     */
    public void backTraversalLoop() {
        if (root == null) {
            return;
        }
        Stack<AvlNode> stack = new Stack<>();
        AvlNode lastVisitNode = null;
        AvlNode curNode = root;
        stack.push(curNode);
        while (!stack.isEmpty()) {
            curNode = stack.peek();

            //判断curNode是否满足两个条件中一种
            //1.不存在左子节点和右子节点
            //2.存在左子节点或右子节点，但其左子节点或右子节点被访问过了
            if ((curNode.lChild == null && curNode.rChild == null)
                    || (lastVisitNode != null && (lastVisitNode == curNode.lChild || lastVisitNode == curNode.rChild))) {
                printNodeData(curNode);
                lastVisitNode = curNode;
                stack.pop();
            } else {

                //若非上述两种情况, 将curNode的右子节点和左子节点依次入栈, 保证之后每次取栈顶元素时, 左子节点在右子节点前被访问, 左右子节点都在根节点前被访问
                if (curNode.rChild != null) {
                    stack.push(curNode.rChild);
                }
                if (curNode.lChild != null) {
                    stack.push(curNode.lChild);
                }
            }
        }
    }

    /**
     * 获取树节点的最小值
     *
     * @return 树的节点数量不为0时返回节点数据最小值, 否则返回null
     */
    public Integer getMinNodeValue() {
        if (root == null) {
            return null;
        }
        AvlNode minNode = root;
        while (minNode.lChild != null) {
            minNode = minNode.lChild;
        }
        return minNode.data;
    }

    /**
     * 获取树节点的最大值
     *
     * @return 树的节点数量不为0时返回节点数据最大值, 否则返回null
     */
    public Integer getMaxNodeValue() {
        if (root == null) {
            return null;
        }
        AvlNode maxNode = root;
        while (maxNode.rChild != null) {
            maxNode = maxNode.rChild;
        }
        return maxNode.data;
    }

    /**
     * 获取根节点引用
     */
    public AvlNode getRootNode() {
        return root;
    }

    /**
     * 获取根节点数据
     *
     * @return 树的节点数量不为0时返回根节点数据, 否则返回null
     */
    public Integer getRootData() {
        return root != null ? root.data : null;
    }

    /**
     * 输出节点数据
     *
     * @param node 节点引用
     */
    public void printNodeData(AvlNode node) {
        System.out.println(node.data);
    }

    /**
     * 数据节点
     */
    private class AvlNode {

        /**
         * 存储数据
         */
        private int data;

        /**
         * 左子节点
         */
        private AvlNode lChild;

        /**
         * 右子节点
         */
        private AvlNode rChild;

        /**
         * 节点的高度(叶子节点高度设为0)
         */
        private int height;

        AvlNode(int data) {
            this.data = data;
            this.height = 0;
            this.lChild = this.rChild = null;
        }
    }

}

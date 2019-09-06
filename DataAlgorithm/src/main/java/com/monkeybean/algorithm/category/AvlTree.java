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
 * 平衡二叉树是为了解决二叉查找树退化为近似链表的问题, 可以将查找时间控制在O(logN),
 * 但是平衡二叉树实际使用并一定是最佳的, 因为平衡二叉树要求每个节点的左右子树高度差最多为1,
 * 此要求太严格, 导致每次插入/删除节点的时候, 几乎都会破坏平衡二叉树高度差限制的规则, 都需要旋转调整,
 * 对于插入/删除频繁的场景中, 平衡二叉树的性能大打折扣, 于是有了红黑树。
 * 红黑树是有颜色属性的二叉查找树, 是不太严格的平衡树, 是一种折中的方案。若仅从查找效率来看, 平衡树比红黑树快。
 * 红黑树有如下特点:
 * 1.具有二叉查找树的特点
 * 2.节点是红色或黑色
 * 3.根节点是黑色的
 * 4.每个叶子节点都是黑色的空节点, 即叶子节点不存储数据
 * 5.任何相邻的两个节点都不能同时为红色, 即红色节点被黑色节点隔开
 * 6.每个节点, 从该节点到达其可达的叶子节点的所有路径, 都包含相同数目的黑色节点
 * 红黑树应用场景: 如Jdk8的HashMap, TreeMap
 * <p>
 * Created by MonkeyBean on 2019/9/3.
 */
public class AvlTree {

    /**
     * 树的节点数量
     */
    private int treeSize = 0;

    /**
     * 树的根节点引用
     */
    private AvlNode treeRootNode = null;

    /**
     * 已知前序遍历和中序遍历, 可推导出二叉树各节点位置(前序遍历确定根节点, 中序遍历确定左右子树), 可得后序遍历
     * 已知中序遍历和后序遍历, 可推导出二叉树各节点位置(后序遍历确定根节点, 中序遍历确定左右子树), 可得前序遍历
     * 已知前序遍历和后序遍历, 无法确定唯一的二叉树
     */
    public static void main(String[] args) {
        AvlTree tree = new AvlTree();
        for (int i = 1; i <= 10; i++) {
            tree.insert(i);
        }
        tree.insert(2);
        tree.insert(4);
        tree.insert(6);
        System.out.println("the count of the treeNode is: " + tree.getTreeSize());

        System.out.println("the data of treeRootNode is: " + tree.getRootData());

        System.out.println("the successor data of treeRootNode is: " + tree.getSuccessorNodeValue(tree.getRootNode()));

        System.out.println("the min data of the tree is: " + tree.getMinNodeValue(tree.getRootNode()));

        System.out.println("the max data of the tree is: " + tree.getMaxNodeValue(tree.getRootNode()));

        System.out.println("\n--- frontTraversalRecursive of the tree ---");
        tree.frontTraversalRecursive(tree.getRootNode());

        System.out.println("\n--- frontTraversalLoop of the tree ---");
        tree.frontTraversalLoop(tree.getRootNode());

        System.out.println("\n--- inOrderTraversalRecursive of the tree ---");
        tree.inOrderTraversalRecursive(tree.getRootNode());

        System.out.println("\n--- inOrderTraversalLoop of the tree ---");
        tree.inOrderTraversalLoop(tree.getRootNode());

        System.out.println("\n--- backTraversalRecursive of the tree ---");
        tree.backTraversalRecursive(tree.getRootNode());

        System.out.println("\n--- backTraversalLoop of the tree ---");
        tree.backTraversalLoop(tree.getRootNode());

        int startData = 8;
        AvlNode childTree1 = tree.searchLoop(startData, tree.getRootNode());
        AvlNode childTree2 = tree.searchRecursive(startData, tree.getRootNode());
        System.out.println("\n--- inOrderTraversalRecursive of the childTree1 ---");
        tree.inOrderTraversalRecursive(childTree1);
        System.out.println("\nthe min data of the childTree1 is: " + tree.getMinNodeValue(childTree1));

        System.out.println("\n--- inOrderTraversalLoop of the childTree2 ---");
        tree.inOrderTraversalLoop(childTree2);
        System.out.println("\nthe max data of the childTree2 is: " + tree.getMaxNodeValue(childTree2));

        int searchData = 9;
        System.out.println("\nsearch data is: " + searchData + ", parent data is: " + tree.searchParentNodeValue(searchData));

        int removeData = 4;
        tree.remove(removeData);
        System.out.println("\n--- frontTraversalLoop of the tree after removing data ---");
        tree.frontTraversalLoop(tree.getRootNode());
        System.out.println("\n--- inOrderTraversalLoop of the tree after removing data ---");
        tree.inOrderTraversalLoop(tree.getRootNode());
        System.out.println("\n--- backTraversalLoop of the tree after removing data ---");
        tree.backTraversalLoop(tree.getRootNode());
        System.out.println("\nafter removing data, the count of the treeNode is: " + tree.getTreeSize());
        System.out.println("after removing data, the data of treeRootNode is: " + tree.getRootData());
    }

    /**
     * 前序遍历(先打印根节点, 再打印左子树, 最后打印右子树), 递归方式
     *
     * @param rootNode 根节点(过程中)引用
     */
    public void frontTraversalRecursive(AvlNode rootNode) {
        if (rootNode == null) {
            return;
        }

        //打印当前节点数据
        printNodeData(rootNode);

        //递归遍历左子树
        if (rootNode.lChild != null) {
            frontTraversalRecursive(rootNode.lChild);
        }

        //递归遍历右子树
        if (rootNode.rChild != null) {
            frontTraversalRecursive(rootNode.rChild);
        }
    }

    /**
     * 前序遍历(先打印根节点, 再打印左子树, 最后打印右子树), 循环方式
     *
     * @param rootNode 根节点引用
     */
    public void frontTraversalLoop(AvlNode rootNode) {
        if (rootNode == null) {
            return;
        }
        Stack<AvlNode> stack = new Stack<>();
        AvlNode curNode = rootNode;
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
     * @param rootNode 根节点(过程中)引用
     */
    public void inOrderTraversalRecursive(AvlNode rootNode) {
        if (rootNode == null) {
            return;
        }

        //递归遍历左子树
        if (rootNode.lChild != null) {
            inOrderTraversalRecursive(rootNode.lChild);
        }

        //打印当前节点数据
        printNodeData(rootNode);

        //递归遍历右子树
        if (rootNode.rChild != null) {
            inOrderTraversalRecursive(rootNode.rChild);
        }
    }

    /**
     * 中序(顺序)遍历(先打印左子树, 再打印根节点, 最后打印右子树), 循环方式
     *
     * @param rootNode 根节点引用
     */
    public void inOrderTraversalLoop(AvlNode rootNode) {
        if (rootNode == null) {
            return;
        }
        Stack<AvlNode> stack = new Stack<>();
        AvlNode curNode = rootNode;

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
     * @param rootNode 根节点(过程中)引用
     */
    public void backTraversalRecursive(AvlNode rootNode) {
        if (rootNode == null) {
            return;
        }

        //递归遍历左子树
        if (rootNode.lChild != null) {
            backTraversalRecursive(rootNode.lChild);
        }

        //递归遍历右子树
        if (rootNode.rChild != null) {
            backTraversalRecursive(rootNode.rChild);
        }

        //打印当前节点数据
        printNodeData(rootNode);
    }

    /**
     * 后序遍历(先打印左子树, 再打印右子树, 最后打印根节点), 循环方式
     *
     * @param rootNode 根节点引用
     */
    public void backTraversalLoop(AvlNode rootNode) {
        if (rootNode == null) {
            return;
        }
        Stack<AvlNode> stack = new Stack<>();
        AvlNode lastVisitNode = null;
        AvlNode curNode = rootNode;
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
     * 插入新节点数据
     *
     * @param data 待插入数据
     */
    public void insert(int data) {
        treeRootNode = insert(data, treeRootNode, null, false);
    }

    /**
     * 插入节点
     *
     * @param data        节点数据
     * @param rootNode    根节点(过程中)引用
     * @param parentNode  父节点引用
     * @param isRecursive 是否为递归调用
     * @return 插入成功返回根节点或递归过程中的根节点
     */
    private AvlNode insert(int data, AvlNode rootNode, AvlNode parentNode, boolean isRecursive) {
        boolean dataExist = false;
        if (rootNode == null) {
            rootNode = new AvlNode(data);
            rootNode.parent = parentNode;
            treeSize++;
        } else if (data < rootNode.data) {

            //向左子树递归插入
            rootNode.lChild = insert(data, rootNode.lChild, rootNode, true);

            //若左子树的高度比右子树高度大2, 进行平衡调整
            if (getNodeHeight(rootNode.lChild) - getNodeHeight(rootNode.rChild) == 2) {

                //左-左型
                if (data < rootNode.lChild.data) {
                    rootNode = r_Rotate(rootNode);
                } else {

                    //右-左型
                    rootNode = r_L_Rotate(rootNode);
                }
            }
        } else if (data > rootNode.data) {

            //向右子树递归插入
            rootNode.rChild = insert(data, rootNode.rChild, rootNode, true);

            //若右子树的高度比左子树高度大2, 进行平衡调整
            if (getNodeHeight(rootNode.rChild) - getNodeHeight(rootNode.lChild) == 2) {

                //右-右型
                if (data > rootNode.rChild.data) {
                    rootNode = l_Rotate(rootNode);
                } else {

                    //左-右型
                    rootNode = l_R_Rotate(rootNode);
                }
            }
        } else {
            dataExist = true;
            System.out.println("data has existed before insertion, data is: " + data + ", node height is: " + rootNode.height);
        }
        if (!dataExist) {

            //重新计算T的高度
            setNodeHeight(rootNode);

            //非递归过程, 即每次手动插入数据时, 树的根节点引用需重新指向
            if (!isRecursive) {
                treeRootNode = rootNode;
            }
        }
        return rootNode;
    }

    /**
     * 左-右型，先左旋，再右旋
     *
     * @param oldRoot 不平衡位置的根节点(过程中)引用
     * @return 调整后的根节点(过程中)引用
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
     * @param oldRoot 不平衡位置的根节点(过程中)引用
     * @return 调整后的根节点(过程中)引用
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
     * @param oldRoot 不平衡位置的根节点(过程中)引用
     * @return 调整后的根节点(过程中)引用
     */
    private AvlNode l_Rotate(AvlNode oldRoot) {
        AvlNode newRoot;

        //旋转
        newRoot = oldRoot.rChild;
        oldRoot.rChild = newRoot.lChild;
        newRoot.lChild = oldRoot;
        if (oldRoot.rChild != null) {
            oldRoot.rChild.parent = oldRoot;
        }
        newRoot.parent = oldRoot.parent;
        oldRoot.parent = newRoot;

        //重新计算节点的高度
        setNodeHeight(oldRoot);
        setNodeHeight(newRoot);
        return newRoot;
    }

    /**
     * 右旋
     *
     * @param oldRoot 不平衡位置的根节点(过程中)引用
     * @return 调整后的根节点(过程中)引用
     */
    private AvlNode r_Rotate(AvlNode oldRoot) {
        AvlNode newRoot;

        //旋转
        newRoot = oldRoot.lChild;
        oldRoot.lChild = newRoot.rChild;
        newRoot.rChild = oldRoot;
        if (oldRoot.lChild != null) {
            oldRoot.lChild.parent = oldRoot;
        }
        newRoot.parent = oldRoot.parent;
        oldRoot.parent = newRoot;

        //重新计算节点的高度
        setNodeHeight(oldRoot);
        setNodeHeight(newRoot);
        return newRoot;
    }

    /**
     * 平衡二叉树删除节点
     *
     * @param data 待删除的节点数据
     */
    public void remove(int data) {
        treeRootNode = remove(treeRootNode, data);
    }

    /**
     * 平衡二叉树删除节点
     *
     * @param rootNode 根节点(过程中)引用
     * @param data     待删除的节点数据
     */
    private AvlNode remove(AvlNode rootNode, int data) {
        if (rootNode == null) {
            return null;
        }

        //在左子树上删除
        if (data < rootNode.data) {
            rootNode.lChild = remove(rootNode.lChild, data);

            //在左子树上删除，右子树高度一定不小于左子树高度
            if (getNodeHeight(rootNode.rChild) - getNodeHeight(rootNode.lChild) > 1) {
                if (getNodeHeight(rootNode.rChild.lChild) > getNodeHeight(rootNode.rChild.rChild)) {
                    rootNode = r_L_Rotate(rootNode);
                } else {
                    rootNode = l_Rotate(rootNode);
                }
            }
        } else if (data > rootNode.data) {

            //在右子树上删除
            rootNode.rChild = remove(rootNode.rChild, data);
            if (getNodeHeight(rootNode.lChild) - getNodeHeight(rootNode.rChild) > 1) {
                if (getNodeHeight(rootNode.lChild.lChild) > getNodeHeight(rootNode.lChild.rChild)) {
                    rootNode = r_Rotate(rootNode);
                } else {
                    rootNode = l_R_Rotate(rootNode);
                }
            }
        } else {

            //删除的节点既有左子树又有右子树
            if (rootNode.lChild != null && rootNode.rChild != null) {

                //将失衡点的data域更改为其直接后继节点的data域
                rootNode.data = getSuccessorNodeValue(rootNode);

                //将问题转换为删除其直接后继节点
                rootNode.rChild = remove(rootNode.rChild, rootNode.data);
            } else {

                //只有左子树或者只有右子树或者为叶子结点的情况
                if (rootNode.lChild == null) {
                    if (rootNode.rChild != null) {
                        rootNode.rChild.parent = rootNode.parent;
                    }
                    rootNode.parent = null;
                    AvlNode tempNode = rootNode;
                    rootNode = rootNode.rChild;
                    tempNode.rChild = null;
                } else {
                    rootNode.lChild.parent = rootNode.parent;
                    rootNode.parent = null;
                    AvlNode tempNode = rootNode;
                    rootNode = rootNode.lChild;
                    tempNode.lChild = null;
                }
                treeSize--;
            }
        }

        //更新rootNode的高度值
        setNodeHeight(rootNode);
        return rootNode;
    }

    /**
     * 二叉树删除节点(非递归), 不调整平衡
     * <p>
     * 二叉树节点的删除分三种情况
     * 1.删除的节点为叶子节点: 直接删除
     * 2.删除的节点包含一个子节点(子树): 删除后移动子节点(子树)
     * 3.删除的节点包含两个子节点(子树): 与后继节点交换后转为上述两种情况
     * <p>
     * 关于后继节点: 比当前节点大的最小节点, 所以后继节点一定没有左子节点。当前节点有右子树时, 后继节点为右子树的最小节点。
     * 或者将删除的节点与前驱节点交换, 前驱节点是比当前节点小的最大节点, 前驱节点一定没有右子节点。
     *
     * @param data 待删除的节点数据
     * @return 删除成功则返回被删除的节点; 待删除的节点不存在则返回null
     */
    @Deprecated
    public AvlNode deleteNode(int data) {

        //找到待删除的节点
        AvlNode node = searchLoop(data, treeRootNode);
        if (node != null) {

            //仅有一个节点
            if (treeSize == 1) {
                treeRootNode = null;
                treeSize = 0;
            } else {
                if (node.lChild != null && node.rChild != null) {

                    //找到后继节点, 值交换后, 后继节点为叶子节点或有一个子节点，删除后继节点
                    AvlNode successorNode = getSuccessorNode(node);

                    //无需交换节点值, 仅将后继节点的值覆盖待删除节点即可
                    node.data = successorNode.data;

                    //待处理的节点引用赋值, 此时转为节点有一个子节点(子树)或没有子节点的情况
                    node = successorNode;
                }

                //有左子节点(子树)
                if (node.lChild != null) {
                    node.lChild.parent = node.parent;
                    if (node.parent != null) {
                        if (node.parent.lChild == node) {
                            node.parent.lChild = node.lChild;
                        } else {
                            node.parent.rChild = node.lChild;
                        }
                    }
                    node.lChild = null;
                } else if (node.rChild != null) {

                    //有右子节点(子树)
                    node.rChild.parent = node.parent;
                    if (node.parent != null) {
                        if (node.parent.lChild == node) {
                            node.parent.lChild = node.rChild;
                        } else {
                            node.parent.rChild = node.rChild;
                        }
                    }
                    node.rChild = null;
                } else {

                    //无子节点
                    if (node.parent.lChild == node) {
                        node.parent.lChild = null;
                    } else {
                        node.parent.rChild = null;
                    }
                }

                //调整从父节点到根节点的高度
                setCurToRootNodeHeight(node.parent);
                node.parent = null;
                treeSize--;
            }
            return node;
        }
        return null;
    }

    /**
     * 调整节点高度(从给定节点起, 到根节点)
     *
     * @param node 给定节点
     */
    private void setCurToRootNodeHeight(AvlNode node) {
        while (node != null) {
            setNodeHeight(node);
            node = node.parent;
        }
    }

    /**
     * 设置节点高度
     *
     * @param node 节点引用
     */
    private void setNodeHeight(AvlNode node) {
        if (node != null) {
            node.height = Math.max(getNodeHeight(node.lChild), getNodeHeight(node.rChild)) + 1;
        }
    }

    /**
     * 获取节点的高度
     *
     * @param node 节点引用
     * @return T为null返回-1; T不为null返回实际高度
     */
    private int getNodeHeight(AvlNode node) {
        if (node == null) {
            return -1;
        } else {
            return node.height;
        }
    }

    /**
     * 查找某个节点的后继节点数据
     *
     * @param node 给定的节点
     */
    private Integer getSuccessorNodeValue(AvlNode node) {
        if (node == null) {
            return null;
        }
        AvlNode successorNode = getSuccessorNode(node);
        return successorNode != null ? successorNode.data : null;
    }

    /**
     * 查找某个节点的后继节点
     *
     * @param node 给定的节点
     */
    private AvlNode getSuccessorNode(AvlNode node) {
        return node != null ? getMinNode(node.rChild) : null;
    }

    /**
     * 查找某个节点的前驱节点
     *
     * @param node 给定的节点
     */
    private AvlNode getPreNode(AvlNode node) {
        return node != null ? getMaxNode(node.lChild) : null;
    }

    /**
     * 查找某个节点(循环方式)
     *
     * @param data 待查找的节点数据
     * @param node 根节点
     * @return 数据存在则返回对应节点, 否则返回null
     */
    public AvlNode searchLoop(int data, AvlNode node) {
        while (node != null) {
            if (data < node.data) {
                node = node.lChild;
            } else if (data > node.data) {
                node = node.rChild;
            } else {
                return node;
            }
        }
        return null;
    }

    /**
     * 查找某个节点(递归方式)
     *
     * @param data 待查找的节点数据
     * @param node 父节点
     * @return 数据存在则返回对应节点, 否则返回null
     */
    public AvlNode searchRecursive(int data, AvlNode node) {
        if (node == null) {
            return null;
        }
        if (data < node.data) {
            return searchRecursive(data, node.lChild);
        } else if (data > node.data) {
            return searchRecursive(data, node.rChild);
        } else {
            return node;
        }
    }

    /**
     * 从树的根节点开始, 查找某个节点的父节点值
     * 不使用parent属性, 若使用, 直接curNode.parent即可获取父节点
     *
     * @param data 给定节点值
     * @return 若存在父节点, 则返回父节点值; 给定值的节点不存在或给定值的节点无父节点则返回null
     */
    public Integer searchParentNodeValue(int data) {
        AvlNode parentNode = searchParentNodeByData(data, treeRootNode);
        return parentNode != null ? parentNode.data : null;
    }

    /**
     * 查找某个节点的父节点值
     * 不使用parent属性, 若使用, 直接curNode.parent即可获取父节点
     *
     * @param node     给定节点
     * @param rootNode 查找的起始节点
     * @return 若存在父节点, 则返回父节点值; 给定节点不存在或给定节点无父节点则返回null
     */
    private Integer searchParentNodeValueByNode(AvlNode node, AvlNode rootNode) {
        if (rootNode == null) {
            return null;
        }
        AvlNode parentNode = searchParentNodeByNode(node, rootNode);
        return parentNode != null ? parentNode.data : null;
    }

    /**
     * 查找某个节点的父节点值
     * 不使用parent属性, 若使用, 直接curNode.parent即可获取父节点
     *
     * @param data     给定节点值
     * @param rootNode 查找的起始节点
     * @return 若存在父节点, 则返回父节点值; 给定值的节点不存在或给定值的节点无父节点则返回null
     */
    private Integer searchParentNodeValueByData(int data, AvlNode rootNode) {
        if (rootNode == null) {
            return null;
        }
        AvlNode parentNode = searchParentNodeByData(data, rootNode);
        return parentNode != null ? parentNode.data : null;
    }

    /**
     * 查找某个节点的父节点
     * 不使用parent属性, 若使用, 直接curNode.parent即可获取父节点
     *
     * @param node     给定节点
     * @param rootNode 查找的起始节点
     * @return 若存在父节点, 则返回父节点; 给定节点不存在或给定节点无父节点则返回null
     */
    private AvlNode searchParentNodeByNode(AvlNode node, AvlNode rootNode) {
        if (rootNode == null) {
            return null;
        }
        return node != null ? searchParentNodeByData(node.data, rootNode) : null;
    }

    /**
     * 查找某个节点的父节点
     * 不使用parent属性, 若使用, 直接curNode.parent即可获取父节点
     *
     * @param data     给定节点值
     * @param rootNode 查找的起始节点
     * @return 若存在父节点, 则返回父节点; 给点节点不存在或给定节点无父节点则返回null
     */
    private AvlNode searchParentNodeByData(int data, AvlNode rootNode) {
        AvlNode parentNode = null;
        while (rootNode != null) {
            if (data == rootNode.data) {
                break;
            } else {
                parentNode = rootNode;
                if (data < rootNode.data) {
                    rootNode = rootNode.lChild;
                } else {
                    rootNode = rootNode.rChild;
                }
            }
        }
        return parentNode;
    }

    /**
     * 获取树的最小节点值
     *
     * @param rootNode 根节点
     * @return 树的节点数量不为0时返回节点数据最小值, 否则返回null
     */
    public Integer getMinNodeValue(AvlNode rootNode) {
        AvlNode minNode = getMinNode(rootNode);
        return minNode != null ? minNode.data : null;
    }

    /**
     * 获取树的最小节点
     *
     * @param rootNode 根节点
     * @return 树的节点数量不为0时返回最小节点引用, 否则返回null
     */
    private AvlNode getMinNode(AvlNode rootNode) {
        if (rootNode == null) {
            return null;
        }
        AvlNode minNode = rootNode;
        while (minNode.lChild != null) {
            minNode = minNode.lChild;
        }
        return minNode;
    }

    /**
     * 获取树的最大节点值
     *
     * @param rootNode 根节点
     * @return 树的节点数量不为0时返回节点数据最大值, 否则返回null
     */
    public Integer getMaxNodeValue(AvlNode rootNode) {
        AvlNode maxNode = getMaxNode(rootNode);
        return maxNode != null ? maxNode.data : null;
    }

    /**
     * 获取树的最大节点
     *
     * @param rootNode 根节点
     * @return 树的节点数量不为0时返回最大节点引用, 否则返回null
     */
    private AvlNode getMaxNode(AvlNode rootNode) {
        if (rootNode == null) {
            return null;
        }
        AvlNode maxNode = rootNode;
        while (maxNode.rChild != null) {
            maxNode = maxNode.rChild;
        }
        return maxNode;
    }

    /**
     * 获取当前树节点总数
     */
    public int getTreeSize() {
        return treeSize;
    }

    /**
     * 获取根节点引用
     */
    public AvlNode getRootNode() {
        return treeRootNode;
    }

    /**
     * 获取根节点数据
     *
     * @return 树的节点数量不为0时返回根节点数据, 否则返回null
     */
    public Integer getRootData() {
        return treeRootNode != null ? treeRootNode.data : null;
    }

    /**
     * 输出节点数据
     *
     * @param node 节点引用
     */
    private void printNodeData(AvlNode node) {
        System.out.print(node.data + "\t");
    }

    /**
     * 数据节点
     * 成员变量无父节点引用, 查找父节点需从根节点遍历
     */
    private class AvlNode {

        /**
         * 存储数据
         */
        private int data;

        /**
         * 父节点
         */
        private AvlNode parent;

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
            this.parent = this.lChild = this.rChild = null;
        }
    }

}

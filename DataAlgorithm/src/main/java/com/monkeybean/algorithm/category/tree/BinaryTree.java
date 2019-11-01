package com.monkeybean.algorithm.category.tree;

import java.util.*;

/**
 * 简单无序二叉树
 * <p>
 * Created by MonkeyBean on 2019/9/28.
 */
public class BinaryTree {

    /**
     * 根节点
     */
    private Node treeRoot;

    /**
     * 存储数据节点的列表
     */
    private List<Node> nodeList;

    public static void main(String[] args) {

        //二叉树初始化
        BinaryTree tree = new BinaryTree();
        int num = 10;
        tree.initCompleteTree(num);

        //广度优先遍历
        tree.broadTraversal();
        //tree.broadTraversalByList();

        //判断是否为完全二叉树
        System.out.println("tree is complete: " + tree.isComplete());

        //查找两节点的公共父节点, 递归及非递归方式
        Node nodeA = tree.getNodeList().get(7);
        Node nodeB = tree.getNodeList().get(4);
        Node parentNodeA = tree.getNearestParent(tree.getTreeRoot(), nodeA, nodeB);
        Node parentNodeB = tree.getNearestParentNotRecursive(tree.getTreeRoot(), nodeA, nodeB);
        assert parentNodeA == parentNodeB;
        int dataA = 8;
        int dataB = 5;
        Node parentNode = tree.getNearestParent(tree.getTreeRoot(), dataA, dataB);
        if (parentNode != null) {
            System.out.println("dataA: " + dataA + ", dataB: " + dataB + ", parentNode data: " + parentNode.getData());
        }

        //查找根节点到给定节点的路径
        Node aimNode = tree.getNodeList().get(8);
        Stack<Node> stack = new Stack<>();
        searchNodePath(tree.getTreeRoot(), aimNode, stack);
        System.out.println("aimNode is: " + aimNode.getData() + ", rootNode is: " + tree.getTreeRoot().getData() + ", path is: ");
        for (int i = 0; i < stack.size(); i++) {
            System.out.print(stack.get(i).getData());
            if (i != (stack.size() - 1)) {
                System.out.print(" -> ");
            }
        }

        //蛇形打印
        tree.generateFixed();
        tree.snakePrint(tree.getTreeRoot());
    }

    /**
     * 递归查找根节点到某节点的路径
     *
     * @param root  根节点
     * @param node  给定节点
     * @param stack 保存节点路径的栈
     * @return 查找成功返回true
     */
    public static boolean searchNodePath(Node root, Node node, Stack<Node> stack) {
        if (root == null) {
            return false;
        }
        stack.push(root);
        if (root == node) {
            return true;
        }
        boolean searchRes = false;

        //查找左子树
        if (root.getlChild() != null) {
            searchRes = searchNodePath(root.getlChild(), node, stack);
        }

        //左子树未找到且右子树不为空, 查找右子树
        if (!searchRes && root.getrChild() != null) {
            searchRes = searchNodePath(root.getrChild(), node, stack);
        }
        if (!searchRes) {
            stack.pop();
        }
        return searchRes;
    }

    public Node getTreeRoot() {
        return treeRoot;
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    /**
     * 完全二叉树初始化
     *
     * @param num 节点数量
     */
    public void initCompleteTree(int num) {
        if (num < 1) {
            return;
        }
        int[] array = new int[num];
        for (int i = 0; i < num; i++) {
            array[i] = i + 1;
        }
        generateCompleteTree(array, Math.random() > 0.5);
    }

    /**
     * 根据二叉树特性生成完全二叉树
     *
     * @param array    数据数组
     * @param isRandom 若为true, 节点权重为随机值; 若为false, 权重与节点值相同
     */
    private void generateCompleteTree(int[] array, boolean isRandom) {
        final int maxHeavy = 1000;
        Random random = new Random();
        nodeList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            int heavy = array[i];
            if (isRandom) {
                heavy = random.nextInt(maxHeavy);
            }
            nodeList.add(new Node(array[i], heavy));
        }
        treeRoot = nodeList.get(0);
        for (int i = 0; i < nodeList.size() / 2; i++) {
            nodeList.get(i).setlChild(nodeList.get(i * 2 + 1));
            int rChildIndex = i * 2 + 2;
            if (rChildIndex < nodeList.size()) {
                nodeList.get(i).setrChild(nodeList.get(rChildIndex));
            }
        }
    }

    /**
     * 固定节点数量及排布的二叉树初始化, 手动建树
     */
    public void generateFixed() {
        Node node1 = new Node(1, 3);
        Node node2 = new Node(2, 2);
        Node node3 = new Node(3, 1);
        Node node4 = new Node(4, 2);
        Node node5 = new Node(5, 3);
        Node node6 = new Node(6, 2);
        Node node7 = new Node(7, 1);
        Node node8 = new Node(8, 4);
        Node node9 = new Node(9, 1);
        Node node10 = new Node(10, 6);
        treeRoot = node1;
        node1.setlChild(node2);
        node1.setrChild(node3);
        node2.setlChild(node4);
        node2.setrChild(node5);
        node3.setlChild(node6);
        node3.setrChild(node7);
        node4.setlChild(node8);
        node6.setlChild(node9);
        node6.setrChild(node10);
    }

    /**
     * 广度优先遍历
     */
    public void broadTraversal() {
        Queue<Node> queue = new ArrayDeque<>();
        if (treeRoot != null) {
            queue.offer(treeRoot);
        }
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            System.out.println("data: " + node.getData() + ", heavy: " + node.getHeavy());
            if (node.getlChild() != null) {
                queue.offer(node.getlChild());
            }
            if (node.getrChild() != null) {
                queue.offer(node.getrChild());
            }
        }
    }

    /**
     * 广度优先遍历, 直接遍历列表
     */
    private void broadTraversalByList() {
        if (nodeList == null) {
            return;
        }
        for (int i = 0; i < nodeList.size(); i++) {
            System.out.println("index: " + i + ", data: " + nodeList.get(i).getData() + ", heavy: " + nodeList.get(i).getHeavy());
        }
    }

    /**
     * 蛇形打印二叉树, 即第一行从左到右的顺序打印, 第二行从右到左的顺序打印, 第三行从左到右...
     * 此题对应leetcode编号为103
     *
     * @param root 根节点
     **/
    public void snakePrint(Node root) {
        if (root == null) {
            return;
        }
        System.out.println();
        List<List<Integer>> list = snakeLevelOrderMethod1(root);

        //逐层打印
        System.out.println("Binary Tree Zigzag Level Order Traversal, Method1:");
        listTravel(list);
        list = snakeLevelOrderMethod2(root);
        System.out.println("Binary Tree Zigzag Level Order Traversal, Method2:");
        listTravel(list);
        list = snakeLevelOrderMethod3(root);
        System.out.println("Binary Tree Zigzag Level Order Traversal, Method3:");
        listTravel(list);
    }

    /**
     * 打印给定list
     *
     * @param list 二维列表
     */
    private void listTravel(List<List<Integer>> list) {
        for (List<Integer> levelList : list) {
            for (Integer data : levelList) {
                System.out.print(data + "\t");
            }
            System.out.println();
        }
    }

    /**
     * 锯齿层级遍历
     * 通过队列实现
     *
     * @param root 根节点
     * @return 层级节点数据列表
     */
    private List<List<Integer>> snakeLevelOrderMethod1(Node root) {
        List<List<Integer>> allList = new ArrayList<>();
        int level = 0;
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            List<Integer> eachList = new ArrayList<>();

            //同一层节点数量
            int size = queue.size();
            while (size > 0) {
                Node curNode = queue.poll();
                eachList.add(curNode.getData());
                if (curNode.getlChild() != null) {
                    queue.offer(curNode.getlChild());
                }
                if (curNode.getrChild() != null) {
                    queue.offer(curNode.getrChild());
                }
                size--;
            }
            if ((level & 1) == 1) {
                Collections.reverse(eachList);
            }
            allList.add(eachList);
            level++;
        }
        return allList;
    }

    /**
     * 锯齿层级遍历
     * 通过两个栈实现
     *
     * @param root 根节点
     * @return 层级节点数据列表
     */
    private List<List<Integer>> snakeLevelOrderMethod2(Node root) {
        List<List<Integer>> allList = new ArrayList<>();
        boolean startLeft = true;
        Stack<Node> evenNodeStack = new Stack<>();
        Stack<Node> oddNodeStack = new Stack<>();
        evenNodeStack.push(root);
        List<Integer> eachList = new ArrayList<>();
        while (!evenNodeStack.isEmpty() || !oddNodeStack.isEmpty()) {
            if (startLeft) {
                if (!evenNodeStack.isEmpty()) {
                    Node curNode = evenNodeStack.pop();
                    eachList.add(curNode.getData());
                    if (curNode.getlChild() != null) {
                        oddNodeStack.push(curNode.getlChild());
                    }
                    if (curNode.getrChild() != null) {
                        oddNodeStack.push(curNode.getrChild());
                    }
                    if (evenNodeStack.isEmpty()) {
                        startLeft = !startLeft;
                        allList.add(eachList);
                        eachList = new ArrayList<>();
                    }
                }
            } else {
                if (!oddNodeStack.isEmpty()) {
                    Node curNode = oddNodeStack.pop();
                    eachList.add(curNode.getData());
                    if (curNode.getrChild() != null) {
                        evenNodeStack.push(curNode.getrChild());
                    }
                    if (curNode.getlChild() != null) {
                        evenNodeStack.push(curNode.getlChild());
                    }
                    if (oddNodeStack.isEmpty()) {
                        startLeft = !startLeft;
                        allList.add(eachList);
                        eachList = new ArrayList<>();
                    }
                }
            }
        }
        return allList;
    }

    /**
     * 锯齿层级遍历
     * 通过递归实现
     *
     * @param root 根节点
     * @return 层级节点数据列表
     */
    private List<List<Integer>> snakeLevelOrderMethod3(Node root) {
        List<List<Integer>> allList = new ArrayList<>();
        traversal(root, allList, 0);
        return allList;
    }

    /**
     * 通过dps的思想实现锯齿遍历
     *
     * @param root    根节点
     * @param allList 数据存储列表
     * @param level   层级
     */
    private void traversal(Node root, List<List<Integer>> allList, int level) {
        if (root == null) {
            return;
        }
        if (allList.size() < level + 1) {
            allList.add(new ArrayList<>());
        }
        if ((level & 1) == 1) {
            allList.get(level).add(0, root.getData());
        } else {
            allList.get(level).add(root.getData());
        }
        traversal(root.getlChild(), allList, level + 1);
        traversal(root.getrChild(), allList, level + 1);
    }

    /**
     * 判断当前树是否为完全二叉树
     *
     * @return 若为完全二叉树则返回true
     */
    public boolean isComplete() {
        Queue<Node> queue = new LinkedList<>();
        if (treeRoot != null) {
            queue.offer(treeRoot);
        }
        while (!queue.isEmpty()) {
            Node node = queue.poll();

            // 左右节点都存在
            if (node.getlChild() != null && node.getrChild() != null) {
                queue.offer(node.getlChild());
                queue.offer(node.getrChild());
            } else if (node.getlChild() == null && node.getrChild() != null) {

                // 左子节点为空, 右子节点不为空, 则一定不为完全二叉树
                return false;
            } else {

                // 左子节点不为空，右子节点为空 或 当前节点为叶子节点, 则该节点之后的所有节点均为叶子节点
                // 即 node.getlChild() != null && node.getrChild() == null || node.getlChild() == null && node.getrChild() == null
                if (node.getlChild() != null) {
                    queue.offer(node.getlChild());
                }
                while (!queue.isEmpty()) {
                    node = queue.poll();
                    if (node.getlChild() != null || node.getrChild() != null) {
                        return false;
                    }
                }
                return true;
            }
        }
        return true;
    }

    /**
     * 递归查找两节点的最近公共父节点, 方法调用前提为传入参数合法且两节点存在公共父节点
     *
     * @param root  根节点
     * @param nodeA 节点A
     * @param nodeB 节点B
     * @return 查找成功则返回公共节点
     */
    public Node getNearestParent(Node root, Node nodeA, Node nodeB) {

        // 边界条件为空节点或两节点中的其中一个
        if (root == null || root == nodeA || root == nodeB) {
            return root;
        }

        // 若root的左子节点是A,B的公共父节点, 则在node的右子树中查询A、B的结果必然为null; 反之亦然
        Node left = getNearestParent(root.getlChild(), nodeA, nodeB);
        Node right = getNearestParent(root.getrChild(), nodeA, nodeB);

        // 当node的左右子树查询结果都不为空时, 则该点即为最近公共父节点
        if (left != null && right != null) {
            return root;
        } else if (left != null) {
            return left;
        } else {
            return right;
        }
    }

    /**
     * 递归查找两节点的最近公共父节点
     * 方法重载, 便于测试, 前提是树的节点值不重复
     *
     * @param root  根节点
     * @param dataA 节点A的数据
     * @param dataB 节点B的数据
     * @return 查找成功则返回公共节点
     */
    public Node getNearestParent(Node root, int dataA, int dataB) {
        if (root == null || root.getData() == dataA || root.getData() == dataB) {
            return root;
        }
        Node left = getNearestParent(root.getlChild(), dataA, dataB);
        Node right = getNearestParent(root.getrChild(), dataA, dataB);
        if (left != null && right != null) {
            return root;
        } else if (left != null) {
            return left;
        } else {
            return right;
        }
    }

    /**
     * 非递归查找两节点的最近公共父节点
     *
     * @param root  根节点
     * @param nodeA 节点A
     * @param nodeB 节点B
     */
    public Node getNearestParentNotRecursive(Node root, Node nodeA, Node nodeB) {
        Stack<Node> stackA = new Stack<>();
        Stack<Node> stackB = new Stack<>();
        boolean searchResA = searchNodePath(root, nodeA, stackA);
        boolean searchResB = searchNodePath(root, nodeB, stackB);

        //删除给定节点
        if (searchResA && searchResB) {
            stackA.pop();
            stackB.pop();
            while (!stackA.isEmpty()) {
                Node tempA = stackA.pop();
                for (Node tempB : stackB) {
                    if (tempA == tempB) {
                        return tempA;
                    }
                }
            }
        }
        return null;
    }

}

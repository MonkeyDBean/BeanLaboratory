package com.monkeybean.algorithm.category.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Solution {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(3);
        TreeNode node1 = new TreeNode(9);
        TreeNode node2 = new TreeNode(20);
        TreeNode node3 = new TreeNode(15);
        TreeNode node4 = new TreeNode(7);
        root.left = node1;
        root.right = node2;
        node2.left = node3;
        node2.right = node4;
        List<List<Integer>> result = zigzagLevelOrder(root);
        for (List<Integer> inner : result) {
            for (Integer each : inner) {
                System.out.print(String.valueOf(each) + '\t');
            }
            System.out.println();
        }
    }

    /**
     * 分层遍历
     */
    public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        Queue<TreeNode> midProcess = new LinkedList<>();
        int flag = 0;
        midProcess.add(root);
        while (!midProcess.isEmpty()) {
            List<Integer> layerResult = new ArrayList<>();
            int elementSize = midProcess.size();
            for (int i = 0; i < elementSize; i++) {
                TreeNode temp = midProcess.poll();
                if (temp.left != null) {
                    midProcess.add(temp.left);
                }
                if (temp.right != null) {
                    midProcess.add(temp.right);
                }
                if (flag % 2 == 0) {
                    layerResult.add(temp.val);
                } else {
                    layerResult.add(0, temp.val);
                }
            }
            flag ++;
            result.add(layerResult);
        }
        return result;
    }

}

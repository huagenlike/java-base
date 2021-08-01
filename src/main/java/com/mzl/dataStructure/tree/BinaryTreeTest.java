package com.mzl.dataStructure.tree;

/**
 * @author lihuagen
 * @version 1.0
 * @className: BinaryTreeTest
 * @description: 二叉树
 * @date 2021/7/22 9:45
 * 二叉树的优势：
 *  在实际使用时会根据链表和有序数组等数据结构的不同优势进行选择。有序数组的优势在于二分查找，链表的优势在于数据项的插入和数据项的删除。
 *  但是在有序数组中插入数据就会很慢，同样在链表中查找数据项效率就很低。
 *  综合以上情况，二叉树可以利用链表和有序数组的优势，同时可以合并有序数组和链表的优势，二叉树也是一种常用的数据结构。
 * 二叉树的构成：
 *  二叉树由节点（node）和边组成。节点分为根节点、父节点、子节点。
 * 二叉树搜索：
 *  二叉树一个节点左子节点的关键字小于这个节点，右子节点关键字大于或等于这个父节点。
 */
public class BinaryTreeTest {

    private BinaryTreeNode root;

    // 在插入节点的过程中其实也就是对tree遍历的过程，最终根据条件遍历到左右节点为null时进行添加新的节点。
    public void insert(int iData, double dData) {
        // 创建node节点
        BinaryTreeNode newNode = new BinaryTreeNode();
        newNode.iData = iData;
        newNode.dData = dData;
        // 判断root node是否为null
        if (root == null) {
            root = newNode;
        } else {
            BinaryTreeNode current = root;
            BinaryTreeNode parent;

            while (true) {
                // 保存当current变为null之前的那一个父节点
                parent = current;
                // 插入左节点
                if (iData < current.iData) {
                    current = current.leftNode;
                    if (current == null) {
                        parent.leftNode = newNode;
                        return;
                    }
                }
                // 插入右节点
                else {
                    current = current.rightNode;
                    if (current == null) {
                        parent.rightNode = newNode;
                        return;
                    }
                }
            }
        }
    }

}

// 创建一个树的节点
// 每个node存放两个数据
// 一个左node引用和一个右node引用
class BinaryTreeNode {
    public int iData;
    public double dData;
    public BinaryTreeNode leftNode;
    public BinaryTreeNode rightNode;

    //显示树节点信息
    public void showNode() {
        System.out.println("{ " + iData + "," + dData + " }");
    }
}


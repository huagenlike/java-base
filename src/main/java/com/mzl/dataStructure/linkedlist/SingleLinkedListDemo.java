package com.mzl.dataStructure.linkedlist;

import java.util.*;

/**
 * 数组模拟队列
 */
public class SingleLinkedListDemo {

    public static void main(String[] args) {
        //进行测试
        //先创建节点
        HeroNode hero1 = new HeroNode(1, "宋江", "及时雨");
        HeroNode hero2 = new HeroNode(2, "卢俊义", "玉麒麟");
        HeroNode hero3 = new HeroNode(3, "吴用", "智多星");
        HeroNode hero4 = new HeroNode(4, "林冲", "豹子头");

        System.out.println("======== singleLinkedList 添加 ========");
        //创建要给链表
        SingleLinkedList singleLinkedList = new SingleLinkedList();
        singleLinkedList.add(hero1);
        singleLinkedList.add(hero4);
        singleLinkedList.add(hero2);
        singleLinkedList.add(hero3);
//
//        // 遍历
//        singleLinkedList.list();
//
//        System.out.println("======== singleLinkedList2 添加时新增排序规则 ========");
//        //创建要给链表
//        SingleLinkedList singleLinkedList2 = new SingleLinkedList();
//        singleLinkedList2.addByOrder(hero1);
//        singleLinkedList2.addByOrder(hero4);
//        singleLinkedList2.addByOrder(hero2);
//        singleLinkedList2.addByOrder(hero3);
//
//        // 遍历
//        singleLinkedList2.list();
//
//        HeroNode updateHero3 = new HeroNode(3, "吴用11", "智多星11");
//        singleLinkedList.update(updateHero3);
//
//        System.out.println("======修改后数据======");
//        // 遍历
//        singleLinkedList.list();
//
//        System.out.println("======删除后数据======");
//        singleLinkedList.del(3);
//        // 遍历
//        singleLinkedList.list();
//
//        System.out.println("list长度 => " + getLength(singleLinkedList.getHead()));
//
//        System.out.println("====== 逆序打印 ======");
//        reversePrint(singleLinkedList.getHead());
//
        System.out.println("====== 将单链表反转 ======");
        reversetList(singleLinkedList.getHead());
        singleLinkedList.list();
//
//        System.out.println("====== 查找单链表中的倒数第k个结点 ======");
//        HeroNode node = findLastIndexNode(singleLinkedList.getHead(), 1);
//        System.out.println(node);
//
//        System.out.println("====== 合并两个有序的单链表，合并之后的链表依然后续 ======");
        SingleLinkedList linklist1 = create_linklist1();
        SingleLinkedList linklist2 = create_linklist2();
        linklist1.list();
        System.out.println("============");
        linklist2.list();
        mergeSingleLinkedList(linklist1.getHead(), linklist2.getHead());
        System.out.println("======= 合并后 =======");
        linklist1.list();
    }

    /**
     * 将单链表反转（可以利用栈这个数据结构，将各个节点压入到栈中，然后利用栈的先进后出的特点，就实现了逆序打印的效果）
     *
     * @param head
     */
    public static void reversePrint(HeroNode head) {
        if (head.next == null) {
            return;
        }
        Stack<HeroNode> nodeStack = new Stack<>();
        HeroNode temp = head.next;
        while (temp != null) {
            nodeStack.push(temp);
            temp = temp.next;
        }
        while (nodeStack.size() > 0) {
            System.out.println(nodeStack.pop());
        }
    }

    /**
     * 将单链表反转
     *
     * @param head
     */
    public static void reversetList(HeroNode head) {
        if (head.next == null || head.next.next == null) {
            return;
        }
        //定义一个辅助的指针(变量)，帮助我们遍历原来的链表
        HeroNode temp = head.next;
        //指向当前节点[temp]的下一个节点
        HeroNode next = null;
        HeroNode reverseHead = new HeroNode(0, "", "");
        //遍历原来的链表，每遍历一个节点，就将其取出，并放在新的链表reverseHead 的最前端
        while (temp != null) {
            //先暂时保存当前节点的下一个节点，因为后面需要使用
            next = temp.next;
            //将cur的下一个节点指向新的链表的最前端
            temp.next = reverseHead.next;
            //将cur 连接到新的链表上
            reverseHead.next = temp;
            //让cur后移
            temp = next;
        }
        //将head.next 指向 reverseHead.next , 实现单链表的反转
        head.next = reverseHead.next;
    }

    //查找单链表中的倒数第k个结点 【新浪面试题】
    //思路
    //1. 编写一个方法，接收head节点，同时接收一个index
    //2. index 表示是倒数第index个节点
    //3. 先把链表从头到尾遍历，得到链表的总的长度 getLength
    //4. 得到size 后，我们从链表的第一个开始遍历 (size-index)个，就可以得到
    //5. 如果找到了，则返回该节点，否则返回nulll
    public static HeroNode findLastIndexNode(HeroNode head, int index) {
        if (head.next == null) {
            return null;
        }
        int length = getLength(head);
        if (index < 0 || index > length) {
            return null;
        }
        HeroNode temp = head.next;
        for (int i = 0; i < length - index; i++) {
            temp = temp.next;
        }
        return temp;
    }

    /**
     * 获取到单链表的节点的个数(如果是带头结点的链表，需求不统计头节点)
     *
     * @param head 链表的头节点
     * @return 返回的就是有效节点的个数
     */
    public static int getLength(HeroNode head) {
        if (head.next == null) {
            return 0;
        }
        int length = 0;
        HeroNode temp = head.next;
        while (temp != null) {
            length++;
            temp = temp.next;
        }
        return length;
    }

    /**
     * 合并两个有序的单链表，合并之后的链表依然后续
     */
    public static void mergeSingleLinkedList(HeroNode head1, HeroNode head2) {
        if (head1.next == null && head2.next == null) {
            System.out.println("两个链表都为空，无法合并");
            return;
        }
        HeroNode temp = head1;
        if (temp.next == null) {
            temp.next = head2.next;
        } else {
            //将辅助节点的next指向链表1
            temp.next = head1.next;
            //创建辅助节点2帮我们遍历链表2
            HeroNode temp2 = head2.next;
            //指向辅助节点的下一个节点
            HeroNode next = null;
            while (temp2 != null) {
                //链表1遍历完了以后，直接把链表2中剩余的接在链表1后面
                if (temp.next == null) {
                    temp.next = temp2;
                    break;
                }
                if (temp2.no <= temp.next.no) {
                    // 先暂时保存链表2中当前节点的下一个节点，方便后续使用
                    next = temp2.next;
                    // 将temp2的下一个节点指向temp的下一个节点
                    temp2.next = temp.next;
                    // 将temp2连接到链表1上
                    temp.next = temp2;
                    //temp2后移
                    temp2 = next;
                }
                //temp后移
                temp = temp.next;
            }
        }
    }

    public static SingleLinkedList create_linklist1() {
        HeroNode hero1 = new HeroNode(1, "宋江", "及时雨");
        HeroNode hero2 = new HeroNode(2, "卢俊义", "玉麒麟");
        HeroNode hero3 = new HeroNode(5, "吴用", "智多星");
        HeroNode hero4 = new HeroNode(8, "林冲", "豹子头");

        SingleLinkedList singleLinkedList = new SingleLinkedList();
        singleLinkedList.addByOrder(hero1);
        singleLinkedList.addByOrder(hero4);
        singleLinkedList.addByOrder(hero2);
        singleLinkedList.addByOrder(hero3);

        return singleLinkedList;
    }

    public static SingleLinkedList create_linklist2() {
        HeroNode hero2 = new HeroNode(3, "关胜", "大刀");
        HeroNode hero1 = new HeroNode(5, "吴用", "智多星");
        HeroNode hero3 = new HeroNode(7, "秦明", "霹雳火");
        HeroNode hero4 = new HeroNode(9, "呼延灼", "双鞭");

        SingleLinkedList singleLinkedList = new SingleLinkedList();
        singleLinkedList.addByOrder(hero1);
        singleLinkedList.addByOrder(hero4);
        singleLinkedList.addByOrder(hero2);
        singleLinkedList.addByOrder(hero3);

        return singleLinkedList;
    }
}


/**
 * 定义SingleLinkedList 管理我们的英雄
 */
class SingleLinkedList {
    /**
     * 先初始化一个头节点, 头节点不要动, 不存放具体的数据
     */
    private HeroNode head = new HeroNode(0, "", "");

    // 返回头节点
    public HeroNode getHead() {
        return head;
    }

    public void add(HeroNode heroNode) {
        HeroNode temp = head;
        while (true) {
            if (temp.next == null) {
                break;
            }
            temp = temp.next;
        }
        temp.next = heroNode;
    }

    // 第二种方式在添加英雄时，根据排名将英雄插入到指定位置
    // (如果有这个排名，则添加失败，并给出提示)
    public void addByOrder(HeroNode heroNode) {
        HeroNode temp = head;
        boolean flag = false;
        while (true) {
            if (temp.next == null) {
                break;
            }
            if (temp.next.no > heroNode.no) {
                break;
            } else if (temp.next.no == heroNode.no) {
                flag = true;
                break;
            }
            temp = temp.next;
        }
        if (flag) {
            System.out.printf("准备插入的英雄的编号 %d 已经存在了, 不能加入\n", heroNode.no);
        } else {
            heroNode.next = temp.next;
            temp.next = heroNode;
        }
    }

    // 修改节点的信息, 根据no编号来修改，即no编号不能改.
    // 说明
    // 1. 根据 newHeroNode 的 no 来修改即可
    public void update(HeroNode newHeroNode) {
        if (head.next == null) {
            System.out.println("链表为空~");
            return;
        }
        boolean flag = false;
        HeroNode temp = head.next;
        while (true) {
            if (temp == null) {
                break;
            }
            if (temp.no == newHeroNode.no) {
                flag = true;
                break;
            }
            temp = temp.next;
        }
        if (flag) {
            temp.name = newHeroNode.name;
            temp.nickname = newHeroNode.nickname;
        } else {
            System.out.printf("没有找到 编号 %d 的节点，不能修改\n", newHeroNode.no);
        }
    }

    // 删除节点
    // 思路
    // 1. head 不能动，因此我们需要一个temp辅助节点找到待删除节点的前一个节点
    // 2. 说明我们在比较时，是temp.next.no 和  需要删除的节点的no比较
    public void del(int no) {
        HeroNode temp = head;
        boolean flag = false;
        while (true) {
            if (temp.next == null) {
                break;
            }
            if (temp.next.no == no) {
                flag = true;
                break;
            }
            temp = temp.next;
        }
        if (flag) {
            temp.next = temp.next.next;
        } else {
            System.out.printf("要删除的 %d 节点不存在\n", no);
        }
    }

    // 显示链表[遍历]
    public void list() {
        if (head.next == null) {
            System.out.println("链表为空");
            return;
        }
        HeroNode temp = head.next;
        while (true) {
            if (temp == null) {
                break;
            }
            System.out.println(temp);
            temp = temp.next;
        }
    }
}

/**
 * 定义HeroNode ， 每个HeroNode 对象就是一个节点
 */
class HeroNode {
    public int no;
    public String name;
    public String nickname;
    /**
     * 指向下一个节点
     */
    public HeroNode next;

    //构造器
    public HeroNode(int no, String name, String nickname) {
        this.no = no;
        this.name = name;
        this.nickname = nickname;
    }

    //为了显示方法，我们重新toString
    @Override
    public String toString() {
        return "HeroNode [no=" + no + ", name=" + name + ", nickname=" + nickname + "]";
    }

}
package com.mzl.dataStructure.linkedlist;

/**
 * 双链表模拟队列
 */
public class DoubleLinkedListDemo {

    public static void main(String[] args) {
        // 测试
        System.out.println("双向链表的测试");
        // 先创建节点
        HeroNode2 hero1 = new HeroNode2(1, "宋江", "及时雨");
        HeroNode2 hero2 = new HeroNode2(2, "卢俊义", "玉麒麟");
        HeroNode2 hero3 = new HeroNode2(3, "吴用", "智多星");
        HeroNode2 hero4 = new HeroNode2(4, "林冲", "豹子头");
        // 创建一个双向链表
        DoubleLinkedList doubleLinkedList = new DoubleLinkedList();
        doubleLinkedList.addOrder(hero1);
        doubleLinkedList.addOrder(hero4);
        doubleLinkedList.addOrder(hero3);
        doubleLinkedList.addOrder(hero2);

        doubleLinkedList.list();

        // 修改
        HeroNode2 newHeroNode = new HeroNode2(4, "公孙胜", "入云龙");
        doubleLinkedList.update(newHeroNode);
        System.out.println("修改后的链表情况");
        doubleLinkedList.list();

        // 删除
        doubleLinkedList.del(3);
        System.out.println("删除后的链表情况~~");
        doubleLinkedList.list();

    }

}

/**
 * 创建一个双向链表的类
 */
class DoubleLinkedList {

    HeroNode2 head = new HeroNode2(0, "", "");

    /**
     * 返回头节点
     *
     * @return
     */
    public HeroNode2 getHead() {
        return head;
    }

    /**
     * 遍历双向链表的方法
     * 显示链表[遍历]
     */
    public void list() {
        if (head.next == null) {
            return;
        }
        HeroNode2 temp = head.next;
        while (temp != null) {
            System.out.println(temp);
            temp = temp.next;
        }
    }

    /**
     * 添加一个节点到双向链表的最后
     *
     * @param heroNode
     */
    public void add(HeroNode2 heroNode) {
        HeroNode2 temp = head;
        while (temp.next != null) {
            if (temp.next.no == heroNode.no) {
                System.out.printf("编号 %d 的节点已存在，不能插入\n", heroNode.no);
                return;
            }
            temp = temp.next;
        }
        temp.next = heroNode;
        heroNode.pre = temp;
    }

    /**
     * 有序插入
     *
     * @param hero
     */
    public void addOrder(HeroNode2 hero) {
        boolean flag = false;
        boolean flag1 = false;
        HeroNode2 temp = head;

        while (true) {
            if (temp.next == null) { // 判断插入的元素是否最后一个
                flag1 = true;
                break;
            } else if (temp.no == hero.no) { // 判断英雄编号是否已存在
                flag = true;
                break;
            } else if (temp.next.no > hero.no) {
                break;
            }
            temp = temp.next;
        }

        if (flag) {
            System.out.printf("编号 %d 的节点已存在，不能插入\n", hero.no);
        } else if (flag1) {
            // 在temp后面插入
            hero.next = temp.next;
            temp.next = hero;
            hero.pre = temp;
        } else {
            // 在temp前面插入
            hero.next = temp.next;
            temp.next.pre = hero;
            temp.next = hero;
            hero.pre = temp;
        }
    }

    /**
     * 修改一个节点的内容, 可以看到双向链表的节点内容修改和单向链表一样
     * 只是 节点类型改成 HeroNode2
     *
     * @param newHeroNode
     */
    public void update(HeroNode2 newHeroNode) {
        if (head.next == null) {
            System.out.println("链表为空~");
            return;
        }
        boolean flag = true;
        HeroNode2 temp = head.next;
        while (temp != null) {
            if (temp.no == newHeroNode.no) {
                flag = false;
                break;
            }
            temp = temp.next;
        }
        if (flag) {
            System.out.printf("没有找到 编号 %d 的节点，不能修改\n", newHeroNode.no);
        } else {
            temp.no = newHeroNode.no;
            temp.name = newHeroNode.name;
            temp.nickName = newHeroNode.nickName;
        }
    }

    /**
     * 从双向链表中删除一个节点,
     * 说明
     * 1 对于双向链表，我们可以直接找到要删除的这个节点
     * 2 找到后，自我删除即可
     *
     * @param no
     */
    public void del(int no) {
        if (head.next == null) {
            System.out.println("链表为空，无法删除");
            return;
        }
        boolean flag = true;
        HeroNode2 temp = head.next;
        while (temp != null) {
            if (temp.no == no) {
                flag = false;
                break;
            }
            temp = temp.next;
        }
        if (flag) {
            System.out.printf("要删除的 %d 节点不存在\n", no);
        } else {
            temp.pre.next = temp.next;
            if (temp.next != null) {
                temp.next.pre = temp.pre;
            }
        }
    }
}

class HeroNode2 {
    public int no;
    public String name;
    public String nickName;
    public HeroNode2 next;
    public HeroNode2 pre;

    public HeroNode2(int no, String name, String nickName) {
        this.no = no;
        this.name = name;
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "HeroNode2{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}

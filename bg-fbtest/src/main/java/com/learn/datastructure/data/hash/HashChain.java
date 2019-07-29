package com.learn.datastructure.data.hash;

import com.learn.datastructure.data.hash.SortLink.LinkNode;


/*
 * 链地址法:
 数组的每个数据项都创建一个子链表或子数组，那么数组内不直接存放单词，当产生冲突时，
 新的数据项直接存放到这个数组下标表示的链表中，这种方法称为链地址法。
 
 找到初始单元需要O(1)的时间级别，而搜索链表的时间与M成正比，M为链表包含的平均项数，即O(M)的时间级别。
 
 另外一种方法类似于链地址法，它是在每个数据项中使用子数组，而不是链表。这样的数组称为桶。
这个方法显然不如链表有效，因为桶的容量不好选择，如果容量太小，可能会溢出，如果太大，又造成性能浪费，
而链表是动态分配的，不存在此问题。所以一般不使用桶。

 */

public class HashChain {
    private SortLink[] hashArray;//数组中存放链表
    private int arraySize;
    public HashChain(int size){
        arraySize = size;
        hashArray = new SortLink[arraySize];
        //new 出每个空链表初始化数组
        for(int i = 0 ; i < arraySize ; i++){
            hashArray[i] = new SortLink();
        }
    }
     
    public void displayTable(){
        for(int i = 0 ; i < arraySize ; i++){
            System.out.print(i + "：");
            hashArray[i].displayLink();
        }
    }
     
    public int hashFunction(int key){
        return key%arraySize;
    }
     
    public void insert(LinkNode node){
        int key = node.getKey();
        int hashVal = hashFunction(key);
        hashArray[hashVal].insert(node);//直接往链表中添加即可
    }
     
    public LinkNode delete(int key){
        int hashVal = hashFunction(key);
        LinkNode temp = find(key);
        hashArray[hashVal].delete(key);//从链表中找到要删除的数据项，直接删除
        return temp;
    }
     
    public LinkNode find(int key){
        int hashVal = hashFunction(key);
        LinkNode node = hashArray[hashVal].find(key);
        return node;
    }
 
}
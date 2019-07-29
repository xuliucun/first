package com.learn.datastructure.data.stack;



/*
栈是允许在同一端进行插入和删除操作的特殊线性表。
允许进行插入和删除操作的一端称为栈顶(top)，另一端为栈底(bottom)；栈底固定，而栈顶浮动；
栈中元素个数为零时称为空栈。插入一般称为进栈（PUSH），删除则称为退栈（POP）。

栈的应用:1.网络浏览器后退  2.文本编辑器的撤销  3.<>{}字符的匹配 4.倒序打印
 * */
public class MyStack {
    private int[] array;
    private int maxSize;
    private int top;
     
    public MyStack(int size){
        this.maxSize = size;
        array = new int[size];
        top = -1;
    }
     
    //压入数据
    public void push(int value){
        if(top < maxSize-1){
            array[++top] = value;
        }
    }
     
    //弹出栈顶数据
    public int pop(){
        return array[top--];
    }
     
    //访问栈顶数据
    public int peek(){
        return array[top];
    }
     
    //判断栈是否为空
    public boolean isEmpty(){
        return (top == -1);
    }
     
    //判断栈是否满了
    public boolean isFull(){
        return (top == maxSize-1);
    }
     
    public static void main(String[] args) {
    	MyStack stack = new MyStack(3);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println(stack.peek());
        while(!stack.isEmpty()){
            System.out.println(stack.pop());
        }
	}
 
}

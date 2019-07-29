package com.learn.datastructure.data.stack;

import java.util.Arrays;
import java.util.EmptyStackException;
/*增强版栈:第一个能自动扩容，第二个能存储各种不同类型的数据*/
public class ArrayStack {
    //存储元素的数组,声明为Object类型能存储任意类型的数据
    private Object[] elementData;
    //指向栈顶的指针
    private int top;
    //栈的总容量
    private int size;
     
     
    //默认构造一个容量为10的栈
    public ArrayStack(){
        this.elementData = new Object[10];
        this.top = -1;
        this.size = 10;
    }
     
    public ArrayStack(int initialCapacity){
        if(initialCapacity < 0){
            throw new IllegalArgumentException("栈初始容量不能小于0: "+initialCapacity);
        }
        this.elementData = new Object[initialCapacity];
        this.top = -1;
        this.size = initialCapacity;
    }
     
     
    //压入元素
    public Object push(Object item){
        //是否需要扩容
        isGrow(top+1);
        elementData[++top] = item;
        return item;
    }
     
    //弹出栈顶元素
    public Object pop(){
        Object obj = peek();
        remove(top);
        return obj;
    }
     
    //获取栈顶元素
    public Object peek(){
        if(top == -1){
            throw new EmptyStackException();
        }
        return elementData[top];
    }
    //判断栈是否为空
    public boolean isEmpty(){
        return (top == -1);
    }
     
    //删除栈顶元素
    public void remove(int top){
        //栈顶元素置为null
        elementData[top] = null;
        this.top--;
    }
     
    /**
     * 是否需要扩容，如果需要，则扩大一倍并返回true，不需要则返回false
     * @param minCapacity
     * @return
     */
    public boolean isGrow(int minCapacity){
        int oldCapacity = size;
        //如果当前元素压入栈之后总容量大于前面定义的容量，则需要扩容
        if(minCapacity >= oldCapacity){
            //定义扩大之后栈的总容量
            int newCapacity = 0;
            //栈容量扩大两倍(左移一位)看是否超过int类型所表示的最大范围
            if((oldCapacity<<1) - Integer.MAX_VALUE >0){
                newCapacity = Integer.MAX_VALUE;
            }else{
                newCapacity = (oldCapacity<<1);//左移一位，相当于*2
            }
            this.size = newCapacity;
           // int[] newArray = new int[size];
            elementData = Arrays.copyOf(elementData, size);
            return true;
        }else{
            return false;
        }
    }
    public static void main(String[] args) {
    	ArrayStack stack = new ArrayStack(3);
        stack.push(1);
        //System.out.println(stack.peek());
        stack.push(2);
        stack.push(3);
        stack.push("abc");
        System.out.println(stack.peek());
        stack.pop();
        stack.pop();
        stack.pop();
        System.out.println(stack.peek());
        
        test02();
	}
    //利用栈实现字符串逆序
    public static void test01() {
    	ArrayStack stack = new ArrayStack();
        String str = "how are you";
        char[] cha = str.toCharArray();
        for(char c : cha){
            stack.push(c);
        }
        while(!stack.isEmpty()){
            System.out.print(stack.pop());
        }
	}
    
  //利用栈判断分隔符是否匹配
    public static void test02() {
    	ArrayStack stack = new ArrayStack(3);
        String str = "12<a[[b{c}]>";
        char[] cha = str.toCharArray();
        for(char c : cha){
            switch (c) {
            case '{':
            case '[':
            case '<':
                stack.push(c);
                break;
            case '}':
            case ']':
            case '>':
                if(!stack.isEmpty()){
                    char ch = stack.pop().toString().toCharArray()[0];
                    if(c=='}' && ch != '{'
                        || c==']' && ch != '['
                        || c==')' && ch != '('){
                        System.out.println("Error:"+ch+"-"+c);
                    }
                }
                break;
            default:
                break;
            }
        }
	}
 
}
package com.learn.design.singleton;

//1、饿汉式（静态常量）
public class Singleton {
	//静态常量只加载一次
    private final static Singleton INSTANCE = new Singleton();
    //私有构造方法
    private Singleton(){}
    //外部获取形式
    public static Singleton getInstance(){
        return INSTANCE;
    }
}

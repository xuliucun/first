package com.learn.design.singleton;

public class StaticSingleton {
	
	 private static StaticSingleton instance;
	 //静态代码块
	    static {
	        instance = new StaticSingleton();
	    }

	    private StaticSingleton() {}

	    public static StaticSingleton getInstance() {
	        return instance;
	    }

}

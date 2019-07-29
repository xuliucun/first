package com.learn.design.singleton;

public class DoubleCheckSingleton {
	//优点：线程安全；延迟加载；效率较高
	private static volatile DoubleCheckSingleton singleton;

    private DoubleCheckSingleton() {}

    public static DoubleCheckSingleton getInstance() {
        if (singleton == null) {//一次检查
            synchronized (DoubleCheckSingleton.class) {//加锁
                if (singleton == null) {//二次检查
                    singleton = new DoubleCheckSingleton();
                }
            }
        }
        return singleton;
    }

}

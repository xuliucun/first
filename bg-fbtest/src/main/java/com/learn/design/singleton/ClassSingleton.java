package com.learn.design.singleton;

public class ClassSingleton {
	private ClassSingleton() {}

    private static class SingletonInstance {//内部类
        private static final ClassSingleton INSTANCE = new ClassSingleton();
    }

    public static ClassSingleton getInstance() {
        return SingletonInstance.INSTANCE;
    }
    /*
             静态内部类方式在Singleton类被装载时并不会立即实例化，而是在需要实例化时，
      调用getInstance方法，才会装载SingletonInstance类，从而完成Singleton的实例化。

            类的静态属性只会在第一次加载类的时候初始化，所以在这里，JVM帮助我们保证了线程的安
      全性，在类进行初始化时，别的线程是无法进入的。

      优点：避免了线程不安全，延迟加载，效率高。
    */
    
}

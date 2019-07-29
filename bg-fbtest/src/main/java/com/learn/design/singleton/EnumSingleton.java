package com.learn.design.singleton;

public enum  EnumSingleton {
	//不仅能避免多线程同步问题，而且还能防止反序列化重新创建新的对象。
	INSTANCE;
}

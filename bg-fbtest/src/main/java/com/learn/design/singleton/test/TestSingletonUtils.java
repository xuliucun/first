package com.learn.design.singleton.test;

import org.junit.Test;

public class TestSingletonUtils {
	
	@Test
	public void testPropertiesUtil(){
		PropertiesUtil props01 = PropertiesUtil.getInstance();
        PropertiesUtil props02 = PropertiesUtil.getInstance();
        System.out.println(props01 == props02);
        
        String testString = "零Ⅲ壹壹872陆7844";
        for (String key : props01.getPropMap().keySet()) {
            if (testString.contains(key)) {
                testString = testString.replace(key, props01.getPropMap().get(key));
            }
        }
        System.out.println(testString);
	}
	
	@Test
	public void testMyDataBaseSource(){
		//很显然获得了三个Connection连接，但是只调用了一次枚举的构造方法,从而通过枚举实现了单例的设计
		MyDataBaseSource.DATASOURCE.getConnection() ;
        MyDataBaseSource.DATASOURCE.getConnection() ;
        MyDataBaseSource.DATASOURCE.getConnection() ;
	}

}

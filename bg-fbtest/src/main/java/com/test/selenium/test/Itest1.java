package com.test.selenium.test;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


//浏览器控制操作
public class Itest1 {
    public static void main(String[] args) throws Exception{
        System.setProperty("webdriver.chrome.driver", "D:\\driver\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.sogou.com/");
        //1.打印标题
        String title = driver.getTitle();
        System.out.printf(title);

        //设置窗口大小
        driver.manage().window().setSize(new Dimension(480, 800));
        Thread.sleep(2000);

        //最大化
        driver.manage().window().maximize();
        Thread.sleep(2000);

        //点击“新闻” 链接
        driver.findElement(By.linkText("新闻")).click();
        System.out.printf("now accesss %s \n", driver.getCurrentUrl());
        Thread.sleep(2000);

        //执行浏览器后退
        driver.navigate().back();
        System.out.printf("back to %s \n", driver.getCurrentUrl());
        Thread.sleep(2000);

        //执行浏览器前面
        driver.navigate().forward();
        System.out.printf("forward to %s \n", driver.getCurrentUrl());
        Thread.sleep(2000);

        //刷新页面
        driver.navigate().refresh();

        //退出
        driver.quit();

    }
}

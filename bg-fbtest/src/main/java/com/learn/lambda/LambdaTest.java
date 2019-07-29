package com.learn.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.Test;


public class LambdaTest {
	/*
	 * 1. 不需要参数,返回值为 5 () -> 5 2. 接收一个参数(数字类型),返回其2倍的值 x -> 2 * x 3.
	 * 接受2个参数(数字),并返回他们的差值 (x, y) -> x – y 4. 接收2个int型整数,返回他们的和 (int x, int y)
	 * -> x + y 5. 接受一个 string 对象,并在控制台打印,不返回任何值(看起来像是返回void) (String s) ->
	 * System.out.print(s)
	 * 
	 * 在Java中，随声明随调用的方式是不行的，比如下面这样，声明了一个λ表达式(x, y) -> x + y，同时企图通过传入实参(2,
	 * 3)来调用它： int five = ( (x, y) -> x + y ) (2, 3); // ERROR! try to call a
	 * lambda in-place 这在C++中是可以的，但Java中不行。Java的λ表达式只能用作赋值、传参、返回值等。
	 */
	@Test
	public void test01() {
		// Prior Java 8 :
		List<String> features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
		for (String feature : features) {
			System.out.println(feature);
		}

		// In Java 8:
		features.forEach(n -> System.out.println(n));
		features.forEach(System.out::println);// 方法引用是使用两个冒号::这个操作符号。

	}
	//------------------------------------------------------------------------------------
	@Test
	public void test02() {
		List<String> languages = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp");
		System.out.println("Languages which starts with J :");
		filter(languages, (str) -> str.startsWith("J"));
		// System.out.println("Languages which ends with a ");
		// filter(languages, (str)->str.endsWith("a"));
		// System.out.println("Print all languages :");
		// filter(languages, (str)->true);
		// System.out.println("Print no language : ");
		// filter(languages, (str)->false);
		// System.out.println("Print language whose length greater than 4:");
		// filter(languages, (str)->str.length() > 4);
	}

	public static void filter(List<String> names, Predicate<String> condition) {
		System.out.println(condition.test("77"));
		for (String name : names) {
			if (condition.test(name)) {
				System.out.println(name + "??");
			}
		}
	}
	// Even better
	public static void filter2(List<String> names, Predicate<String> condition) {
		names.stream().filter((name) -> (condition.test(name)))
					  .forEach((name) -> {
						  System.out.println(name + " ");
					  });
	}
	//----------------------------------------------------------------------------
	//map  reduce
	@Test
	public void test03(){
		List<Integer> costBeforeTax =Arrays.asList(100, 200, 300, 400, 500);
		double bill = costBeforeTax.stream().map((cost) -> cost + .12*cost)
		                                    .reduce((sum, cost) -> sum + cost)
		                                    .get();
		System.out.println("Total : " + bill);
	}
	//------------------------------------------------------------------
	@Test
	public void test04(){
		// 1.1使用匿名内部类  
		new Thread(new Runnable() {  
		    @Override  
		    public void run() {  
		        System.out.println("Hello world !");  
		    }  
		}).start();  
		  
		// 1.2使用 lambda expression  
		new Thread(() -> System.out.println("Hello world !")).start();  
		  
		// 2.1使用匿名内部类  
		Runnable race1 = new Runnable() {  
		    @Override  
		    public void run() {  
		        System.out.println("Hello world !");  
		    }  
		};  
		  
		// 2.2使用 lambda expression  
		Runnable race2 = () -> System.out.println("Hello world !");  
		   
		// 直接调用 run 方法(没开新线程哦!)  
		race1.run();  
		race2.run();
	}
	//------------------------------------------------------------------
	@Test
	public void test05(){
		String[] players = {"Rafael Nadal", "Novak Djokovic",   
			    "Stanislas Wawrinka", "David Ferrer",  
			    "Roger Federer", "Andy Murray",  
			    "Tomas Berdych", "Juan Martin Del Potro",  
			    "Richard Gasquet", "John Isner"};  
			   
			// 1.1 使用匿名内部类根据 name 排序 players  
			Arrays.sort(players, new Comparator<String>() {  
			    @Override  
			    public int compare(String s1, String s2) {  
			        return (s1.compareTo(s2));  
			    }  
			});  
			// 1.2 使用 lambda expression 排序 players  
			Comparator<String> sortByName = (String s1, String s2) -> (s1.compareTo(s2));  
			Arrays.sort(players, sortByName);  
			  
			// 1.3 也可以采用如下形式:  
			Arrays.sort(players, (String s1, String s2) -> (s1.compareTo(s2)));  
			
			//-------------拓展------------------------
			// 1.1 使用匿名内部类根据 surname 排序 players  
			Arrays.sort(players, new Comparator<String>() {  
			    @Override  
			    public int compare(String s1, String s2) {  
			        return (s1.substring(s1.indexOf(" ")).compareTo(s2.substring(s2.indexOf(" "))));  
			    }  
			});  
			  
			// 1.2 使用 lambda expression 排序,根据 surname  
			Comparator<String> sortBySurname = (String s1, String s2) ->   
			    ( s1.substring(s1.indexOf(" ")).compareTo( s2.substring(s2.indexOf(" ")) ) );  
			Arrays.sort(players, sortBySurname);  
			  
			// 1.3 或者这样,怀疑原作者是不是想错了,括号好多...  
			Arrays.sort(players, (String s1, String s2) ->   
			      ( s1.substring(s1.indexOf(" ")).compareTo( s2.substring(s2.indexOf(" ")) ) )   
			    );  
			  
			// 2.1 使用匿名内部类根据 name lenght 排序 players  
			Arrays.sort(players, new Comparator<String>() {  
			    @Override  
			    public int compare(String s1, String s2) {  
			        return (s1.length() - s2.length());  
			    }  
			});  
			  
			// 2.2 使用 lambda expression 排序,根据 name lenght  
			Comparator<String> sortByNameLenght = (String s1, String s2) -> (s1.length() - s2.length());  
			Arrays.sort(players, sortByNameLenght);  
			  
			// 2.3 or this  
			Arrays.sort(players, (String s1, String s2) -> (s1.length() - s2.length()));  
			  
			// 3.1 使用匿名内部类排序 players, 根据最后一个字母  
			Arrays.sort(players, new Comparator<String>() {  
			    @Override  
			    public int compare(String s1, String s2) {  
			        return (s1.charAt(s1.length() - 1) - s2.charAt(s2.length() - 1));  
			    }  
			});  
			  
			// 3.2 使用 lambda expression 排序,根据最后一个字母  
			Comparator<String> sortByLastLetter =   
			    (String s1, String s2) ->   
			        (s1.charAt(s1.length() - 1) - s2.charAt(s2.length() - 1));  
			Arrays.sort(players, sortByLastLetter);  
			  
			// 3.3 or this  
			Arrays.sort(players, (String s1, String s2) -> (s1.charAt(s1.length() - 1) - s2.charAt(s2.length() - 1)));
	}
	
	//------------------------------------------------------------------
	@Test
	public void test06(){
		List<Person> javaProgrammers = new ArrayList<Person>() {  
			  {  
			    add(new Person("Elsdon", "Jaycob", "Java programmer", "male", 43, 2000));  
			    add(new Person("Tamsen", "Brittany", "Java programmer", "female", 23, 1500));  
			    add(new Person("Floyd", "Donny", "Java programmer", "male", 33, 1800));  
			    add(new Person("Sindy", "Jonie", "Java programmer", "female", 32, 1600));  
			    add(new Person("Vere", "Hervey", "Java programmer", "male", 22, 1200));  
			    add(new Person("Maude", "Jaimie", "Java programmer", "female", 27, 1900));  
			    add(new Person("Shawn", "Randall", "Java programmer", "male", 30, 2300));  
			    add(new Person("Jayden", "Corrina", "Java programmer", "female", 35, 1700));  
			    add(new Person("Palmer", "Dene", "Java programmer", "male", 33, 2000));  
			    add(new Person("Addison", "Pam", "Java programmer", "female", 34, 1300));  
			  }  
			};  
			  
			List<Person> phpProgrammers = new ArrayList<Person>() {  
				{  
			    add(new Person("Jarrod", "Pace", "PHP programmer", "male", 34, 1550));  
			    add(new Person("Clarette", "Cicely", "PHP programmer", "female", 23, 1200));  
			    add(new Person("Victor", "Channing", "PHP programmer", "male", 32, 1600));  
			    add(new Person("Tori", "Sheryl", "PHP programmer", "female", 21, 1000));  
			    add(new Person("Osborne", "Shad", "PHP programmer", "male", 32, 1100));  
			    add(new Person("Rosalind", "Layla", "PHP programmer", "female", 25, 1300));  
			    add(new Person("Fraser", "Hewie", "PHP programmer", "male", 36, 1100));  
			    add(new Person("Quinn", "Tamara", "PHP programmer", "female", 21, 1000));  
			    add(new Person("Alvin", "Lance", "PHP programmer", "male", 38, 1600));  
			    add(new Person("Evonne", "Shari", "PHP programmer", "female", 40, 1800));  
			  }  
			}; 
			System.out.println("\n*****************************"); 
			System.out.println("所有程序员的姓名:");  
			javaProgrammers.forEach((p) -> System.out.printf("%s %s; ", p.getFirstName(), p.getLastName()));  
			phpProgrammers.forEach((p) -> System.out.printf("%s %s; ", p.getFirstName(), p.getLastName())); 
			System.out.println("\n*****************************");
			System.out.println("给程序员加薪 5% :");  
			Consumer<Person> giveRaise = e -> e.setSalary(e.getSalary() / 100 * 5 + e.getSalary());  
			javaProgrammers.forEach(giveRaise);  
			phpProgrammers.forEach(giveRaise); 
			System.out.println("\n*****************************");
			System.out.println("下面是月薪超过 $1,400 的PHP程序员:");
			phpProgrammers.stream().filter((p) -> (p.getSalary() > 1400))  
			          			   .forEach((p) -> System.out.printf("%s %s; ", p.getFirstName(), p.getLastName())); 
			
			// 定义 filters  
			Predicate<Person> ageFilter = (p) -> (p.getAge() > 25);  
			Predicate<Person> salaryFilter = (p) -> (p.getSalary() > 1400);  
			Predicate<Person> genderFilter = (p) -> ("female".equals(p.getGender()));  
			System.out.println("\n*****************************");
			System.out.println("下面是年龄大于 24岁且月薪在$1,400以上的女PHP程序员:");  
			phpProgrammers.stream()  
			          .filter(ageFilter)  
			          .filter(salaryFilter)  
			          .filter(genderFilter)  
			          .forEach((p) -> System.out.printf("%s %s; ", p.getFirstName(), p.getLastName()));  
			System.out.println("\n*****************************");  
			// 重用filters  
			System.out.println("年龄大于 24岁的女性 Java programmers:");  
			javaProgrammers.stream()  
			          .filter(ageFilter)  
			          .filter(genderFilter)  
			          .forEach((p) -> System.out.printf("%s %s; ", p.getFirstName(), p.getLastName())); 
			System.out.println("\n*****************************");
			System.out.println("最前面的3个 Java programmers:");  
			javaProgrammers.stream()  
			          .limit(3)  
			          .forEach((p) -> System.out.printf("%s %s; ", p.getFirstName(), p.getLastName()));  
			  
			System.out.println("\n*****************************");  
			System.out.println("最前面的3个女性 Java programmers:");  
			javaProgrammers.stream()  
			          .filter(genderFilter)  
			          .limit(3)  
			          .forEach((p) -> System.out.printf("%s %s; ", p.getFirstName(), p.getLastName())); 
			System.out.println("\n*****************************");
			System.out.println("根据 name 排序,并显示前5个 Java programmers:");  
			List<Person> sortedJavaProgrammers = javaProgrammers  
			          .stream()  
			          .sorted((p, p2) -> (p.getFirstName().compareTo(p2.getFirstName())))  
			          .limit(5)  
			          .collect(Collectors.toList());  
			sortedJavaProgrammers.forEach((p) -> System.out.printf("%s %s; %n", p.getFirstName(), p.getLastName()));  
			System.out.println("\n*****************************");  
			System.out.println("根据 salary 排序 Java programmers:");  
			sortedJavaProgrammers = javaProgrammers  
			          .stream()  
			          .sorted( (p, p2) -> (p.getSalary() - p2.getSalary()) )  
			          .collect(Collectors.toList());  
			  
			sortedJavaProgrammers.forEach((p) -> System.out.printf("%s %s; %n", p.getFirstName(), p.getLastName()));
			System.out.println("\n*****************************");  
			System.out.println("工资最低的 Java programmer:");  
			Person pers = javaProgrammers  
			          .stream()  
			          .min((p1, p2) -> (p1.getSalary() - p2.getSalary()))  
			          .get();
			System.out.printf("Name: %s %s; Salary: $%,d.", pers.getFirstName(), pers.getLastName(), pers.getSalary());
			System.out.println("\n*****************************");    
			System.out.println("工资最高的 Java programmer:");  
			Person person = javaProgrammers  
			          .stream()  
			          .max((p, p2) -> (p.getSalary() - p2.getSalary()))  
			          .get();
			System.out.printf("Name: %s %s; Salary: $%,d.", person.getFirstName(), person.getLastName(), person.getSalary());
			System.out.println("\n*****************************"); 
			System.out.println("将 PHP programmers 的 first name 拼接成字符串:");  
			String phpDevelopers = phpProgrammers  
			          .stream()  
			          .map(Person::getFirstName)  
			          .collect(Collectors.joining(" ; ")); // 在进一步的操作中可以作为标记(token)     
			System.out.println("\n*****************************");  
			System.out.println("将 Java programmers 的 first name 存放到 TreeSet:");  
			TreeSet<String> javaDevLastName = javaProgrammers  
			          .stream()  
			          .map(Person::getLastName)  
			          .collect(Collectors.toCollection(TreeSet::new));
			System.out.println("\n*****************************");  
			System.out.println("计算付给 Java programmers 的所有money:");  
			int totalSalary = javaProgrammers  
			          .parallelStream()  
			          .mapToInt(p -> p.getSalary())  
			          .sum();  
			System.out.println("\n*****************************");  
			//计算 count, min, max, sum, and average for numbers  
			List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);  
			IntSummaryStatistics stats = numbers  
			          .stream()  
			          .mapToInt((x) -> x)  
			          .summaryStatistics();  
			  
			System.out.println("List中最大的数字 : " + stats.getMax());  
			System.out.println("List中最小的数字 : " + stats.getMin());  
			System.out.println("所有数字的总和   : " + stats.getSum());  
			System.out.println("所有数字的平均值 : " + stats.getAverage());   
	}
	//------------------------------------------------------------------
	@Test
	public void test07(){
		
	}
	//------------------------------------------------------------------

}

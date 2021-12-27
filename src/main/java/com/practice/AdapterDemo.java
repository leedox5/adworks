package com.practice;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class AdapterDemo
{
	public static void goodMethod(Enumeration<String> enu)
	{
		while(enu.hasMoreElements())
		{
			System.out.println(enu.nextElement());
		}
	}

	public static void main(String[] args)
	{
		List<String> list = new ArrayList<String>();
		list.add("이은결");
		list.add("Kevin Parker");
		Enumeration<String> ite = new IteratorToEnumeration(list.iterator());
		AdapterDemo.goodMethod(ite);

		Print p = new PrintBanner("Hello");
		p.printWeak();
		p.printStrong();

		Print1 p1 = new PrintBanner1("Hello");
		p1.printWeak();
		p1.printStrong();
	}
}
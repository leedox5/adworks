package com.practice;

public class PrintBanner1 extends Print1
{
	private Banner banner;
	public PrintBanner1(String string)
	{
		this.banner = new Banner(string);
	}
	public void printStrong()
	{
		banner.showWithAster();
	}

	public void printWeak()
	{
		banner.showWithParen();
	}
}
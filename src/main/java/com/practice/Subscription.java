package com.practice;

public class Subscription
{
	private int price;
	private int length;

	public Subscription(int p, int n)
	{
		price = p;
		length = n;
	}

	public double pricePerMonth()
	{
		double r = (double) price / (double) length / 100.0;
		return r;
	}
}
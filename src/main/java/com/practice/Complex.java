package com.practice;
import java.util.Date;

public class Complex implements Cloneable
{
	private String complexinfo;
	private Date date;

	public Complex(String complexinfo)
	{
		this.complexinfo = complexinfo;
	}

	public void setDate(Date date)
	{
		this.date = new Date(date.getTime());
	}

	public Date getDate()
	{
		return date;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Complex tmp = (Complex)super.clone();
		return tmp;
	}
}

package com.practice;

import java.math.BigDecimal;
import java.util.Date;

public class Receipt {
	private int confirmationNumber;
	private Date pickupTime;
	private BigDecimal gcBalance;

	public int getConfirmationNumber() {
		return confirmationNumber;
	}

	public void setConfirmationNumber(int confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}

	public void setPickupTime(Date date) {
		this.pickupTime = date;
	}

	public Date getPickupTime() {
		return pickupTime;
	}

	public BigDecimal getGCBalance() {
		return gcBalance;
	}

	public void setGLBalance(BigDecimal balance) {
		this.gcBalance = balance;
	}
}

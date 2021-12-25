package com.practice;

import java.math.BigDecimal;
import java.util.Date;

public class OrderProcessor {

	private DBAccessor dbAccessor;
	
	public void setDbAccessor(DBAccessor dbAccessor) {
		this.dbAccessor = dbAccessor;
	}
	
	public Receipt processOrder(OrderInformation orderInfo) {
		GiftCard gc = dbAccessor.getGC(orderInfo.getGiftCardNumber());
		dbAccessor.saveOrder(orderInfo);
		
		gc.setBalance(new BigDecimal(0));
		dbAccessor.saveGL(gc);
		
		Receipt receipt = new Receipt();
		receipt.setConfirmationNumber(12345);
		receipt.setPickupTime(new Date());
		receipt.setGLBalance(gc.getBalance());
		//receipt.setGLBalance(new BigDecimal(0));
		
		return receipt;
	}


}

package com.practice;

public interface DBAccessor {

	GiftCard getGC(int giftCardNumber);

	void saveOrder(OrderInformation orderInfo);

	void saveGL(GiftCard gc);

}

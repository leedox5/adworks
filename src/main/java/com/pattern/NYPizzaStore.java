package com.pattern;

public class NYPizzaStore extends PizzaStore {

	@Override
	protected	Pizza createPizza(String type) {
		if(type.equals("cheese")) {
			return new NYStyleCheesePizza();
		}
		return null;
	}

}

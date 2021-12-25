package com.pattern;

public abstract class PizzaStore {
	public Pizza orderPizza(String type) {
		Pizza pizza = null;
		
		pizza = createPizza(type);
		
		pizza.prepare();
		pizza.box();
		
		return pizza;
	}

	protected abstract Pizza createPizza(String type);
}


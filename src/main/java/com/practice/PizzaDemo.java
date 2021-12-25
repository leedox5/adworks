package com.practice;

public class PizzaDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimplePizzaFactory factory = new SimplePizzaFactory();
		PizzaStore store = new PizzaStore(factory);
		
		Pizza pizza = store.orderPizza("cheese");
		System.out.println("We orderd a " + pizza.getName() + "\n");
	}
}

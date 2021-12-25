package com.pattern;

public class PizzaDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PizzaStore nyStore = new NYPizzaStore();
		
		Pizza pizza = nyStore.orderPizza("cheese");
		System.out.println("Ethan orderd a "+pizza.getName() + "\n");
	}
}

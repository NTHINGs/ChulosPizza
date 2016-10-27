package com.nthings.chulospizza.util;

public class PedidoPizzas {
	private String pizza;
	
	public PedidoPizzas(String nombrepizza){
		this.pizza=nombrepizza;
	}

	public String getPizza(){
		return pizza;
	}
	
	public void setPizza(String nombrepizza){
		this.pizza=nombrepizza;
	}
}

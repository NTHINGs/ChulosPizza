package com.nthings.chulospizza.util;
//Clase para modelar el catálogo
public class CatalogoPizzas {
	private String pizza;
	private int imagenPizza;
	private String precio;
	
	public CatalogoPizzas(String nombrepizza, int drawablePizza, String precio){
		this.pizza=nombrepizza;
		this.imagenPizza=drawablePizza;
		this.precio=precio;
	}
	
	public String getPizza(){
		return pizza;
	}
	
	public void setPizza(String nombrepizza){
		this.pizza=nombrepizza;
	}
	
	public int getimagenPizza(){
		return imagenPizza;
	}
	
	public void setimagenPizza(int drawablePizza){
		this.imagenPizza=drawablePizza;
	}
	
	public String getPrecio(){
		return precio;
	}
	
	public void setPrecio(String precio){
		this.precio=precio;
	}
}

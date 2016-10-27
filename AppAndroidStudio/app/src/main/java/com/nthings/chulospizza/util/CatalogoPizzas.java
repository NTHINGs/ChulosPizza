package com.nthings.chulospizza.util;

import android.graphics.drawable.Drawable;

//Clase para modelar el catálogo
public class CatalogoPizzas {
	private String pizza;
	private Drawable imagenPizza;
	private String precio;
	
	public CatalogoPizzas(String nombrepizza, Drawable drawablePizza, String precio){
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
	
	public Drawable getimagenPizza(){
		return imagenPizza;
	}
	
	public void setimagenPizza(Drawable drawablePizza){
		this.imagenPizza=drawablePizza;
	}
	
	public String getPrecio(){
		return precio;
	}
	
	public void setPrecio(String precio){
		this.precio=precio;
	}
}

package com.aegamesi.mc.po8;

public class Po8Item {
	public boolean active;
	public String id;
	public double maxPrice;
	public double minPrice;
	public String name;
	public int stackSize;

	public Po8Item(boolean active, String id, double minPrice, double maxPrice, String name, int stackSize) {
		this.active = active;
		this.id = id;
		this.maxPrice = maxPrice;
		this.minPrice = minPrice;
		this.name = name;
		this.stackSize = stackSize;
	}
}

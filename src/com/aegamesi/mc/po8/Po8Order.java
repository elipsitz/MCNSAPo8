package com.aegamesi.mc.po8;

import java.io.Serializable;

import org.bukkit.inventory.ItemStack;

import com.aegamesi.mc.po8.support.CardboardBox;

public class Po8Order implements Serializable {
	private static final long serialVersionUID = 4766125679201998352L;
	public int type;
	public double value;
	public String owner;
	public CardboardBox[] items;

	public Po8Order(int type, double value, String owner, ItemStack[] items) {
		this.type = type;
		this.value = value;
		this.owner = owner;
		this.items = new CardboardBox[items.length];
		for (int i = 0; i < items.length; i++)
			this.items[i] = new CardboardBox(items[i]);
	}

	public ItemStack[] getItems() {
		ItemStack[] stacks = new ItemStack[items.length];
		for (int i = 0; i < items.length; i++)
			stacks[i] = items[i].unbox();
		return stacks;
	}
}

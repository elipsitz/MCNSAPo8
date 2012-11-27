package com.aegamesi.mc.po8;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class Po8InventoryHolder implements InventoryHolder {
	public Inventory inventory;
	public int type;
	public String player;

	public Inventory getInventory() {
		return inventory;
	}

	public Po8InventoryHolder(int type, String player, ItemStack[] contents) {
		this.player = player;
		this.type = type;

		inventory = Bukkit.getServer().createInventory(this, 54, player + "'s " + (type == Po8.SELL ? "Sell Chest" : "Buy Chest"));
		if (contents != null)
			inventory.setContents(contents);
	}
}

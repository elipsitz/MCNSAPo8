package com.aegamesi.mc.po8.support;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * A serializable ItemStack
 */
public class CardboardBox implements Serializable {
	private static final long serialVersionUID = 729890133797629668L;

	private final int type, amount;
	private final short damage;
	private final byte data;
	private final boolean isNull;

	private final HashMap<CardboardEnchantment, Integer> enchants;

	public CardboardBox(ItemStack item) {
		if (item == null) {
			this.isNull = true;
			this.type = 0;
			this.amount = 0;
			this.damage = 0;
			this.data = 0;
			this.enchants = null;
		} else {
			this.isNull = false;
			this.type = item.getTypeId();
			this.amount = item.getAmount();
			this.damage = item.getDurability();
			this.data = item.getData().getData();

			HashMap<CardboardEnchantment, Integer> map = new HashMap<CardboardEnchantment, Integer>();

			Map<Enchantment, Integer> enchantments = item.getEnchantments();

			for (Enchantment enchantment : enchantments.keySet()) {
				map.put(new CardboardEnchantment(enchantment), enchantments.get(enchantment));
			}

			this.enchants = map;
		}
	}

	public ItemStack unbox() {
		if (isNull)
			return null;

		ItemStack item = new ItemStack(type, amount, damage, data);

		HashMap<Enchantment, Integer> map = new HashMap<Enchantment, Integer>();

		for (CardboardEnchantment cEnchantment : enchants.keySet()) {
			map.put(cEnchantment.unbox(), enchants.get(cEnchantment));
		}

		item.addUnsafeEnchantments(map);

		return item;
	}
}
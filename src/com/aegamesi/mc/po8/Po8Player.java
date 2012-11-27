package com.aegamesi.mc.po8;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import com.aegamesi.mc.po8.support.CardboardBox;

public class Po8Player implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4390628890938756777L;
	private CardboardBox[] buyInv;
	private CardboardBox[] sellInv;
	public HashMap<String, Integer> buyOrder;
	public ArrayList<CardboardBox> extendedInv;
	public Po8Order reviewOrder;
	public double balance;
	public boolean notify;

	//
	public boolean isReviewingOrder;

	public Po8Player() {
		buyInv = new CardboardBox[54];
		sellInv = new CardboardBox[54];
		balance = 0;
		buyOrder = new HashMap<String, Integer>();
		extendedInv = new ArrayList<CardboardBox>();
		reviewOrder = null;
		notify = false;
		isReviewingOrder = false;
	}

	public ItemStack[] getInventory(int type) {
		CardboardBox[] from = type == Po8.BUY ? buyInv : sellInv;
		ItemStack[] to = new ItemStack[54];
		for (int i = 0; i < 54; i++) {
			to[i] = from[i] == null ? null : from[i].unbox();
		}
		return to;
	}

	public void setInventory(int type, ItemStack[] contents) {
		CardboardBox[] to = type == Po8.BUY ? buyInv : sellInv;
		for (int i = 0; i < 54; i++) {
			to[i] = contents[i] == null ? null : new CardboardBox(contents[i]);
		}
	}
}

package com.aegamesi.mc.po8;

import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.block.CraftChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.aegamesi.mc.po8.support.SLAPI;
import com.aegamesi.mc.po8.support.SerializedLocation;

public final class Po8Plugin extends JavaPlugin implements Listener {
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getCommand("po8").setExecutor(new Po8CommandExecutor(this));

		Po8.slapi = new SLAPI();
		saveDefaultConfig();
		Po8.load(getDataFolder(), getConfig());

		Po8.init(this);
	}

	public void onDisable() {
		Po8.save(getDataFolder());
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent evt) {
		if (!Po8.playerMap.containsKey(evt.getPlayer().getName()))
			Po8.playerMap.put(evt.getPlayer().getName(), new Po8Player());

		if (evt.getPlayer().hasPermission("po8.review"))
			Po8Util.message(evt.getPlayer(), "&dThere are&5 " + Po8.orderList.size() + " &dnew Po8 orders to be reviewed.");

		Po8.playerMap.get(evt.getPlayer().getName()).isReviewingOrder = false;

		/*
		 * if (evt.getPlayer().getName() == "PickleMan")
		 * evt.getPlayer().setPlayerisNsanidy(true);
		 */

		// ^^ foiled!
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent evt) {
		if (Po8.playerMap.get(evt.getPlayer().getName()).reviewOrder != null) {
			Po8.orderList.add(Po8.playerMap.get(evt.getPlayer().getName()).reviewOrder);
			Po8.playerMap.get(evt.getPlayer().getName()).reviewOrder = null;
		}
	}

	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent evt) {
		if (evt.getView().getType() == InventoryType.CHEST && evt.getInventory().getHolder() instanceof CraftChest) {
			CraftChest cc = (CraftChest) evt.getInventory().getHolder();
			if (Po8.chestMap.containsKey(new SerializedLocation(cc.getLocation()))) {
				int type = Po8.chestMap.get(new SerializedLocation(cc.getLocation()));
				evt.setCancelled(true);
				ItemStack[] inv = Po8.playerMap.get(evt.getPlayer().getName()).getInventory(type);
				Po8InventoryHolder holder = new Po8InventoryHolder(type, evt.getPlayer().getName(), inv);
				evt.getPlayer().openInventory(holder.getInventory());
			}
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent evt) {
		if (evt.getInventory().getHolder() instanceof Po8InventoryHolder) {
			// save?
			Po8InventoryHolder hold = (Po8InventoryHolder) evt.getInventory().getHolder();
			Po8Player player = Po8.playerMap.get(hold.player);
			ItemStack[] stacks = evt.getInventory().getContents();
			if (hold.type == Po8.BUY) {
				if (player.extendedInv.size() > 0)
					Po8Util.message((CommandSender) evt.getPlayer(), "&dYou have items pending placement in your buy chest. Clear some space to receive them");
				for (int i = 0; i < stacks.length; i++) {
					if (player.extendedInv.size() <= 0)
						break;
					if (stacks[i] == null)
						stacks[i] = player.extendedInv.remove(0).unbox();
				}
			}
			Po8.playerMap.get(hold.player).setInventory(hold.type, stacks);
		}
		Po8.playerMap.get(evt.getPlayer().getName()).isReviewingOrder = false;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent evt) {
		if (Po8.playerMap.get(evt.getWhoClicked().getName()).isReviewingOrder)
			evt.setCancelled(true);
	}

	// for use in other plugins
	// returns new balance
	// returns -1 if user not found
	public double balanceAdd(String name, double amount) {
		if (!Po8.playerMap.containsKey(name))
			return -1;
		Po8.playerMap.get(name).balance += amount;
		return Po8.playerMap.get(name).balance;
	}
}
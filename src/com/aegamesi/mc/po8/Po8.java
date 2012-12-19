package com.aegamesi.mc.po8;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.aegamesi.mc.po8.support.SLAPI;
import com.aegamesi.mc.po8.support.SerializedLocation;

public class Po8 {
	public static HashMap<SerializedLocation, Integer> chestMap;
	public static HashMap<String, Po8Player> playerMap;
	public static HashMap<String, Integer> stockMap;
	public static HashMap<String, Po8Item> itemMap;
	public static ArrayList<Po8Order> orderList;

	public static SLAPI slapi;
	public static double commission;
	public static BufferedWriter writer;

	public static final int BUY = 1;
	public static final int SELL = 2;
	public static final int REVIEW = 3;

	@SuppressWarnings("unchecked")
	public static void load(File dataFolder, FileConfiguration config) {
		// create data folder
		try {
			if (!dataFolder.isDirectory())
				dataFolder.mkdirs();
			String path;
			File file;

			boolean exists = new File(dataFolder + File.separator + "log.csv").exists();
			writer = new BufferedWriter(new FileWriter(new File(dataFolder + File.separator + "log.csv"), true));
			if (!exists) {
				writer.write("from,to,time,amount,type,notes");
				writer.newLine();
				writer.flush();
			}

			// load file
			path = dataFolder + File.separator + "chestmap.bin";
			file = new File(path);
			if (file.exists())
				chestMap = slapi.load(path);
			else
				chestMap = new HashMap<SerializedLocation, Integer>();

			// load file
			path = dataFolder + File.separator + "stockmap.bin";
			file = new File(path);
			if (file.exists())
				stockMap = slapi.load(path);
			else
				stockMap = new HashMap<String, Integer>();

			// load file
			path = dataFolder + File.separator + "playermap.bin";
			file = new File(path);
			playerMap = new HashMap<String, Po8Player>();
			if (file.exists())
				playerMap = slapi.load(path);
			else
				playerMap = new HashMap<String, Po8Player>();

			// load file
			path = dataFolder + File.separator + "orderlist.bin";
			file = new File(path);
			orderList = new ArrayList<Po8Order>();
			if (file.exists())
				orderList = slapi.load(path);
			else
				orderList = new ArrayList<Po8Order>();

			// load itemMap from config
			itemMap = new HashMap<String, Po8Item>();
			List<Map<?, ?>> items = config.getMapList("items");
			for (int i = 0; i < items.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) items.get(i);
				String id = (String) map.get("id");
				boolean active = (int) map.get("active") == 1;
				Object minPrice = map.get("min_price");
				Object maxPrice = map.get("max_price");
				itemMap.put(id, new Po8Item(active, id, minPrice instanceof Integer ? (int) minPrice : (double) minPrice, maxPrice instanceof Integer ? (int) maxPrice : (double) maxPrice, (String) map.get("name"), (int) map.get("stackSize")));
				if (!stockMap.containsKey(id))
					stockMap.put(id, 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		commission = config.getDouble("commission");
	}

	public static void save(File dataFolder) {
		try {
			slapi.save(chestMap, dataFolder + File.separator + "chestmap.bin");
			slapi.save(stockMap, dataFolder + File.separator + "stockmap.bin");
			slapi.save(playerMap, dataFolder + File.separator + "playermap.bin");
			slapi.save(orderList, dataFolder + File.separator + "orderlist.bin");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void init(Po8Plugin plugin) {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				if(Po8.orderList.size() <= 0)
					return;
				
				Player[] players = Bukkit.getServer().getOnlinePlayers();
				for (Player player : players) {
					if (player.hasPermission("po8.review"))
						Po8Util.message(player, "&dThere are&5 " + Po8.orderList.size() + " &dnew Po8 orders to be reviewed.");
				}
			}
		}, 0L, 72000L);
	}

	public static int stockCheck(String id) {
		if (stockMap.containsKey(id) && Po8.itemMap.containsKey(id) && Po8.itemMap.get(id).active)
			return stockMap.get(id);
		return -1;
	}

	public static int stockAdd(String id, int amt) {
		int stock = stockCheck(id);
		if (stock >= 0) {
			stockMap.put(id, stock + amt);
			return stock + amt;
		}
		return stock;
	}
}

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import com.aegamesi.mc.po8.*;
import com.aegamesi.mc.po8.support.*;

public class Testbed {
	public static void main(String[] args) {
		HashMap<String, Po8Player> playerMap = null;

		SLAPI slapi = new SLAPI();
		try {
			playerMap = slapi.load("playermap.bin");
		} catch (Exception e) {
			e.printStackTrace();
		}

		Po8Player pickle = playerMap.get("PickleMan");
		System.out.println(pickle.extendedInv.size());
		ItemStack inv[] = pickle.getInventory(Po8.BUY);
		for (int i = 0; i < inv.length; i++) {
			System.out.println(inv[i] == null);
			if (inv[i] != null) {
				System.out.println(inv[i].getTypeId() + " - x" + inv[i].getAmount());
			}
			System.out.println("--");
		}
	}
}

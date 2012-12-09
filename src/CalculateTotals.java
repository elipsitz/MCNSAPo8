import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.aegamesi.mc.po8.Po8Player;
import com.aegamesi.mc.po8.support.SLAPI;

public class CalculateTotals {
	public static void main(String[] args) throws IOException {
		HashMap<String, Po8Player> oldMap = null;

		SLAPI slapi = new SLAPI();
		try {
			oldMap = slapi.load("defaults.bin");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		BufferedReader br = new BufferedReader(new FileReader("log.csv"));
		String line;
		br.readLine();
		double commission = 0.02;
		while ((line = br.readLine()) != null) {
		   //from,to,time,amount,type,notes
			String[] split = line.split(",");
			String from = split[0].substring(1, split[0].length() - 1);
			String to = split[1].substring(1, split[1].length() - 1);
			double amount = Double.parseDouble(split[3].substring(1, split[3].length() - 1));
			String type = split[4].substring(1, split[4].length() - 1);
			String notes = split[5].substring(1, split[5].length() - 1);
			
			if(type.equals("SELL")) {
				if(!oldMap.containsKey(to))
					oldMap.put(to, new Po8Player());
				if(!oldMap.containsKey(notes))
					oldMap.put(notes, new Po8Player());
				oldMap.get(to).balance += amount;
				if(notes != to)
					oldMap.get(notes).balance += (amount * commission);
			}
			if(type.equals("BUY")) {
				if(!oldMap.containsKey(from))
					oldMap.put(from, new Po8Player());
				if(!oldMap.containsKey(notes))
					oldMap.put(notes, new Po8Player());
				oldMap.get(from).balance -= amount;
				if(notes != from)
					oldMap.get(notes).balance += (amount * commission);
			}
			if(type.equals("P2P_TRANSFER")) {
				if(!oldMap.containsKey(to))
					oldMap.put(to, new Po8Player());
				if(!oldMap.containsKey(from))
					oldMap.put(from, new Po8Player());
				oldMap.get(from).balance -= amount;
				oldMap.get(to).balance += amount;
			}
			if(type.equals("GRANT")) {
				if(!oldMap.containsKey(to))
					oldMap.put(to, new Po8Player());
				oldMap.get(to).balance += amount;
			}
		}
		
		for (Map.Entry<String, Po8Player> entry : oldMap.entrySet()) {
		    String key = entry.getKey();
		    Po8Player value = entry.getValue();
		    System.out.println(key + ": " + value.balance);
		}
		
		br.close();
		
		
	}
}

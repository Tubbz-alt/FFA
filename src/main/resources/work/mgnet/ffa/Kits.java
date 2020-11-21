package work.mgnet.ffa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;

import com.google.common.io.Files;

public class Kits {
	public static void saveKit(String name, Inventory inventory, Path privateConfigDir) throws Exception {
		File kitFile = Paths.get(privateConfigDir.toString(), name + ".kit").toFile();
		if (!kitFile.exists()) kitFile.createNewFile();
		List<DataView> items = InventorySerializer.serializeInventory(inventory);
		StringBuilder json=new StringBuilder();
		
		
		for (DataView dataView : items) {
			if(dataView!=null) {
				json=json.append(DataFormats.JSON.write(dataView)+"\n");
			}
		}
		
		try {
			Files.write(json.toString().getBytes(), kitFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Inventory loadKit(String name, Path privateConfigDir) throws Exception {
		File kitFile = Paths.get(privateConfigDir.toString(), name + ".kit").toFile();
		if (!kitFile.exists()) {
			throw new Exception();
		}
		List<DataView> items = new ArrayList<DataView>();
		BufferedReader oos = new BufferedReader(new FileReader(kitFile));
		String json;
		while((json=oos.readLine())!=null) {
			items.add(DataFormats.JSON.read(json));
		}
		oos.close();
		Inventory inv = Inventory.builder().of(InventoryArchetypes.DOUBLE_CHEST).build(Sponge.getPluginManager().getPlugin("ffa").get());
		inv =InventorySerializer.deserializeInventory(items, inv);
		return inv;
	}
}

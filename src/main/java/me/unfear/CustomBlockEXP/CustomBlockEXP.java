package me.unfear.CustomBlockEXP;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomBlockEXP extends JavaPlugin implements Listener {

	private boolean preventSilk = true;
	private HashMap<Material, Integer> expAmounts = new HashMap<>();
	
	public void onEnable() {
		saveDefaultConfig();
		
		preventSilk = getConfig().getBoolean("prevent-silk");
		for (String key : getConfig().getConfigurationSection("blocks").getKeys(false)) {
			try {
				Material mat = Material.valueOf(key);
				Integer amount = getConfig().getInt("blocks." + key);
				
				expAmounts.put(mat, amount);
			} catch (IllegalArgumentException e) {
				getLogger().warning(key + " is not a valid block type! It will drop the default experience.");
			}
		}
		
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	void onMine(BlockBreakEvent e) {
		if (!expAmounts.containsKey(e.getBlock().getType()) || e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		if (preventSilk && e.getPlayer().getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) return;
		e.setExpToDrop(expAmounts.get(e.getBlock().getType()));
	}
}

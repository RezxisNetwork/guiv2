package net.rezxis.mchosting.gui;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class ItemBack extends GUIItem{

	private GUIWindow window;
	
	public ItemBack(GUIWindow window) {
		super(getIcon());
		this.window = window;
	}
	
	private static ItemStack getIcon() {
		ItemStack is = new ItemStack(Material.ARROW);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.AQUA+"Back");
		is.setItemMeta(im);
		return is;
	}

	@Override
	public GUIAction invClick(InventoryClickEvent e) {
		window.delayShow();
		return GUIAction.CLOSE;
	}
}

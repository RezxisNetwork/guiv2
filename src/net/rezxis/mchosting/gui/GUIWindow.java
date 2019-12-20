package net.rezxis.mchosting.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class GUIWindow implements Listener {
    private Inventory inv;
    private Map<Integer, GUIItem> items;
    private Player player;
    private JavaPlugin plugin;
/**
 *
 * @param name
 * @param rows 1=9 slots, 2=18 slots,...
 */
    public GUIWindow(Player player, String name, int rows, JavaPlugin plugin) {
        this.inv = Bukkit.createInventory(player, rows * 9, name);
        this.items = new HashMap<>(rows * 9);
		this.player = player;
		this.plugin = plugin;
    }

    public abstract HashMap<Integer,GUIItem> getOptions();
    
    public static void setItem(int slot, GUIItem item, HashMap<Integer,GUIItem> map) {
    	map.put(slot, item);
    }

	public static void setItem(int x, int y, GUIItem item, HashMap<Integer,GUIItem> map) {
		setItem(x + y*9, item, map);
	}

    public GUIItem getItem(int slot) {
        return this.items.get(slot);
    }

	public GUIItem getItem(int x, int y) {
		return getItem(x*9 + y);
	}

	@EventHandler
	public void Open(InventoryOpenEvent e) {
	}
	@EventHandler
	public void Click(InventoryClickEvent e) {
		if (player.getName().equals(e.getWhoClicked().getName())) {
			if (e.getClickedInventory() != null) {
				if(e.getClickedInventory().equals(inv)) {
					e.setCancelled(true);
					if (this.items.containsKey(e.getSlot())) {
						GUIItem item = this.items.get(e.getSlot());
						GUIAction act = item.invClick(e);
						if (act == GUIAction.CLOSE) {
							//HandlerList.unregisterAll(this);
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
								@Override
								public void run() {
									e.getWhoClicked().closeInventory();
								}}, 1);
						} else if (act == GUIAction.UPDATE) {
							load();
							((Player)e.getWhoClicked()).updateInventory();
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void Closed(InventoryCloseEvent e) {
		if (player.getName().equals(e.getPlayer().getName())) {
			if(e.getInventory().equals(inv)) {
				HandlerList.unregisterAll(this);
			}
		}
	}

	private void load() {
		this.inv.clear();
		this.items.clear();
		for (Entry<Integer,GUIItem> entry : getOptions().entrySet()) {
			this.inv.setItem(entry.getKey(), entry.getValue().getBukkitItem());
			this.items.put(entry.getKey(), entry.getValue());
		}
	}
	
    /*@Deprecated
    public Inventory getBukkitInventory() {
        return this.inv;
    }*/
    
    public Player getPlayer() {
    	return this.player;
    }
    
    public void delayShow() {
    	Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
    		public void run() {
    			show();
    		}
    	}, 4);
    }
    
	public void show() {
		load();
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		player.openInventory(inv);
	}
}

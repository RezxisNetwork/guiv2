package net.rezxis.mchosting.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class GUIItem {
    private ItemStack item;

    public GUIItem(ItemStack item) {
        this.item = item;
    }

    public ItemStack getBukkitItem() {
        return this.item;
    }

    public abstract GUIAction invClick(InventoryClickEvent e);
}


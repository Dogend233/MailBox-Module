package com.tripleying.dogend.mailbox.module.mcgui.holder;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class MCGUIHolder implements InventoryHolder {
    
    protected Inventory inv;
    
    public abstract void onClick(Player p, InventoryClickEvent evt);
    
    public void setInventory(Inventory inv){
        this.inv = inv;
    }
    
    @Override
    public Inventory getInventory() {
        return inv;
    }
    
}

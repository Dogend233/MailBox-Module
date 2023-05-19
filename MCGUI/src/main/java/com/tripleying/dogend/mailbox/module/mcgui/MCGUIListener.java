package com.tripleying.dogend.mailbox.module.mcgui;

import com.tripleying.dogend.mailbox.MailBox;
import com.tripleying.dogend.mailbox.module.mcgui.holder.MCGUIHolder;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;

public class MCGUIListener implements Listener {
    
    private final Map<Player, Consumer<String>> pm;
    
    public MCGUIListener(){
        pm = new LinkedHashMap();
    }
    
    @EventHandler
    public void onInvClick(InventoryClickEvent evt){
        InventoryHolder holder = evt.getInventory().getHolder();
        if(holder!=null && holder instanceof MCGUIHolder){
            evt.setCancelled(true);
            ((MCGUIHolder)holder).onClick((Player)evt.getWhoClicked(), evt);
        }
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent evt){
        if(pm.containsKey(evt.getPlayer())){
            evt.setCancelled(true);
            Consumer<String> csm = pm.get(evt.getPlayer());
            pm.remove(evt.getPlayer());
            new BukkitRunnable() {
                @Override
                public void run() {
                    csm.accept(MessageUtil.color(evt.getMessage()));
                }
            }.runTask(MailBox.getMailBox());
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent evt){
        if(pm.containsKey(evt.getPlayer())){
            pm.remove(evt.getPlayer());
        }
    }
    
    public void addPlayer(Player p, Consumer<String> csm){
        pm.put(p, csm);
    }
    
}

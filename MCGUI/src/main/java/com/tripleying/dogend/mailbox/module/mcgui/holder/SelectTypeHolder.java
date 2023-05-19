package com.tripleying.dogend.mailbox.module.mcgui.holder;

import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.manager.MailManager;
import com.tripleying.dogend.mailbox.module.mcgui.MCGUI;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.PlayerInventory;

public class SelectTypeHolder extends MCGUIHolder {
    
    private final Map<Integer, String> map;
    
    public SelectTypeHolder(){
        map = new HashMap();
    }
    
    @Override
    public void onClick(Player p, InventoryClickEvent evt){
        if(evt.getClickedInventory() instanceof PlayerInventory) return;
        if(map.containsKey(evt.getSlot())){
            p.closeInventory();
            SystemMail sm = MailManager.getMailManager().createSystemMail(map.get(evt.getSlot()));
            MCGUI.getMCGUI().openPreviewMailGUI(p, sm);
        }
    }
    
    public void addSlot(int slot, String type){
        map.put(slot, type);
    }
    
}

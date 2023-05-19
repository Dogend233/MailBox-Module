package com.tripleying.dogend.mailbox.module.mcgui.holder;

import com.tripleying.dogend.mailbox.api.mail.attach.AttachFile;
import com.tripleying.dogend.mailbox.module.commonguiapi.gui.ReplaceConfig;
import com.tripleying.dogend.mailbox.module.mcgui.MCGUI;
import com.tripleying.dogend.mailbox.module.mcgui.util.MCReplacePackage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ReplaceItemHolder extends MCGUIHolder {
    
    private int preview;
    private final String type;
    private final ReplaceConfig rc;
    private final MCReplacePackage mcrp;
    private final Set<Integer> map;
    private final LinkedList<Integer> air;
    
    public ReplaceItemHolder(ReplaceConfig rc, MCReplacePackage mcrp, String type){
        map = new HashSet();
        air = new LinkedList();
        this.type = type;
        this.rc = rc;
        this.mcrp = mcrp;
    }

    @Override
    public void onClick(Player p, InventoryClickEvent evt) {
        if(evt.getCurrentItem()==null) return;
        if(evt.getClickedInventory() instanceof PlayerInventory){
            if(!air.isEmpty()){
                Integer slot = air.removeFirst();
                evt.getInventory().setItem(slot, evt.getCurrentItem());
                map.add(slot);
            }
        }else{
            int slot = evt.getSlot();
            if(slot==preview){
                p.closeInventory();
                Inventory ivt = evt.getInventory();
                List<ItemStack> isl = new ArrayList();
                int i = 0;
                for(int s:map){
                    ItemStack is = ivt.getItem(s);
                    if(is!=null) isl.add(is);
                }
                rc.replaceValue(type, isl);
                MCGUI.getMCGUI().openPreviewMailGUI(rc, mcrp);
            }else if(map.contains(slot)){
                evt.setCurrentItem(null);
                map.remove(slot);
                air.add(slot);
                Collections.sort(air);
            }
        }
    }
    
    public void addSlot(int slot){
        if(!map.contains(slot)) map.add(slot);
    }
    
    public void addAir(int slot){
        if(!air.contains(slot)) air.add(slot);
    }
    
    public void setPreview(int slot){
        preview = slot;
    }
    
}

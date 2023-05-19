package com.tripleying.dogend.mailbox.module.mcgui.holder;

import com.tripleying.dogend.mailbox.module.commonguiapi.gui.ReplaceConfig;
import com.tripleying.dogend.mailbox.module.mcgui.MCGUI;
import com.tripleying.dogend.mailbox.module.mcgui.util.MCReplacePackage;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.PlayerInventory;

public class ReplaceListHolder extends MCGUIHolder {
    
    private int put;
    private int preview;
    private final String type;
    private final ReplaceConfig rc;
    private final MCReplacePackage mcrp;
    private final Map<Integer, String> map;
    
    public ReplaceListHolder(ReplaceConfig rc, MCReplacePackage mcrp, String type){
        map = new HashMap();
        this.rc = rc;
        this.mcrp = mcrp;
        this.type = type;
    }
    
    @Override
    public void onClick(Player p, InventoryClickEvent evt){
        if(evt.getClickedInventory() instanceof PlayerInventory) return;
        int slot = evt.getSlot();
        if(slot==preview){
            p.closeInventory();
            MCGUI.getMCGUI().openPreviewMailGUI(rc, mcrp);
        }else if(slot==put){
            p.closeInventory();
            MessageUtil.log(p, "请在下方输入你想要的文字:");
            MCGUI.getMCGUI().getListener().addPlayer(p, (msg) -> {
                mcrp.addList(type, msg, rc.getSystemMail());
                MCGUI.getMCGUI().openReplaceListGUI(rc, mcrp, type);
            });
        }else if(map.containsKey(slot)){
            p.closeInventory();
            mcrp.removeList(type, map.get(slot), rc.getSystemMail());
            MCGUI.getMCGUI().openReplaceListGUI(rc, mcrp, type);
        }
    }
    
    public void setPut(int put){
        this.put = put;
    }

    public void setPreview(int preview) {
        this.preview = preview;
    }
    
    public void addSlot(int slot, String value){
        map.put(slot, value);
    }
    
}
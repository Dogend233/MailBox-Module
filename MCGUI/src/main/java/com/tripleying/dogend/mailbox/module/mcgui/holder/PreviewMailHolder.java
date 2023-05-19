package com.tripleying.dogend.mailbox.module.mcgui.holder;

import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.module.commonguiapi.gui.ReplaceConfig;
import com.tripleying.dogend.mailbox.module.mcgui.util.MCReplacePackage;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.PlayerInventory;

public class PreviewMailHolder extends MCGUIHolder {
    
    private int send;
    private final ReplaceConfig rc;
    private final MCReplacePackage mcrp;
    private final Map<Integer, String> map;
    
    public PreviewMailHolder(ReplaceConfig rc, MCReplacePackage mcrp){
        map = new HashMap();
        this.rc = rc;
        this.mcrp = mcrp;
    }
    
    @Override
    public void onClick(Player p, InventoryClickEvent evt){
        if(evt.getClickedInventory() instanceof PlayerInventory) return;
        int slot = evt.getSlot();
        if(slot==send){
            p.closeInventory();
            SystemMail sm = rc.getSystemMail().sendMail();
            if(sm.getId()==0){
                MessageUtil.error(p, "邮件发送失败!");
            }else{
                MessageUtil.log(p, "邮件发送成功!");
            }
        }else if(map.containsKey(slot)){
            p.closeInventory();
            mcrp.replaceMethod(map.get(slot), rc);
        }
    }
    
    public void addSlot(int slot, String type){
        map.put(slot, type);
    }
    
    public void setSend(int slot){
        send = slot;
    }

    public ReplaceConfig getReplaceConfig() {
        return rc;
    }
    
}
package com.tripleying.dogend.mailbox.module.mcgui.holder;

import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.module.mcgui.MCGUI;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.PlayerInventory;

public class SystemListHolder extends MCGUIHolder {
    
    private final int page;
    private int pre;
    private int next;
    private final Map<Integer, SystemMail> map;
    
    public SystemListHolder(int page){
        map = new HashMap();
        this.page = page;
    }

    @Override
    public void onClick(Player p, InventoryClickEvent evt) {
        if(evt.getClickedInventory() instanceof PlayerInventory) return;
        int slot = evt.getSlot();
        if(slot==pre){
            p.closeInventory();
            MCGUI.getMCGUI().openPersonListGUI(p, page-1);
        }else if(slot==next){
            p.closeInventory();
            MCGUI.getMCGUI().openPersonListGUI(p, page+1);
        }else if(map.containsKey(slot)){
            SystemMail sm = map.get(slot);
            switch(evt.getClick()){
                case RIGHT:
                    p.closeInventory();
                    if(p.isOp()){
                        sm.deleteMail();
                        MCGUI.getMCGUI().openPersonListGUI(p, page);
                    }else{
                        MessageUtil.error(p, "删除邮件失败, 没有管理员权限");
                    }
                    break;
            }
        }
    }
    
    public void addSlot(int slot, SystemMail sm){
        map.put(slot, sm);
    }

    public void setPre(int pre) {
        this.pre = pre;
    }

    public void setNext(int next) {
        this.next = next;
    }
    
}

package com.tripleying.dogend.mailbox.module.mcgui.holder;

import com.tripleying.dogend.mailbox.api.mail.PersonMail;
import com.tripleying.dogend.mailbox.manager.MailManager;
import com.tripleying.dogend.mailbox.module.mcgui.MCGUI;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.PlayerInventory;

public class PersonListHolder extends MCGUIHolder {
    
    private final int page;
    private int pre;
    private int next;
    private int receive;
    private int delete;
    private final Map<Integer, PersonMail> map;
    
    public PersonListHolder(int page){
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
        }else if(slot==receive){
            p.closeInventory();
            long count = MailManager.getMailManager().receiveAllPersonMail(p);
            MessageUtil.log(p, "已领取"+count+"封邮件");
        }else if(slot==delete){
            p.closeInventory();
            long count = MailManager.getMailManager().clearPersonReceivedMail(p);
            MessageUtil.log(p, "已删除"+count+"封邮件");
        }else if(map.containsKey(slot)){
            PersonMail pm = map.get(slot);
            switch(evt.getClick()){
                case LEFT:
                    if(!pm.isReceived()){
                        p.closeInventory();
                        if(pm.receivedMail()){
                            MessageUtil.log(p, "领取邮件成功");
                        }else{
                            MessageUtil.error(p, "领取邮件失败");
                        }
                    }
                    break;
                case RIGHT:
                    p.closeInventory();
                    if(pm.isReceived()){
                        p.closeInventory();
                        if(pm.isReceived()){
                            pm.deleteMail();
                            MCGUI.getMCGUI().openPersonListGUI(p, page);
                        }else{
                            MessageUtil.error(p, "删除邮件失败, 有附件未领取");
                        }
                    }
                    break;
            }
        }
    }
    
    public void addSlot(int slot, PersonMail pm){
        map.put(slot, pm);
    }

    public void setPre(int pre) {
        this.pre = pre;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public void setReceive(int receive) {
        this.receive = receive;
    }

    public void setDelete(int delete) {
        this.delete = delete;
    }
    
}

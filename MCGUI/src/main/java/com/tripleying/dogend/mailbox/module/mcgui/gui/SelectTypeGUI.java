package com.tripleying.dogend.mailbox.module.mcgui.gui;

import com.tripleying.dogend.mailbox.module.mcgui.util.GUIUtil;
import com.tripleying.dogend.mailbox.module.mcgui.util.GUIPackage;
import com.tripleying.dogend.mailbox.MailBox;
import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.manager.MailManager;
import com.tripleying.dogend.mailbox.module.mcgui.holder.SelectTypeHolder;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SelectTypeGUI {
    
    private final GUIPackage gp;
    private final String title;
    private final ItemStack t;
    private final ItemStack tp;
    
    public SelectTypeGUI(YamlConfiguration yml){
        gp = new GUIPackage(yml);
        title = MessageUtil.color(yml.getString("title", "&b[选择邮件类型]"));
        t = GUIUtil.setItemStackDisplayName(GUIUtil.getItemStack(yml.getString("t.id", "PAPER")), MessageUtil.color(yml.getString("t.name", "&6[%type%]")));
        tp = GUIUtil.setItemStackDisplayName(GUIUtil.getItemStack(yml.getString("t.no.id", "AIR")), MessageUtil.color(yml.getString("t.no.name", " ")));
    }
    
    public void open(Player p){
        char[][] gc = gp.getGUI();
        SelectTypeHolder holder = new SelectTypeHolder();
        Inventory inv = Bukkit.createInventory(holder, gc.length*9, title);
        holder.setInventory(inv);
        MailManager mm = MailBox.getMailBox().getMailManager();
        List<String> tl = new ArrayList();
        for(SystemMail sm:mm.getSystemMailTypeList()){
            if(sm.couldSendMail(p)) tl.add(sm.getType());
        }
        Iterator<String> it = tl.iterator();
        ItemStack[] iss = new ItemStack[gc.length*9];
        int i = 0;
        for(int j=0;j<gc.length;j++){
            for(int k=0;k<9;k++){
                char c = gc[j][k];
                if(c=='t'){
                    if(it.hasNext()){
                        String type = it.next();
                        iss[i] = t.clone();
                        ItemMeta im = iss[i].getItemMeta();
                        im.setDisplayName(im.getDisplayName().replaceAll("%type%", mm.getSystemMailDisplay(type)));
                        iss[i].setItemMeta(im);
                        holder.addSlot(i++, type);
                    }else{
                        iss[i++] = tp.clone();
                    }
                }else{
                    iss[i++] = gp.getItemStack(gc[j][k], p);
                }
            }
        }
        inv.setContents(iss);
        p.openInventory(inv);
    }
    
}

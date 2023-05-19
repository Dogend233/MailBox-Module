package com.tripleying.dogend.mailbox.module.mcgui.gui;

import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.manager.MailManager;
import com.tripleying.dogend.mailbox.module.mcgui.holder.SystemListHolder;
import com.tripleying.dogend.mailbox.module.mcgui.util.GUIPackage;
import com.tripleying.dogend.mailbox.module.mcgui.util.GUIUtil;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SystemListGUI {
    
    private final GUIPackage gp;
    private final String title;
    
    public SystemListGUI(YamlConfiguration yml){
        gp = new GUIPackage(yml);
        title = MessageUtil.color(yml.getString("title", "&b[邮件列表]"));
    }
    
    public void open(Player p, SystemMail smt, int page){
        if(page<1) page=1;
        char[][] gc = gp.getGUI();
        String gcs = new String();
        for(int i=0;i<gc.length;i++){
            gcs = gcs.concat(new String(gc[i]));
        }
        int mcount = gcs.length()-gcs.replaceAll("m","").length();
        List<SystemMail> sml;
        int pages = MailManager.getMailManager().getSystemMailPages(smt, mcount);
        if(pages==0){
            sml = new ArrayList();
        }else{
            if(page>pages) page=pages;
            sml = MailManager.getMailManager().getSystemMailList(smt.getType(), mcount, page);
        }
        SystemListHolder holder = new SystemListHolder(page);
        Inventory inv = Bukkit.createInventory(holder, gc.length*9, title);
        holder.setInventory(inv);
        Iterator<SystemMail> it = sml.iterator();
        ItemStack[] iss = new ItemStack[gc.length*9];
        int i = 0;
        for(int j=0;j<gc.length;j++){
            for(int k=0;k<9;k++){
                char c = gc[j][k];
                if(c=='m'){
                    if(it.hasNext()){
                        SystemMail sm = it.next();
                        iss[i] = GUIUtil.mail2ItemStack(sm);
                        holder.addSlot(i++, sm);
                    }else{
                        iss[i++] = null;
                    }
                }else{
                    switch (c) {
                        case 'p':
                            holder.setPre(i);
                            break;
                        case 'n':
                            holder.setNext(i);
                            break;
                    }
                    iss[i++] = gp.getItemStack(gc[j][k], p);
                }
            }
        }
        inv.setContents(iss);
        p.openInventory(inv);
    }
    
}

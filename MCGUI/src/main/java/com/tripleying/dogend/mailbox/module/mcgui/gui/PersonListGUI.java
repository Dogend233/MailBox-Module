package com.tripleying.dogend.mailbox.module.mcgui.gui;

import com.tripleying.dogend.mailbox.api.mail.PersonMail;
import com.tripleying.dogend.mailbox.manager.MailManager;
import com.tripleying.dogend.mailbox.module.mcgui.holder.PersonListHolder;
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

public class PersonListGUI {
    
    private final GUIPackage gp;
    private final String title;
    
    public PersonListGUI(YamlConfiguration yml){
        gp = new GUIPackage(yml);
        title = MessageUtil.color(yml.getString("title", "&b[邮件列表]"));
    }
    
    public void open(Player p, int page){
        if(page<1) page=1;
        char[][] gc = gp.getGUI();
        String gcs = new String();
        for(int i=0;i<gc.length;i++){
            gcs = gcs.concat(new String(gc[i]));
        }
        int mcount = gcs.length()-gcs.replaceAll("m","").length();
        List<PersonMail> pml;
        int pages = MailManager.getMailManager().getPersonMailPages(p, mcount);
        if(pages==0){
            pml = new ArrayList();
        }else{
            if(page>pages) page=pages;
            pml = MailManager.getMailManager().getPersonMailList(p, mcount, page);
        }
        PersonListHolder holder = new PersonListHolder(page);
        Inventory inv = Bukkit.createInventory(holder, gc.length*9, title);
        holder.setInventory(inv);
        Iterator<PersonMail> it = pml.iterator();
        ItemStack[] iss = new ItemStack[gc.length*9];
        int i = 0;
        for(int j=0;j<gc.length;j++){
            for(int k=0;k<9;k++){
                char c = gc[j][k];
                if(c=='m'){
                    if(it.hasNext()){
                        PersonMail pm = it.next();
                        pm.seeMail();
                        iss[i] = GUIUtil.mail2ItemStack(pm);
                        holder.addSlot(i++, pm);
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
                        case 'r':
                            holder.setReceive(i);
                            break;
                        case 'd':
                            holder.setDelete(i);
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

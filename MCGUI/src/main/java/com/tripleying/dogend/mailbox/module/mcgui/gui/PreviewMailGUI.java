package com.tripleying.dogend.mailbox.module.mcgui.gui;

import com.tripleying.dogend.mailbox.module.commonguiapi.gui.ReplaceConfig;
import com.tripleying.dogend.mailbox.module.commonguiapi.gui.ReplacePackage;
import com.tripleying.dogend.mailbox.module.mcgui.util.GUIUtil;
import com.tripleying.dogend.mailbox.module.mcgui.util.GUIPackage;
import com.tripleying.dogend.mailbox.module.mcgui.holder.PreviewMailHolder;
import com.tripleying.dogend.mailbox.module.mcgui.util.MCReplacePackage;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import com.tripleying.dogend.mailbox.util.TimeUtil;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PreviewMailGUI {
    
    private final GUIPackage gp;
    private final String title;
    private final ItemStack r;
    private final ItemStack rp;
    
    public PreviewMailGUI(YamlConfiguration yml){
        gp = new GUIPackage(yml);
        title = MessageUtil.color(yml.getString("title", "&b[选择邮件类型]"));
        r = GUIUtil.setItemStackDisplayName(GUIUtil.getItemStack(yml.getString("r.id", "PAPER")), MessageUtil.color(yml.getString("r.name", "&6[%type%]")));
        rp = GUIUtil.setItemStackDisplayName(GUIUtil.getItemStack(yml.getString("r.no.id", "AIR")), MessageUtil.color(yml.getString("r.no.name", " ")));
    }
    
    public void open(ReplaceConfig rc, MCReplacePackage mcrp){
        if(rc==null) return;
        Player p = rc.getPlayer();
        char[][] gc = gp.getGUI();
        PreviewMailHolder holder = new PreviewMailHolder(rc, mcrp);
        Inventory inv = Bukkit.createInventory(holder, gc.length*9, title);
        holder.setInventory(inv);
        rc.getSystemMail().setSendtime(TimeUtil.currentTimeString());
        Map<String, ReplacePackage> rm = rc.getReplaceMap();
        Iterator<Map.Entry<String, ReplacePackage>> it = rm.entrySet().iterator();
        ItemStack[] iss = new ItemStack[gc.length*9];
        int i = 0;
        for(int j=0;j<gc.length;j++){
            for(int k=0;k<9;k++){
                char c = gc[j][k];
                switch (c) {
                    case 'r':
                        if(it.hasNext()){
                            Map.Entry<String, ReplacePackage> me = it.next();
                            ReplacePackage rp = me.getValue();
                            iss[i] = r.clone();
                            ItemMeta im = iss[i].getItemMeta();
                            im.setDisplayName(im.getDisplayName().replaceAll("%type%", rp.getName()));
                            switch(rp.getType()){
                                case ItemStackList:
                                    break;
                                case StringList:
                                    Object v = rp.getValue(rc.getSystemMail());
                                    if(v!=null && v instanceof List){
                                        try{
                                            im.setLore((List<String>)v);
                                        }catch(Exception ex){}
                                    }   break;
                                default:
                                    Object vs = rp.getValue(rc.getSystemMail());
                                    if(vs!=null){
                                        try{
                                            im.setLore(Arrays.asList(vs.toString()));
                                        }catch(Exception ex){}
                                    }
                            }
                            iss[i].setItemMeta(im);
                            holder.addSlot(i++, me.getKey());
                        }else{
                            iss[i++] = rp.clone();
                        }   break;
                    case 'm':
                        iss[i++] = GUIUtil.mail2ItemStack(rc.getSystemMail());
                        break;
                    case 's':
                        holder.setSend(i);
                    default:
                        iss[i++] = gp.getItemStack(gc[j][k], p);
                        break;
                }
            }
        }
        inv.setContents(iss);
        rc.getPlayer().openInventory(inv);
    }
    
}

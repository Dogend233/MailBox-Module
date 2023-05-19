package com.tripleying.dogend.mailbox.module.mcgui.gui;

import com.tripleying.dogend.mailbox.module.commonguiapi.gui.ReplaceConfig;
import com.tripleying.dogend.mailbox.module.commonguiapi.gui.ReplacePackage;
import com.tripleying.dogend.mailbox.module.mcgui.holder.ReplaceListHolder;
import com.tripleying.dogend.mailbox.module.mcgui.util.GUIPackage;
import com.tripleying.dogend.mailbox.module.mcgui.util.GUIUtil;
import com.tripleying.dogend.mailbox.module.mcgui.util.MCReplacePackage;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ReplaceListGUI {
    
    private final GUIPackage gp;
    private final String title;
    private final ItemStack n;
    private final ItemStack np;
    
    public ReplaceListGUI(YamlConfiguration yml){
        gp = new GUIPackage(yml);
        title = MessageUtil.color(yml.getString("title", "&b[修改%type%]"));
        n = GUIUtil.setItemStackDisplayName(GUIUtil.getItemStack(yml.getString("n.id", "PAPER")), MessageUtil.color(yml.getString("n.name", "&6[%type%]")));
        np = GUIUtil.setItemStackDisplayName(GUIUtil.getItemStack(yml.getString("n.no.id", "AIR")), MessageUtil.color(yml.getString("n.no.name", " ")));
    }
    
    public void openGui(ReplaceConfig rc, MCReplacePackage mcrp, String type){
        char[][] gc = gp.getGUI();
        ReplacePackage rp = rc.getValue(type);
        if(rp==null) return;
        Player p = rc.getPlayer();
        String display = rp.getName();
        List<String> sl = (List)rp.getValue(rc.getSystemMail());
        ReplaceListHolder holder = new ReplaceListHolder(rc, mcrp, type);
        Inventory inv = Bukkit.createInventory(holder, gc.length*9, title.replace("%type%", display));
        holder.setInventory(inv);
        ItemStack[] iss = new ItemStack[gc.length*9];
        Iterator it = sl.iterator();
        int i = 0;
        for(int j=0;j<gc.length;j++){
            for(int k=0;k<9;k++){
                char c = gc[j][k];
                switch (c) {
                    case 'n':
                        if(it.hasNext()){
                            String str = it.next().toString();
                            iss[i] = n.clone();
                            ItemMeta im = iss[i].getItemMeta();
                            im.setDisplayName(im.getDisplayName().replaceAll("%text%", str));
                            iss[i].setItemMeta(im);
                            holder.addSlot(i++, str);
                        }else{
                            iss[i++] = np.clone();
                        }   break;
                    case 'p':
                        holder.setPut(i);
                        iss[i] = gp.getItemStack(gc[j][k], p);
                        ItemMeta imp = iss[i].getItemMeta();
                        imp.setDisplayName(imp.getDisplayName().replace("%type%", display));
                        iss[i++].setItemMeta(imp);
                        break;
                    case 'r':
                        holder.setPreview(i);
                        iss[i++] = gp.getItemStack(gc[j][k], p);
                        break;
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
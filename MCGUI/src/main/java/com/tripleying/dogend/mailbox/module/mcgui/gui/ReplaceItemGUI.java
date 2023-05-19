package com.tripleying.dogend.mailbox.module.mcgui.gui;

import com.tripleying.dogend.mailbox.module.commonguiapi.gui.ReplaceConfig;
import com.tripleying.dogend.mailbox.module.mcgui.holder.ReplaceItemHolder;
import com.tripleying.dogend.mailbox.module.mcgui.util.GUIPackage;
import com.tripleying.dogend.mailbox.module.mcgui.util.MCReplacePackage;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ReplaceItemGUI {
    
    private final GUIPackage gp;
    private final String title;
    
    public ReplaceItemGUI(YamlConfiguration yml){
        gp = new GUIPackage(yml);
        title = MessageUtil.color(yml.getString("title", "&b[修改物品]"));
    }
    
    public void openGui(ReplaceConfig rc, MCReplacePackage mcrp, String type){
        char[][] gc = gp.getGUI();
        Object o = rc.getValue(type).getValue(rc.getSystemMail());
        if(o==null || !(o instanceof List) || (!((List)o).isEmpty() && !(((List)o).get(0) instanceof ItemStack))) return;
        Player p = rc.getPlayer();
        List lis = (List)o;
        ReplaceItemHolder holder = new ReplaceItemHolder(rc, mcrp, type);
        Inventory inv = Bukkit.createInventory(holder, gc.length*9, title);
        holder.setInventory(inv);
        ItemStack[] iss = new ItemStack[gc.length*9];
        Iterator it = lis.iterator();
        int i = 0;
        for(int j=0;j<gc.length;j++){
            for(int k=0;k<9;k++){
                char c = gc[j][k];
                switch (c) {
                    case 'i':
                        if(it.hasNext()){
                            iss[i] = (ItemStack)it.next();
                            holder.addSlot(i++);
                        }else{
                            iss[i] = null;
                            holder.addAir(i++);
                        }   break;
                    case 'p':
                        holder.setPreview(i);
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

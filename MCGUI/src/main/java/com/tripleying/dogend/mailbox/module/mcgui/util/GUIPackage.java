package com.tripleying.dogend.mailbox.module.mcgui.util;

import com.tripleying.dogend.mailbox.MailBox;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class GUIPackage {
    
    private final char[][] gui;
    private final Map<Character, ItemStack> map;
    
    public GUIPackage(YamlConfiguration yml){
        List<String> list = yml.getStringList("gui");
        gui = new char[list.size()][9];
        for(int i=0;i<gui.length;i++){
            String row = list.get(i);
            int length = row.length();
            if(length>9){
                row = row.substring(0, 9);
            }
            if(length<9){
                for(int j=0;j<9-length;j++){
                    row += "x";
                }
            }
            gui[i] = row.toCharArray();
        }
        map = new HashMap();
        for(String key:yml.getConfigurationSection("slot").getKeys(false)){
            char k = key.charAt(0);
            ItemStack is = GUIUtil.getItemStack(yml.getString("slot."+key+".id", "AIR"));
            if(!Material.AIR.equals(is.getType())){
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(MessageUtil.color(yml.getString("slot."+key+".name", " ")));
                if(yml.contains("slot."+key+".lore")){
                    List<String> lore = yml.getStringList("slot."+key+".lore");
                    lore.replaceAll(l -> MessageUtil.color(l));
                    im.setLore(lore);
                }
                is.setItemMeta(im);
                if(im instanceof SkullMeta && yml.contains("slot."+key+".skull")){
                    is = new SkullItemStack(yml.getString("slot."+key+".skull"), is);
                }
            }
            map.put(k, is);
        }
    }
    
    public char[][] getGUI(){
        return gui;
    }
    
    public ItemStack getItemStack(char c, Player p){
        ItemStack is = map.getOrDefault(c, null);
        if(is==null){
            return null;
        }else{
            ItemStack isc;
            if(is instanceof SkullItemStack){
                isc = ((SkullItemStack)is).getSkullItemStack(p);
            }else{
                isc = is.clone();
            }
            if(isc.hasItemMeta()){
                ItemMeta im = isc.getItemMeta();
                if(im.hasDisplayName()){
                    im.setDisplayName(im.getDisplayName().replaceAll("%player%", p.getName()));
                }
                if(im.hasLore()){
                    List<String> lore = im.getLore();
                    lore.replaceAll(l -> l.replaceAll("%player%", p.getName()));
                    im.setLore(lore);
                }
                isc.setItemMeta(im);
            }
            return isc;
        }
    }
    
    private class SkullItemStack extends ItemStack{

        private final String owner;
        private final ItemStack is;
        private final SkullMeta sm;
        
        public SkullItemStack(String owner, ItemStack is){
            this.owner = owner;
            this.is = is;
            sm = (SkullMeta)is.getItemMeta();
            if(!"%player%".equals(owner)){
                try{
                    sm.setOwner(owner);
//                    sm.setOwningPlayer(Bukkit.getOfflinePlayer(owner));
                }catch(Exception ex){
                    if(ex instanceof NoSuchMethodException){
                        sm.setOwner(owner);
                    }
                }
            }
        }
        
        public ItemStack getSkullItemStack(Player p){
            ItemStack isc = is.clone();
            SkullMeta smc = sm.clone();
            if("%player%".equals(owner)){
                try{
                    smc.setOwner(p.getName());
//                    smc.setOwningPlayer(p);
                }catch(Exception ex){
                    if(ex instanceof NoSuchMethodException){
                        sm.setOwner(p.getName());
                    }
                }
            }
            isc.setItemMeta(smc);
            return isc;
        }
        
    }
    
}

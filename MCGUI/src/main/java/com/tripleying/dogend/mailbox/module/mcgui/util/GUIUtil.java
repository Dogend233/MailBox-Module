package com.tripleying.dogend.mailbox.module.mcgui.util;

import com.tripleying.dogend.mailbox.api.mail.BaseMail;
import com.tripleying.dogend.mailbox.api.mail.PersonMail;
import com.tripleying.dogend.mailbox.api.mail.attach.AttachFile;
import com.tripleying.dogend.mailbox.api.mail.attach.AttachMoney;
import com.tripleying.dogend.mailbox.manager.MoneyManager;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIUtil {
    
    private static Enchantment enhance;
    private static ItemStack read;
    private static List<String> read_lore;
    private static ItemStack unread;
    private static List<String> unread_lore;
    private static String escape_body;
    private static String escape_attach_header;
    private static String escape_attach_item_header;
    private static String escape_attach_item_body;
    private static String escape_attach_money;
    private static String escape_attach_command;
    
    public static void init(YamlConfiguration yml){
        enhance = Enchantment.values()[0];
//        for(Enchantment en:Enchantment.values()){
//            System.out.println(en.toString());
//        }
//        try{
//            enhance = Enchantment.getByName(yml.getString("mail.enhance_tag", "ARROW_INFINITE"));
//        }catch(Exception ex){
//            ex.printStackTrace();
//            enhance = Enchantment.ARROW_INFINITE;
//        }
        read = setItemStackDisplayName(getItemStack(yml.getString("mail.read.id", "PAPER")), MessageUtil.color(yml.getString("mail.read.name", "&6[%type%] - &r&a[%title%]")));
        read_lore = yml.getStringList("mail.read.lore");
        read_lore.replaceAll(l -> MessageUtil.color(l));
        if(yml.getBoolean("mail.read.enhance", false)) setEnchantment(read);
        unread = setItemStackDisplayName(getItemStack(yml.getString("mail.unread.id", "PAPER")), MessageUtil.color(yml.getString("mail.unread.name", "&6[%type%] - &r&a[%title%]")));
        unread_lore = yml.getStringList("mail.unread.lore");
        unread_lore.replaceAll(l -> MessageUtil.color(l));
        if(yml.getBoolean("mail.unread.enhance", true)) setEnchantment(unread);
        escape_body = MessageUtil.color(yml.getString("escape.body", ""));
        escape_attach_header = MessageUtil.color(yml.getString("escape.attach.header", ""));
        escape_attach_item_header = MessageUtil.color(yml.getString("escape.attach.item.header", ""));
        escape_attach_item_body = MessageUtil.color(yml.getString("escape.attach.item.body", ""));
        escape_attach_money = MessageUtil.color(yml.getString("escape.attach.money", ""));
        escape_attach_command = MessageUtil.color(yml.getString("escape.attach.command", ""));
    }
    
    public static ItemStack getItemStack(String id){
        ItemStack is;
        if(id.contains(":")){
            String[] ids = id.split(":");
            is = getItemStack(Material.getMaterial(ids[0]));
            is.setDurability(Short.parseShort(ids[1]));
        }else{
            is = getItemStack(Material.getMaterial(id));
        }
        return is;
    }
    
    public static ItemStack setEnchantment(ItemStack is){
        ItemMeta im = is.getItemMeta();
        im.addEnchant(enhance, 1, true);
        try{
            Class.forName("org.bukkit.inventory.ItemFlag");
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }catch(ClassNotFoundException ex){}
        is.setItemMeta(im);
        return is;
    }
    
    public static ItemStack getItemStack(Material ml){
        return new ItemStack(ml==null?Material.AIR:ml);
    }
    
    public static ItemStack setItemStackDisplayName(ItemStack is, String name){
        if(is==null || Material.AIR.equals(is.getType())) return is;
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        is.setItemMeta(im);
        return is;
    }
    
    public static ItemStack setLore(ItemStack is, List<String> lore){
        if(is.hasItemMeta()){
            ItemMeta im = is.getItemMeta();
            im.setLore(lore);
            is.setItemMeta(im);
        }
        return is;
    }
    
    public static String getItemName(ItemStack is){
        if(is==null) return "AIR";
        ItemMeta im = is.getItemMeta();
        if(im!=null && im.hasDisplayName()){
            return im.getDisplayName();
        }
        return is.getType().toString();
    }
    
    public static ItemStack mail2ItemStack(BaseMail bm){
        ItemStack is;
        List<String> lore = new ArrayList();
        List<String> prel = new ArrayList();
        if(bm instanceof PersonMail && ((PersonMail)bm).isReceived()){
            is = read.clone();
            prel.addAll(read_lore);
        }else{
            is = unread.clone();
            prel.addAll(unread_lore);
        }
        setItemStackDisplayName(is, is.getItemMeta().getDisplayName().replaceAll("%type%", bm.getDisplay()).replaceAll("%title%", bm.getTitle()));
        String sender = bm.getSender();
        String sendtime = bm.getSendtime();
        for(String l:prel){
            if(l.equals("$body$")){
                for(String b:bm.getBody()){
                    lore.add(escape_body.replace("%body%", b));
                }
            }else if(l.equals("$attach$")){
                if(bm.getAttachFile().hasAttach()){
                    lore.add(escape_attach_header);
                    AttachFile attach = bm.getAttachFile();
                    List<ItemStack> isl = attach.getItemStacks();
                    if(!isl.isEmpty()){
                        lore.add(escape_attach_item_header);
                        isl.forEach(ist -> lore.add(escape_attach_item_body.replace("%name%", getItemName(ist)).replace("%amount%", ist.getAmount()+"")));
                    }
                    Map<String, AttachMoney> mmap = attach.getMoneys();
                    if(!mmap.isEmpty()){
                        MoneyManager mm = MoneyManager.getMoneyManager();
                        mmap.forEach((s,m) -> lore.add(escape_attach_money.replace("%type%", mm.getMoneyDisplay(s)).replace("%count%", m.getCount().toString())));
                    }
                    if(attach.getCommands().hasCommand()){
                        lore.add(escape_attach_command);
                    }
                }
            }else{
                lore.add(l.replaceAll("%sender%", sender).replaceAll("%time%", sendtime));
            }
        }
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }
    
}

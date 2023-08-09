package com.tripleying.dogend.mailbox.module.vexviewgui.vexview;

import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.util.Arrays;
import java.util.List;
import lk.vexview.api.VexViewAPI;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class VexViewUtil {
    
    public static double[] getPlayerClientScale(Player p) {
        double[] scale = new double[2];
        scale[0] = 1D;
        scale[1] = 1D;
        try {
            int w = VexViewAPI.getPlayerClientWindowWidth(p);
            int h = VexViewAPI.getPlayerClientWindowHeight(p);
            scale[0] = w / 480D;
            scale[1] = h / 255D;
        } catch (Exception ignored) {}
        return scale;
    }
    
    public static GuiPackage getGuiPackage(ConfigurationSection cs){
        return new GuiPackage(
                cs.getString("img"),
                cs.getInt("x"),
                cs.getInt("y"),
                cs.getInt("w"),
                cs.getInt("h"));
    }
    
    public static InvGuiPackage getInvGuiPackage(ConfigurationSection cs){
        return new InvGuiPackage(
                cs.getString("img"),
                cs.getInt("x"),
                cs.getInt("y"),
                cs.getInt("w"),
                cs.getInt("h"),
                cs.getInt("ix"),
                cs.getInt("iy"));
    }
    
    public static ImagePackage getImagePackage(ConfigurationSection cs){
        if(cs.getString("img", "").endsWith(".gif")){
            return getGifImagePackage(cs);
        }else return new ImagePackage(
                cs.getString("img"),
                cs.getInt("x"),
                cs.getInt("y"),
                cs.getInt("w"),
                cs.getInt("h"));
    }
    
    public static GifImagePackage getGifImagePackage(ConfigurationSection cs){
        return new GifImagePackage(
                cs.getString("img"),
                cs.getInt("x"),
                cs.getInt("y"),
                cs.getInt("w"),
                cs.getInt("h"),
                cs.getInt("interval", 1));
    }
    
    public static TextPackage getTextPackage(ConfigurationSection cs){
        return new TextPackage(
                cs.getInt("x"),
                cs.getInt("y"),
                getStringList(cs, "text"));
    }
    
    public static TextFieldPackage getTextFieldPackage(ConfigurationSection cs){
        return  new TextFieldPackage(
                cs.getInt("x"),
                cs.getInt("y"),
                cs.getInt("w"),
                cs.getInt("h"),
                cs.getInt("max", 100),
                cs.contains("place")?cs.getString("place"):null);
    }
    
    public static TextAreaPackage getTextAreaPackage(ConfigurationSection cs){
        return  new TextAreaPackage(
                cs.getInt("x"),
                cs.getInt("y"),
                cs.getInt("w"),
                cs.getInt("h"),
                cs.getInt("max", 100),
                cs.contains("place")?getStringList(cs, "place"):null);
    }
    
    public static ButtonPackage getButtonPackage(ConfigurationSection cs){
        return new ButtonPackage(
                MessageUtil.color(cs.getString("text", "")),
                cs.getString(cs.contains("img1")?"img1":"img"),
                cs.getString(cs.contains("img2")?"img2":"img"),
                cs.getInt("x"),
                cs.getInt("y"),
                cs.getInt("w"),
                cs.getInt("h"),
                cs.contains("hvoer")?getStringList(cs, "hover"):null);
    }
    
    public static CheckBoxPackage getCheckBoxPackage(ConfigurationSection cs){
        return new CheckBoxPackage(
                cs.getString("img1"),
                cs.getString("img2"),
                cs.getInt("x"),
                cs.getInt("y"),
                cs.getInt("w"),
                cs.getInt("h"));
    }
    
    public static ListPackage getListPackage(ConfigurationSection cs){
        ListPackage vsl = new ListPackage(
                cs.getInt("c"),
                cs.getInt("x"),
                cs.getInt("y"),
                cs.getInt("w"),
                cs.getInt("h"),
                cs.getInt("mh"),
                cs.getInt("sh"),
                cs.getInt("oh"),
                cs.getInt("ox"),
                cs.getInt("oy")
        );
        return vsl;
    }
    
    public static ItemListPackage getItemListPackage(ConfigurationSection cs){
        ItemListPackage vsl = new ItemListPackage(
                cs.getInt("c"),
                cs.getInt("x"),
                cs.getInt("y"),
                cs.getInt("w"),
                cs.getInt("h"),
                cs.getInt("mh"),
                cs.getInt("sh"),
                cs.getInt("oh"),
                cs.getInt("ox"),
                cs.getInt("oy"),
                cs.contains("bg")?getImagePackage(cs.getConfigurationSection("bg")):null
        );
        return vsl;
    }
    
    public static TextHUDPackage getTextHUDPackage(String id, ConfigurationSection cs){
        return new TextHUDPackage(id, getTextPackage(cs), cs.getInt("time", 0));
    }
    
    public static ImageHUDPackage getImageHUDPackage(String id, ConfigurationSection cs){
        return new ImageHUDPackage(id, getImagePackage(cs), cs.getInt("time", 0));
    }
    
    public static ButtonHUDPackage getButtonHUDPackage(String id, ConfigurationSection cs){
        return new ButtonHUDPackage(id, getButtonPackage(cs), cs.getInt("time", 0), null);
    }
    
    public static FlowPackage getFlowPackage(ConfigurationSection cs){
        return new FlowPackage(MessageUtil.color(cs.getString("text")), cs.getInt("y", 0), cs.getBoolean("bg", true));
    }
    
    public static List<String> getStringList(ConfigurationSection cs, String key){
        List<String> list = cs.getStringList(key);
        if(list.isEmpty()){
            list = Arrays.asList(cs.getString(key));
        }
        list.replaceAll(l -> MessageUtil.color(l));
        return list;
    }
    
}

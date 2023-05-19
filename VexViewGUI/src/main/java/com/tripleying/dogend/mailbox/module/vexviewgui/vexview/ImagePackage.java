package com.tripleying.dogend.mailbox.module.vexviewgui.vexview;

import lk.vexview.gui.components.VexImage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class ImagePackage extends VexPackage {
    
    protected final String img;
    protected final int x;
    protected final int y;
    protected final int w;
    protected final int h;
    
    public ImagePackage(String img, int x, int y, int w, int h){
        this.img = img;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    
    public VexImage getVexImage(int ox, int oy){
        return new VexImage(img, x+ox, y+oy, w, h);
    }
    
    public VexImage getSlotBackground(int ix, int iy){
        return new VexImage(img, ix+((w-18)/2)-1, iy+((h-18)/2)-1, w, h);
    }
    
    public static ConfigurationSection createConfigurationSection() {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("img", "[local]MailBox/gui_select_type.png");
        yml.set("x", 0);
        yml.set("y", 0);
        yml.set("w", 380);
        yml.set("h", 220);
        return yml;
    }
    
}

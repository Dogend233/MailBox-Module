package com.tripleying.dogend.mailbox.module.vexviewgui.vexview;

import lk.vexview.gui.components.VexImage;
import lk.vexview.gui.components.expand.VexGifImage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class GifImagePackage extends ImagePackage {
    
    private final int interval;
    
    public GifImagePackage(String img, int x, int y, int w, int h, int interval) {
        super(img, x, y, w, h);
        this.interval = interval;
    }
    
    @Override
    public VexImage getVexImage(int ox, int oy){
        return new VexGifImage(img, x+ox, y+oy, w, h, interval);
    }
    
    @Override
    public VexImage getSlotBackground(int ix, int iy){
        return new VexGifImage(img, ix+((w-18)/2)-1, iy+((h-18)/2)-1, w, h, interval);
    }
    
    public static ConfigurationSection createConfigurationSection() {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("img", "[local]MailBox/gif.gif");
        yml.set("x", 0);
        yml.set("y", 0);
        yml.set("w", 380);
        yml.set("h", 220);
        yml.set("interval", 1);
        return yml;
    }
    
}

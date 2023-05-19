package com.tripleying.dogend.mailbox.module.vexviewgui.vexview;

import java.util.Arrays;
import java.util.List;
import lk.vexview.gui.components.VexText;
import lk.vexview.gui.components.VexTextArea;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class TextAreaPackage extends VexPackage implements DescriptionTextPackage {
    
    private final int x;
    private final int y;
    private final int w;
    private final int h;
    private final int max;
    private final List<String> place;
    public TextAreaPackage(int x, int y, int w, int h, int max, List<String> place) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.max = max;
        this.place = place;
    }
    
    public VexTextArea getVexTextArea(int id, int ox, int oy){
        VexTextArea vta;
        if(place==null){
            vta = new VexTextArea(x+ox, y+oy, w, h, max, id);
        }else{
            vta = new VexTextArea(x+ox, y+oy, w, h, max, id, place);
        }
        return vta;
    }
    
    public VexTextArea getVexTextArea(int id, int ox, int oy, List<String> place){
        VexTextArea vta = new VexTextArea(x+ox, y+oy, w, h, max, id, place);
        return vta;
    }
    
    public static ConfigurationSection createConfigurationSection() {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("x", 0);
        yml.set("y", 0);
        yml.set("w", 100);
        yml.set("h", 100);
        yml.set("max", 200);
        yml.set("place", Arrays.asList("这是预先设置好的文字"));
        return yml;
    }

    @Override
    public VexText getDescriptionText(List<String> desc) {
        return getDescriptionText(desc, x, y);
    }
    
}

package com.tripleying.dogend.mailbox.module.vexviewgui.vexview;

import java.util.List;
import lk.vexview.gui.components.VexText;
import lk.vexview.gui.components.VexTextField;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class TextFieldPackage extends VexPackage implements DescriptionTextPackage {
    
    private final int x;
    private final int y;
    private final int w;
    private final int h;
    private final int max;
    private final String place;
    
    public TextFieldPackage(int x, int y, int w, int h, int max, String place){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.max = max;
        this.place = place;
    }
    
    public VexTextField getVexTextField(int id, int ox, int oy){
        VexTextField vtf;
        if(place==null){
            vtf = new VexTextField(x+ox, y+oy, w, h, max, id);
        }else{
            vtf = new VexTextField(x+ox, y+oy, w, h, max, id, place);
        }
        return vtf;
    }
    
    public VexTextField getVexTextField(int id, int ox, int oy, String place){
        VexTextField vtf = new VexTextField(x+ox, y+oy, w, h, max, id, place);
        return vtf;
    }
    
    public static ConfigurationSection createConfigurationSection() {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("x", 0);
        yml.set("y", 0);
        yml.set("w", 30);
        yml.set("h", 15);
        yml.set("max", 15);
        yml.set("place", "这是预先设置好的文字");
        return yml;
    }
    
    @Override
    public VexText getDescriptionText(List<String> desc) {
        return getDescriptionText(desc, x, y);
    }
    
}

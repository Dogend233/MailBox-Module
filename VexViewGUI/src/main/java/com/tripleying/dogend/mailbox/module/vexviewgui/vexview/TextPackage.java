package com.tripleying.dogend.mailbox.module.vexviewgui.vexview;

import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import lk.vexview.gui.components.VexText;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class TextPackage extends VexPackage {
    
    private final int x;
    private final int y;
    private final List<String> text;
    
    public TextPackage(int x, int y, List<String> text){
        this.x = x;
        this.y = y;
        this.text = text;
    }
    
    public VexText getVexText(int ox, int oy, UnaryOperator<List<String>> replaceText){
        List<String> rtext = this.cloneList(text);
        if(replaceText!=null){
            rtext = replaceText.apply(rtext);
        }
        return new VexText(x+ox ,y+oy , rtext);
    }
    
    public static  ConfigurationSection createConfigurationSection() {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("x", 0);
        yml.set("y", 0);
        yml.set("text", Arrays.asList("这是一段文字"));
        return yml;
    }
    
}

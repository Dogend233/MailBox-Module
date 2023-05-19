package com.tripleying.dogend.mailbox.module.vexviewgui.vexview;

import java.util.List;
import lk.vexview.gui.components.VexCheckBox;
import lk.vexview.gui.components.VexText;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class CheckBoxPackage extends VexPackage implements DescriptionTextPackage {
    
    private final String img1;
    private final String img2;
    private final int x;
    private final int y;
    private final int w;
    private final int h;

    public CheckBoxPackage(String img1, String img2, int x, int y, int w, int h) {
        this.img1 = img1;
        this.img2 = img2;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    
    public VexCheckBox getVexCheckBox(int id, int ox, int oy, boolean status){
        VexCheckBox vcb = new VexCheckBox(id,img1, img2, x+ox, y+oy, w, h, status);
        return vcb;
    }

    public static ConfigurationSection createConfigurationSection() {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("img1", "[local]MailBox/check_box.png");
        yml.set("img2", "[local]MailBox/check_box_.png");
        yml.set("x", 0);
        yml.set("y", 0);
        yml.set("w", 7);
        yml.set("h", 7);
        return yml;
    }

    @Override
    public VexText getDescriptionText(List<String> desc) {
        return getDescriptionText(desc, x, y);
    }
    
}

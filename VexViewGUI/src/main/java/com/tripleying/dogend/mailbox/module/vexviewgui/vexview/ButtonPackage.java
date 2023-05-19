package com.tripleying.dogend.mailbox.module.vexviewgui.vexview;

import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import lk.vexview.gui.components.ButtonFunction;
import lk.vexview.gui.components.VexButton;
import lk.vexview.gui.components.VexText;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class ButtonPackage extends VexPackage implements DescriptionTextPackage {
    
    private final String text;
    private final String img1;
    private final String img2;
    private final int x;
    private final int y;
    private final int w;
    private final int h;
    private final List<String> hover;
    
    public ButtonPackage(String text, String img1, String img2, int x, int y, int w, int h, List<String> hover){
        this.text = text;
        this.img1 = img1;
        this.img2 = img2;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.hover = hover;
    }
    
    public VexButton getButton(String id, int ox, int oy, ButtonFunction bf, UnaryOperator<String> replaceText, UnaryOperator<List<String>> replaceHover){
        String rtext = text;
        if(replaceText!=null){
            rtext = replaceText.apply(rtext);
        }
        if(bf==null){
            bf = p -> {};
        }
        VexButton vb = new VexButton(id, rtext, img1, img2, x+ox, y+oy, w, h, bf);
        if(hover!=null){
            List<String> rhover = this.cloneList(hover);
            if(replaceHover!=null){
                rhover = replaceHover.apply(rhover);
            }
            vb.setHover(rhover);
        }
        return vb;
    }
    
    public static ConfigurationSection createConfigurationSection() {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("text", "&b按钮");
        yml.set("img1", "[local]MailBox/button_mail_normal.png");
        yml.set("img2", "[local]MailBox/button_mail_normal_.png");
        yml.set("x", 0);
        yml.set("y", 0);
        yml.set("w", 45);
        yml.set("h", 15);
        yml.set("hover", Arrays.asList("&6这是一个按钮"));
        return yml;
    }

    @Override
    public VexText getDescriptionText(List<String> desc) {
        return getDescriptionText(desc, x, y);
    }
    
}

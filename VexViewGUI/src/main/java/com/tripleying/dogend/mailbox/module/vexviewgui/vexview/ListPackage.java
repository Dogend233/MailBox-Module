package com.tripleying.dogend.mailbox.module.vexviewgui.vexview;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import lk.vexview.gui.components.DynamicComponent;
import lk.vexview.gui.components.ScrollingListComponent;
import lk.vexview.gui.components.VexComponents;
import lk.vexview.gui.components.VexScrollingList;
import lk.vexview.gui.components.VexText;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class ListPackage extends VexPackage {
    
    protected final int c;
    protected final int x;
    protected final int y;
    protected final int w;
    protected final int h;
    protected final int mh;
    protected final int sh;
    protected final int oh;
    protected final int ox;
    protected final int oy;
    
    public ListPackage(int c, int x, int y, int w, int h, int mh, int sh, int oh, int ox, int oy){
        this.c = c;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.mh = mh;
        this.sh = sh;
        this.oh = oh;
        this.ox = ox;
        this.oy = oy;
    }
    
    public VexScrollingList getVexScrollingList(int size, BiFunction<Integer, Integer, List<ScrollingListComponent>> addComponents){
        int rh = size/c;
        if(size%c!=0) rh++;
        rh = rh*sh+oh;
        if(rh<mh) rh = mh;
        VexScrollingList vsl = new VexScrollingList(x, y, w, h, rh);
        for(int i=0;i<size;i++){
            addComponents.apply(i%c*ox, i/c*oy).forEach(vc -> vsl.addComponent(vc));
        }
        return vsl;
    }
    
    public VexScrollingList getTextScrollingList(int max, int line, List<String> text){
        int size = 0;
        for(String t : text) {
            size += t.length() / max;
            if(t.length()%max != 0) size++;
        }
        int rh = mh;
        if(size>line){
            rh += (size-line)*sh;
        }
        VexScrollingList vsl = new VexScrollingList(x, y, w, h, rh);
        vsl.addComponent(new VexText(0, 0, text));
        return vsl;
    }
    
    public List<VexComponents> getVexComponentsList(int size, BiFunction<Integer, Integer, List<VexComponents>> addComponents){
        List<VexComponents> list = new ArrayList();
        for(int i=0;i<size;i++){
            list.addAll(addComponents.apply(i%c*ox, i/c*oy));
        }
        return list;
    }
    
    public List<DynamicComponent> getDynamicComponentList(int size, BiFunction<Integer, Integer, List<DynamicComponent>> addComponents){
        List<DynamicComponent> list = new ArrayList();
        for(int i=0;i<size;i++){
            list.addAll(addComponents.apply(i%c*ox, i/c*oy));
        }
        return list;
    }
    
    public static ConfigurationSection createConfigurationSection() {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("c", 3);
        yml.set("x", 0);
        yml.set("y", 0);
        yml.set("w", 200);
        yml.set("h", 200);
        yml.set("mh", 195);
        yml.set("sh", 50);
        yml.set("oh", 5);
        yml.set("ox", 50);
        yml.set("oy", 50);
        return yml;
    }
    
}
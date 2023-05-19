package com.tripleying.dogend.mailbox.module.vexviewgui.vexview;

import java.util.ArrayList;
import java.util.List;
import lk.vexview.gui.components.DynamicComponent;
import lk.vexview.gui.components.VexComponents;
import lk.vexview.gui.components.VexSlot;
import lk.vexview.gui.components.VexText;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class ItemListPackage extends ListPackage implements DescriptionTextPackage {
    
    private final ImagePackage ip;
    
    public ItemListPackage(int c, int x, int y, int w, int h, int mh, int sh, int oh, int ox, int oy, ImagePackage ip) {
        super(c, x, y, w, h, mh, sh, oh, ox, oy);
        this.ip = ip;
    }
    
    public List<VexComponents> getItemComponentList(List<ItemStack> isl, Integer max){
        List<VexComponents> list = new ArrayList();
        int isize = isl.size();
        int size = max==null?isize:max;
        for(int i = 0 ;i<size;i++){
            int ix = i%c*ox+x;
            int iy = i/c*oy+y;
            ItemStack is = i<isize?isl.get(i):null;
            list.add(new VexSlot(i, ix, iy, is));
            if(ip!=null) list.add(ip.getSlotBackground(ix, iy));
        }
        return list;
    }
    
    public List<DynamicComponent> getItemDynamicComponentList(List<ItemStack> isl, Integer max){
        List<DynamicComponent> list = new ArrayList();
        int isize = isl.size();
        int size = max==null?isize:max;
        for(int i = 0 ;i<size;i++){
            int ix = i%c*ox+x;
            int iy = i/c*oy+y;
            ItemStack is = i<isize?isl.get(i):null;
            list.add(new VexSlot(i, ix, iy, is));
            if(ip!=null) list.add(ip.getSlotBackground(ix, iy));
        }
        return list;
    }
    
    public static ConfigurationSection createConfigurationSection() {
        ConfigurationSection cs = ListPackage.createConfigurationSection();
        cs.set("bg.img", "[local]MailBox/image_slot_bg.png");
        cs.set("bg.x", 0);
        cs.set("bg.y", 0);
        cs.set("bg.w", 18);
        cs.set("bg.h", 18);
        return cs;
    }

    @Override
    public VexText getDescriptionText(List<String> desc) {
        return getDescriptionText(desc, x, y);
    }
    
}

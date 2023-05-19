package com.tripleying.dogend.mailbox.module.vexviewgui.vexview;

import java.lang.reflect.Constructor;
import java.util.List;
import lk.vexview.gui.VexInventoryGui;
import lk.vexview.gui.components.VexComponents;

public class InvGuiPackage extends GuiPackage {
    
    private final int ix;
    private final int iy;
    
    public InvGuiPackage(String img, int x, int y, int w, int h, int ix, int iy) {
        super(img, x, y, w, h);
        this.ix = ix;
        this.iy = iy;
    }
    
    public VexInventoryGui getVexInventoryGui(){
        return new VexInventoryGui(img, x, y, w, h, w, h, ix, iy);
    }
    
    public VexInventoryGui getVexInventoryGui(List<VexComponents> vcl){
        VexInventoryGui gui = this.getVexInventoryGui();
        gui.addAllComponents(vcl);
        return gui;
    }
    
    public VexInventoryGui getVexInventoryGui(Class<? extends VexInventoryGui> clazz){
        try{
            Constructor<? extends VexInventoryGui> constructor = clazz.getConstructor(String.class, int.class, int.class, int.class, int.class, int.class, int.class);
            return constructor.newInstance(img, x, y, w, h, ix, iy);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    
}

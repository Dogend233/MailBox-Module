package com.tripleying.dogend.mailbox.module.vexviewgui.vexview;

import java.lang.reflect.Constructor;
import java.util.List;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.VexComponents;

public class GuiPackage extends ImagePackage {
    
    public GuiPackage(String img, int x, int y, int w, int h){
        super(img, x, y, w, h);
    }
    
    public VexGui getVexGui(){
        return new VexGui(img, x, y, w, h);
    }
    
    public VexGui getVexGui(List<VexComponents> vcl){
        VexGui gui = this.getVexGui();
        gui.addAllComponents(vcl);
        return gui;
    }
    
    public VexGui getVexGui(Class<? extends VexGui> clazz){
        try{
            Constructor<? extends VexGui> constructor = clazz.getConstructor(String.class, int.class, int.class, int.class, int.class);
            return constructor.newInstance(img, x, y, w, h);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    
}

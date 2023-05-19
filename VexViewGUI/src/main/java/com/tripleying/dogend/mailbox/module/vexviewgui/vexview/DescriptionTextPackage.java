package com.tripleying.dogend.mailbox.module.vexviewgui.vexview;

import java.util.List;
import lk.vexview.gui.components.VexText;

public interface DescriptionTextPackage {
    
    default VexText getDescriptionText(List<String> desc, int vx, int vy){
        int size = desc.size();
        return new VexText(vx, vy-size*10, desc);
    }
    
    public VexText getDescriptionText(List<String> desc);
    
}

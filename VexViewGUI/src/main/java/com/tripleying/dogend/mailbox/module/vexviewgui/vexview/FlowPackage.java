package com.tripleying.dogend.mailbox.module.vexviewgui.vexview;

import java.util.function.UnaryOperator;
import lk.vexview.api.VexViewAPI;
import org.bukkit.entity.Player;

public class FlowPackage extends VexPackage {
    
    private String text;
    private int y;
    private boolean bg;

    public FlowPackage(String text, int y, boolean bg) {
        this.text = text;
        this.y = y;
        this.bg = bg;
    }
    
    public void sendFlow(Player p, UnaryOperator<String> replaceText){
        if(replaceText==null){
            VexViewAPI.sendFlowView(p, text, y, bg);
        }else{
            VexViewAPI.sendFlowView(p, replaceText.apply(text), y, bg);
        }
    }
    
}

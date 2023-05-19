package com.tripleying.dogend.mailbox.module.vexviewgui.vexview;

import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.components.ButtonFunction;
import lk.vexview.hud.VexButtonShow;
import lk.vexview.hud.VexShow;
import org.bukkit.entity.Player;

public class ButtonHUDPackage extends HUDPackage {
    
    private final ButtonPackage bp;
    private ButtonFunction bf;

    public ButtonHUDPackage(String id, ButtonPackage bp, int time, ButtonFunction bf) {
        super(id, time);
        this.bp = bp;
        this.bf = bf;
    }

    @Override
    public VexShow getVexShow() {
        return new VexButtonShow(id, bp.getButton(id+"HUD", time, time, bf==null?p->{}:bf, null, null), time);
    }
    
    public void sendHud(Player p, ButtonFunction bf){
        VexViewAPI.sendHUD(p, new VexButtonShow(id, bp.getButton(id+p.getName()+"HUD", time, time, bf, null, null), time));
    }
    
    public ButtonHUDPackage setButtonFunction(ButtonFunction bf){
        this.bf = bf;
        return this;
    }
    
}
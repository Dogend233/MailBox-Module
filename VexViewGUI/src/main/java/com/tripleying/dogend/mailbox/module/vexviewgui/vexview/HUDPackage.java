package com.tripleying.dogend.mailbox.module.vexviewgui.vexview;

import lk.vexview.api.VexViewAPI;
import lk.vexview.hud.VexShow;
import org.bukkit.entity.Player;

public abstract class HUDPackage extends VexPackage {
    
    protected String id;
    protected int time;

    public HUDPackage(String id, int time) {
        this.id = id;
        this.time = time;
    }
    
    public abstract VexShow getVexShow();

    public abstract VexShow getScaleVexShow(Player p);

    public void sendHud(Player p){
        VexViewAPI.sendHUD(p, getVexShow());
    }

    public void sendScaleHud(Player p) {
        VexViewAPI.sendHUD(p, getScaleVexShow(p));
    }
    
    public void removeHud(Player p){
        VexViewAPI.removeHUD(p, id);
    }
    
}

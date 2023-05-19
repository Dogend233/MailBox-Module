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
    
    public void sendHud(Player p){
        VexViewAPI.sendHUD(p, getVexShow());
    }
    
    public void removeHud(Player p){
        VexViewAPI.removeHUD(p, id);
    }
    
}

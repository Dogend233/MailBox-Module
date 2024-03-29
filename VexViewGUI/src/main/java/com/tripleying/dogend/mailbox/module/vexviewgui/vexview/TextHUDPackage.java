package com.tripleying.dogend.mailbox.module.vexviewgui.vexview;

import lk.vexview.hud.VexImageShow;
import lk.vexview.hud.VexShow;
import lk.vexview.hud.VexTextShow;
import org.bukkit.entity.Player;

public class TextHUDPackage extends HUDPackage {
    
    private final TextPackage tp;

    public TextHUDPackage(String id, TextPackage tp, int time) {
        super(id, time);
        this.tp = tp;
    }

    @Override
    public VexShow getVexShow() {
        return new VexTextShow(id, tp.getVexText(0, 0, null), time);
    }

    @Override
    public VexImageShow getScaleVexShow(Player p) {
        return null;
    }
}

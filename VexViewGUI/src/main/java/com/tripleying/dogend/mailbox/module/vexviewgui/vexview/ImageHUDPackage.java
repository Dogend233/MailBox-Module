package com.tripleying.dogend.mailbox.module.vexviewgui.vexview;

import lk.vexview.hud.VexImageShow;
import lk.vexview.hud.VexShow;

public class ImageHUDPackage extends HUDPackage {
    
    private final ImagePackage ip;

    public ImageHUDPackage(String id, ImagePackage ip, int time) {
        super(id, time);
        this.ip = ip;
    }

    @Override
    public VexShow getVexShow() {
        return new VexImageShow(id, ip.getVexImage(0, 0), time);
    }
    
}

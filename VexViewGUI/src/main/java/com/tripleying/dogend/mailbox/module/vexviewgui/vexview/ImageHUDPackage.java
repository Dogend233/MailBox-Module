package com.tripleying.dogend.mailbox.module.vexviewgui.vexview;

import lk.vexview.gui.components.VexImage;
import lk.vexview.hud.VexImageShow;
import lk.vexview.hud.VexShow;
import org.bukkit.entity.Player;

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

    @Override
    public VexShow getScaleVexShow(Player p) {
        VexImage vexImage = ip.getVexImage(0, 0);
        double[] scale = VexViewUtil.getPlayerClientScale(p);
        vexImage.setX((int) (vexImage.getX() * scale[0]));
        vexImage.setY((int) (vexImage.getY() * scale[1]));
        vexImage.setW((int) (vexImage.getW() * scale[0]));
        vexImage.setH((int) (vexImage.getH() * scale[1]));
        return new VexImageShow(id, vexImage, time);
    }

}

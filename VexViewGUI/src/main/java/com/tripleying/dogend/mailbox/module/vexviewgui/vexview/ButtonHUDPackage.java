package com.tripleying.dogend.mailbox.module.vexviewgui.vexview;

import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.components.ButtonFunction;
import lk.vexview.gui.components.VexButton;
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

    public VexShow getScaleVexShow(Player player) {
        VexButton button = bp.getButton(id + "HUD", time, time, bf == null ? p -> {} : bf, null, null);
        double[] scale = VexViewUtil.getPlayerClientScale(player);
        button.setX((int) (button.getX() * scale[0]));
        button.setY((int) (button.getY() * scale[1]));
        button.setW((int) (button.getW() * scale[0]));
        button.setH((int) (button.getH() * scale[1]));
        /* 位置打印
        MessageUtil.log(player,"玩家" + player.getName() + "X" + button.getX() + "Y" + button.getY() +
                "W" + button.getW() + "H" + button.getH());
        */
        return new VexButtonShow(id, button, time);
    }

    public void sendHud(Player p, ButtonFunction bf){
        VexViewAPI.sendHUD(p, new VexButtonShow(id, bp.getButton(id+p.getName()+"HUD", time, time, bf, null, null), time));
    }
    
    public ButtonHUDPackage setButtonFunction(ButtonFunction bf){
        this.bf = bf;
        return this;
    }


    
}
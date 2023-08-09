package com.tripleying.dogend.mailbox.module.vexviewgui;

import com.tripleying.dogend.mailbox.MailBox;
import com.tripleying.dogend.mailbox.api.event.mail.MailBoxPersonMailReceiveEvent;
import com.tripleying.dogend.mailbox.api.event.mail.MailBoxPersonMailSendEvent;
import com.tripleying.dogend.mailbox.api.event.mail.MailBoxSystemMailSendEvent;
import com.tripleying.dogend.mailbox.api.mail.PersonMail;
import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.manager.DataManager;
import com.tripleying.dogend.mailbox.manager.MailManager;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.FlowPackage;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.HUDPackage;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.VexViewUtil;
import java.util.List;
import lk.vexview.announce.AnnounceType;
import lk.vexview.api.VexViewAPI;
import lk.vexview.event.PlayerClientWindowSizeEvent;
import lk.vexview.gui.components.ButtonFunction;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class VexViewGUIListener implements Listener {

    private final String announce_tip;
    private final FlowPackage flow_tip;
    private final HUDPackage hud_mail;
    private final HUDPackage hud_tip_hold;
    private final HUDPackage hud_tip_flash;
    private final boolean hud_scale;

    public VexViewGUIListener(YamlConfiguration yml) {
        ButtonFunction bf = player -> player.performCommand("mailbox vvgui list");
        flow_tip = yml.getBoolean("tips.flow.enable", false) ? VexViewUtil.getFlowPackage(yml.getConfigurationSection("tips.flow")) : null;
        announce_tip = yml.getBoolean("tips.announce.enable", false) ? yml.getString("tips.announce.text", "") : null;
        hud_mail = yml.getBoolean("hud.enable", false) ?
                (yml.getString("hud.img", "").endsWith(".gif") ?
                        VexViewUtil.getImageHUDPackage("MailBoxMailHud", yml.getConfigurationSection("hud")) :
                        VexViewUtil.getButtonHUDPackage("MailBoxMailHud", yml.getConfigurationSection("hud")).setButtonFunction(bf)) :
                null;
        hud_tip_hold = yml.getBoolean("tips.hud.hold.enable", false) ?
                (yml.getString("tips.hud.hold.img", "").endsWith(".gif") ?
                        VexViewUtil.getImageHUDPackage("MailBoxTipsHudHold", yml.getConfigurationSection("tips.hud.hold")) :
                        VexViewUtil.getButtonHUDPackage("MailBoxTipsHudHold", yml.getConfigurationSection("tips.hud.hold")).setButtonFunction(bf)) :
                null;
        hud_tip_flash = yml.getBoolean("tips.hud.flash.enable", false) ?
                (yml.getString("tips.hud.flash.img", "").endsWith(".gif") ?
                        VexViewUtil.getImageHUDPackage("MailBoxTipsHudFlash", yml.getConfigurationSection("tips.hud.flash")) :
                        VexViewUtil.getButtonHUDPackage("MailBoxTipsHudFlash", yml.getConfigurationSection("tips.hud.flash")).setButtonFunction(bf)) :
                null;
        hud_scale = yml.getBoolean("scale");
        for (Player player:Bukkit.getOnlinePlayers()) checkAndSendTips(player, false);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerLoginEvent evt) {
        Player p = evt.getPlayer();
        try {
            checkAndSendTips(p, true);
        } catch (Exception e) {}
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent evt) {
        removeAllHUD(evt.getPlayer());
    }

    @EventHandler
    public void onSystemMailSend(MailBoxSystemMailSendEvent evt) {
        SystemMail sm = evt.getSystemMail();
        if (sm.needCheckPlayerData()) {
            DataManager dm = DataManager.getDataManager();
            Bukkit.getOnlinePlayers().forEach(p -> sm.checkPlayerData(dm.getPlayerData(p)));
        }
    }

    @EventHandler
    public void onPersonMailSend(MailBoxPersonMailSendEvent evt) {
        PersonMail pm = evt.getPersonMail();
        Player p = pm.getReceiver();
        if (hud_tip_hold != null) {
            if (hud_scale) hud_tip_hold.sendScaleHud(p);
            else hud_tip_hold.sendHud(p);
        }
        if (hud_tip_flash != null) {
            if (hud_scale) hud_tip_flash.sendScaleHud(p);
            else hud_tip_flash.sendHud(p);
        }
        if (flow_tip != null) flow_tip.sendFlow(p, null);
        if (announce_tip != null) VexViewAPI.sendAnnounceMessage(p, announce_tip, AnnounceType.NORMAL);
    }

    @EventHandler
    public void onPersonMailReceived(MailBoxPersonMailReceiveEvent evt) {
        PersonMail pm = evt.getPersonMail();
        Player p = pm.getReceiver();
        checkAndSendTips(p, false);
    }

    public void removeAllHUD(Player p) {
        if (hud_mail != null) hud_mail.removeHud(p);
        if (hud_tip_hold != null) hud_tip_hold.removeHud(p);
        if (hud_tip_flash != null) hud_tip_flash.removeHud(p);
    }

    public void unload() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            removeAllHUD(p);
        });
    }

    public final void checkAndSendTips(Player p, boolean flash) {
        Bukkit.getScheduler().runTask(MailBox.getMailBox(), () -> {
            List<PersonMail> pml = MailManager.getMailManager().getPersonMailList(p);
            if (!pml.isEmpty()) {
                for (PersonMail pm : pml) {
                    if (!pm.isReceived()) {

                        if (hud_tip_hold != null) {
                            if (hud_scale) hud_tip_hold.sendScaleHud(p);
                            else hud_tip_hold.sendHud(p);
                        }
                        if (flash) {
                            if (hud_tip_flash != null) {
                                if (hud_scale) hud_tip_flash.sendScaleHud(p);
                                else hud_tip_flash.sendHud(p);
                            }
                            if (flow_tip != null) flow_tip.sendFlow(p, null);
                        }
                        if (announce_tip != null)
                            VexViewAPI.sendAnnounceMessage(p, announce_tip, AnnounceType.NORMAL);
                        if (hud_mail != null) {
                            if (hud_scale) hud_mail.sendScaleHud(p);
                            else hud_mail.sendHud(p);
                        }
                        return;

                    }
                }
            }
            if (hud_tip_hold != null) hud_tip_hold.removeHud(p);
            if (hud_tip_flash != null) hud_tip_flash.removeHud(p);
            if (hud_mail != null) {
                if (hud_scale) hud_mail.sendScaleHud(p);
                else hud_mail.sendHud(p);
            }
        });
    }

    @EventHandler
    public void onPlayerClientChange(PlayerClientWindowSizeEvent e) {
        checkAndSendTips(e.getPlayer(), false);
    }

}
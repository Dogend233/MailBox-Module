package com.tripleying.dogend.mailbox.module.mailtips;

import com.tripleying.dogend.mailbox.MailBox;
import com.tripleying.dogend.mailbox.api.event.mail.MailBoxPersonMailSendEvent;
import com.tripleying.dogend.mailbox.api.event.mail.MailBoxSystemMailSendEvent;
import com.tripleying.dogend.mailbox.api.mail.PersonMail;
import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.api.module.MailBoxModule;
import com.tripleying.dogend.mailbox.manager.DataManager;
import com.tripleying.dogend.mailbox.module.mailtips.tips.*;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class MailTips extends MailBoxModule implements Listener {
    
    private List<Tips> types;
    
    @Override
    public void onEnable() {
        types = new ArrayList();
        this.saveDefaultConfig();
        this.reloadConfig();
        YamlConfiguration config = this.getConfig();
        config.getStringList("types").forEach(type -> {
            switch(type){
                case "msg":
                    types.add(new MessageTips(config));
                    System.out.println(MessageUtil.color("&6[MailTips]: &a注册聊天框提示"));
                    break;
                case "title":
                    if(MailBox.getMCVersion()>1.07){
                        types.add(new TitleTips(config.getConfigurationSection("title")));
                        System.out.println(MessageUtil.color("&6[MailTips]: &a注册标题提示"));
                    }
                    break;
                case "sound":
                    types.add(new SoundTips(config));
                    System.out.println(MessageUtil.color("&6[MailTips]: &a注册声音提示"));
                    break;
            }
        });
        this.registerListener(this);
    }
    
    @Override
    public void onDisable() {
        this.unregisterAllListener();
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent evt){
        Player p = evt.getPlayer();
        new BukkitRunnable(){
            @Override
            public void run() {
                if(DataManager.getDataManager().getNotReceivedPersonMailCount(p)>0){
                    types.forEach(type -> type.sendTips(p));
                }
            }
        }.runTaskAsynchronously(MailBox.getMailBox());
    }
    
    @EventHandler
    public void onPersonMailSend(MailBoxPersonMailSendEvent evt){
        PersonMail pm = evt.getPersonMail();
        types.forEach(type -> type.sendTips(pm));
    }
    
    @EventHandler
    public void onSystemMailSend(MailBoxSystemMailSendEvent evt){
        new BukkitRunnable(){
            @Override
            public void run() {
                SystemMail sm = evt.getSystemMail();
                if(sm.needCheckPlayerData()){
                    DataManager dm = DataManager.getDataManager();
                    Bukkit.getOnlinePlayers().forEach(p -> sm.checkPlayerData(dm.getPlayerData(p)));
                }
            }
        }.runTaskAsynchronously(MailBox.getMailBox());
    }
    
}

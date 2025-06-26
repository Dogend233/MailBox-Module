package com.tripleying.dogend.module.singleplayermailapi;

import com.github.yitter.idgen.YitIdHelper;
import com.tripleying.dogend.mailbox.api.event.mail.MailBoxPersonMailPreSendEvent;
import com.tripleying.dogend.mailbox.api.event.mail.MailBoxPersonMailSendEvent;
import com.tripleying.dogend.mailbox.api.mail.PersonMail;
import com.tripleying.dogend.mailbox.manager.MailManager;
import com.tripleying.dogend.mailbox.util.TimeUtil;
import com.tripleying.dogend.module.singleplayermailapi.util.PersonMailData;
import com.tripleying.dogend.module.singleplayermailapi.util.UUIDData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SinglePlayerMailAPI {

    public static List<String> send2Player(PersonMail pm, String... names){
        if(pm.getId()==0){
            pm.setId(YitIdHelper.nextId());
        }
        pm.setSendtime(TimeUtil.currentTimeString());
        List<String> successNames = new ArrayList<>();
        for(String name:names){
            Player p = Bukkit.getPlayer(name);
            if(p!=null && p.isOnline()){
                if(send2OnlinePlayer(pm, p)){
                    successNames.add(name);
                }
            }else{
                if(send2OfflinePlayerByName(pm, name)){
                    successNames.add(name);
                }
            }
        }
        return successNames;
    }

    public static List<Player> send2Player(PersonMail pm, Player... ps){
        if(pm.getId()==0){
            pm.setId(YitIdHelper.nextId());
        }
        pm.setSendtime(TimeUtil.currentTimeString());
        List<Player> successNames = new ArrayList<>();
        for(Player p:ps){
            if(send2OnlinePlayer(pm, p)){
                successNames.add(p);
            }
        }
        return successNames;
    }

    public static boolean send2OnlinePlayer(PersonMail pm, Player p){
        pm.setReceiver(p);
        MailBoxPersonMailPreSendEvent evt = new MailBoxPersonMailPreSendEvent(pm);
        Bukkit.getPluginManager().callEvent(evt);
        if(!evt.isCancelled() && MailManager.getMailManager().sendPersonMail(pm, p)){
            Bukkit.getPluginManager().callEvent(new MailBoxPersonMailSendEvent(pm));
            return true;
        }else{
            return false;
        }
    }

    public static boolean send2OfflinePlayerByName(PersonMail pm, String name){
        String uuid = UUIDData.getUUIDFromName(name);
        if(uuid==null){
            return false;
        }else{
            return send2OfflinePlayerByUUID(pm, uuid);
        }
    }

    public static boolean send2OfflinePlayerByUUID(PersonMail pm, String uuid){
        PersonMailData pmd = new PersonMailData();
        YamlConfiguration yml = pm.toYamlConfiguration();
        yml.set("uuid", uuid);
        yml.set("received", false);
        pm = new PersonMail(yml);
        MailBoxPersonMailPreSendEvent evt = new MailBoxPersonMailPreSendEvent(pm);
        Bukkit.getPluginManager().callEvent(evt);
        if(!evt.isCancelled()){
            yml = pm.toYamlConfiguration();
            pmd = (PersonMailData) pmd.loadFromYamlConfiguration(yml);
            if(pmd.insertCustomData()){
                Bukkit.getPluginManager().callEvent(new MailBoxPersonMailSendEvent(pm));
                return true;
            }
        }
        return false;
    }

}
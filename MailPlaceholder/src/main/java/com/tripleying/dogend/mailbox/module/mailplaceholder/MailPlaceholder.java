package com.tripleying.dogend.mailbox.module.mailplaceholder;

import com.tripleying.dogend.mailbox.api.module.MailBoxModule;
import com.tripleying.dogend.mailbox.api.util.CommonConfig;
import com.tripleying.dogend.mailbox.manager.DataManager;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;

public class MailPlaceholder extends MailBoxModule {
    
    @Override
    public void onEnable(){
        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            if(PlaceholderAPI.isRegistered("mailbox")) PlaceholderAPI.unregisterPlaceholderHook("mailbox");
            MailExpansion me = new MailExpansion(this);
            if(me.register()){
                MessageUtil.log("已注册MailBox变量");
                me.registerPlayerPlaceholder("player_all", p -> 
                    Long.toString(DataManager.getDataManager().getPersonMailCount(p))
                );
                me.registerPlayerPlaceholder("player_not_received", p -> 
                    Long.toString(DataManager.getDataManager().getNotReceivedPersonMailCount(p))
                );
                me.registerServerPlaceholder("server_expire_person", () -> 
                    Integer.toString(CommonConfig.expire_day)
                );
                return;
            }
        }
        MessageUtil.log("注册MailBox变量失败");
    }
    
    @Override
    public void onDisable(){
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI") && PlaceholderAPI.isRegistered("mailbox")){
            PlaceholderAPI.unregisterPlaceholderHook("mailbox");
            MessageUtil.error("已注销MailBox变量");
        }
    }
    
}

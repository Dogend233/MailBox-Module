package com.tripleying.dogend.module.playermail;

import com.tripleying.dogend.mailbox.api.module.MailBoxModule;
import com.tripleying.dogend.mailbox.manager.ModuleManager;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import com.tripleying.dogend.module.playermail.gui.AdminMailConfig;
import com.tripleying.dogend.module.playermail.mail.AdminSystemMail;
import com.tripleying.dogend.module.singleplayermailapi.SinglePlayerMailAPI;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerMail extends MailBoxModule {

    public static String player_type;
    public static String player_display;
    public static String admin_type;
    public static String admin_display;
    public static boolean enable;
    public static PlayerOption po;
    
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.reloadConfig();
        YamlConfiguration config = this.getConfig();
        player_type = config.getString("player.type", "player");
        player_display = MessageUtil.color(config.getString("player.display", "&6玩家邮件"));
        enable = config.getBoolean("player.enable", false);
        admin_type = config.getString("admin.type", "admin");
        admin_display = MessageUtil.color(config.getString("admin.display", "&6管理员邮件"));
        po = new PlayerOption(config);
//        if(enable) this.registerSystemMail(new PlayerSystemMail());
        this.registerSystemMail(new AdminSystemMail());
        if(ModuleManager.getModuleManager().hasModule("CommonGUIAPI")){
            //if(enable) PlayerSystemMail.registerConfig();
            AdminMailConfig.registerConfig();
        }
        if(ModuleManager.getModuleManager().hasModule("SinglePlayerMailAPI")){
//            MailBoxModule mm = ModuleManager.getModuleManager().getMailBoxModule("SinglePlayerMailAPI");
//            System.out.println(mm.getClass());
            System.out.println(SinglePlayerMailAPI.class);
            System.out.println(SinglePlayerMailAPI.class.getClassLoader());
        }
    }

    @Override
    public void onDisable() {
        this.unregisterAllSystemMail();
    }
    
    public static PlayerOption getOption(){
        return po;
    }
    
}

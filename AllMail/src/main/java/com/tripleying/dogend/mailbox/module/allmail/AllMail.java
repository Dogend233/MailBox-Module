package com.tripleying.dogend.mailbox.module.allmail;

import com.tripleying.dogend.mailbox.module.allmail.mail.AllSystemMail;
import com.tripleying.dogend.mailbox.api.module.MailBoxModule;
import com.tripleying.dogend.mailbox.manager.ModuleManager;
import com.tripleying.dogend.mailbox.module.allmail.gui.AllMailConfig;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import org.bukkit.configuration.file.YamlConfiguration;

public class AllMail extends MailBoxModule {
    
    public static String type;
    public static String display;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.reloadConfig();
        YamlConfiguration config = this.getConfig();
        type = config.getString("type", "all");
        display = MessageUtil.color(config.getString("display", "&6全体邮件"));
        this.registerSystemMail(new AllSystemMail());
        if(ModuleManager.getModuleManager().hasModule("CommonGUIAPI")){
            AllMailConfig.registerConfig();
        }
    }

    @Override
    public void onDisable() {
        this.unregisterAllSystemMail();
    }
    
}

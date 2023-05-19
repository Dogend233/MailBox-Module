package com.tripleying.dogend.mailbox.module.datemail;

import com.tripleying.dogend.mailbox.api.module.MailBoxModule;
import com.tripleying.dogend.mailbox.manager.ModuleManager;
import com.tripleying.dogend.mailbox.module.datemail.gui.DateMailConfig;
import com.tripleying.dogend.mailbox.module.datemail.mail.DateSystemMail;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import org.bukkit.configuration.file.YamlConfiguration;

public class DateMail extends MailBoxModule {
    
    public static String type;
    public static String display;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.reloadConfig();
        YamlConfiguration config = this.getConfig();
        type = config.getString("type", "date");
        display = MessageUtil.color(config.getString("display", "&6期限邮件"));
        this.registerSystemMail(new DateSystemMail());
        if(ModuleManager.getModuleManager().hasModule("CommonGUIAPI")){
            DateMailConfig.registerConfig();
        }
    }

    @Override
    public void onDisable() {
        this.unregisterAllSystemMail();
    }
    
}
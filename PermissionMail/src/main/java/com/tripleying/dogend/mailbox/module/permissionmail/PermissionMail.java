package com.tripleying.dogend.mailbox.module.permissionmail;

import com.tripleying.dogend.mailbox.api.module.MailBoxModule;
import com.tripleying.dogend.mailbox.manager.ModuleManager;
import com.tripleying.dogend.mailbox.module.permissionmail.gui.PermissionMailConfig;
import com.tripleying.dogend.mailbox.module.permissionmail.mail.PermissionSystemMail;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import org.bukkit.configuration.file.YamlConfiguration;

public class PermissionMail extends MailBoxModule {
    
    public static String type;
    public static String display;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.reloadConfig();
        YamlConfiguration config = this.getConfig();
        type = config.getString("type", "permission");
        display = MessageUtil.color(config.getString("display", "&6权限邮件"));
        this.registerSystemMail(new PermissionSystemMail());
        if(ModuleManager.getModuleManager().hasModule("CommonGUIAPI")){
            PermissionMailConfig.registerConfig();
        }
    }

    @Override
    public void onDisable() {
        this.unregisterAllSystemMail();
    }
    
}

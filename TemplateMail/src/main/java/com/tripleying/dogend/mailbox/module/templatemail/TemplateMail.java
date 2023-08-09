package com.tripleying.dogend.mailbox.module.templatemail;

import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.api.module.MailBoxModule;
import com.tripleying.dogend.mailbox.manager.MailManager;
import com.tripleying.dogend.mailbox.manager.ModuleManager;
import com.tripleying.dogend.mailbox.module.templatemail.gui.TemplateMailConfig;
import com.tripleying.dogend.mailbox.module.templatemail.mail.TemplateSystemMail;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import org.bukkit.configuration.file.YamlConfiguration;

public class TemplateMail extends MailBoxModule {
    
    public static String type;
    public static String display;
    
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.reloadConfig();
        YamlConfiguration config = this.getConfig();
        type = config.getString("type", "template");
        display = MessageUtil.color(config.getString("display", "&6模板邮件"));
        this.registerSystemMail(new TemplateSystemMail());
        if(ModuleManager.getModuleManager().hasModule("CommonGUIAPI")){
            TemplateMailConfig.registerConfig();
        }
    }
    
    public TemplateSystemMail getTemplateMail(long id){
        SystemMail sm = MailManager.getMailManager().getSystemMailById(type, id);
        if(sm instanceof TemplateSystemMail){
            return (TemplateSystemMail)sm;
        }else{
            return null;
        }
    }

    @Override
    public void onDisable() {
        this.unregisterAllSystemMail();
    }
    
}

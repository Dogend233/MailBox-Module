package com.tripleying.dogend.mailbox.module.templatemail.mail;

import com.tripleying.dogend.mailbox.api.mail.PersonMail;
import com.tripleying.dogend.mailbox.api.mail.PlayerData;
import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.module.templatemail.TemplateMail;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class TemplateSystemMail extends SystemMail {

    public TemplateSystemMail() {
        super(TemplateMail.type, TemplateMail.display);
    }
    
    public TemplateSystemMail(YamlConfiguration yml) {
        super(yml);
    }
    
    public PersonMail getPersonMail(String type){
        PersonMail pm = new PersonMail(this);
        pm.setType(type);
        return pm;
    }

    @Override
    public SystemMail createSystemMail() {
        return new TemplateSystemMail();
    }

    @Override
    public SystemMail loadSystemMail(YamlConfiguration yml) {
        return new TemplateSystemMail(yml);
    }

    @Override
    public boolean needCheckPlayerData() {
        return false;
    }

    @Override
    public void checkPlayerData(PlayerData pd) {
        
    }

    @Override
    public boolean couldSend2Player(Player p) {
        return false;
    }
    
    @Override
    public boolean send2Player(Player p){
        return false;
    }

    @Override
    public boolean autoCreateDatabaseTable() {
        return true;
    }

    @Override
    public boolean couldSendMail(CommandSender sender) {
        return sender.isOp();
    }

    @Override
    public boolean isExpire() {
        return false;
    }
    
}

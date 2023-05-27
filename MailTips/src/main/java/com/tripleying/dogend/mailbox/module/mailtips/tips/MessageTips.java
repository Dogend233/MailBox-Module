package com.tripleying.dogend.mailbox.module.mailtips.tips;

import com.tripleying.dogend.mailbox.api.mail.PersonMail;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class MessageTips implements Tips {
    
    private final String message;
    
    public MessageTips(ConfigurationSection yml){
        this.message = MessageUtil.color(yml.getString("message", "&6你有一封新邮件, 请打开邮箱查看"));
    }
    
    @Override
    public void sendTips(PersonMail pm){
        sendTips(pm.getReceiver());
    }
    
    @Override
    public void sendTips(Player p) {
        p.sendMessage(message);
    }
    
}
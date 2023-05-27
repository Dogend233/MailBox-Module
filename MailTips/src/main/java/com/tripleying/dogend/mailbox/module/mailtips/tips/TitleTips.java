package com.tripleying.dogend.mailbox.module.mailtips.tips;

import com.tripleying.dogend.mailbox.MailBox;
import com.tripleying.dogend.mailbox.api.mail.PersonMail;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class TitleTips implements Tips  {
    
    private final String main;
    private final String sub;
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;
    
    public TitleTips(ConfigurationSection yml){
        this.main = MessageUtil.color(yml.getString("main", "&6你有一封新邮件"));
        this.sub = MessageUtil.color(yml.getString("sub", "&b请打开邮箱查看"));
        this.fadeIn = yml.getInt("fadeIn", 10);
        this.stay = yml.getInt("stay", 70);
        this.fadeOut = yml.getInt("fadeOut", 20);
    }
    
    @Override
    public void sendTips(PersonMail pm){
        sendTips(pm.getReceiver());
    }
    
    @Override
    public void sendTips(Player p) {
        if(MailBox.getMCVersion()<1.11){
            p.sendTitle(main, sub);
        }else{
            p.sendTitle(main, sub, fadeIn, stay, fadeOut);
        }
    }
    
}

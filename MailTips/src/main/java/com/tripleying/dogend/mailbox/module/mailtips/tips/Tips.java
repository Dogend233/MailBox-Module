package com.tripleying.dogend.mailbox.module.mailtips.tips;

import com.tripleying.dogend.mailbox.api.mail.PersonMail;
import org.bukkit.entity.Player;

public interface Tips {
    
    public void sendTips(PersonMail pm);
    public void sendTips(Player p);
    
}

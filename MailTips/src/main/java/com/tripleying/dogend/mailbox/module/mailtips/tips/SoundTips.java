package com.tripleying.dogend.mailbox.module.mailtips.tips;

import com.tripleying.dogend.mailbox.api.mail.PersonMail;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class SoundTips implements Tips {
    
    private final String sound;
    
    public SoundTips(ConfigurationSection yml){
        this.sound = yml.getString("sound", "minecraft:entity.player.levelup");
    }
    
    @Override
    public void sendTips(PersonMail pm){
        sendTips(pm.getReceiver());
    }

    @Override
    public void sendTips(Player p) {
        p.playSound(p.getLocation(), sound, 1, 1);
    }
    
}

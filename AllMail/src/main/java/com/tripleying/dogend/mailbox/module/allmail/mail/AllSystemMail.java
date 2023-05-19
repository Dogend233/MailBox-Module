package com.tripleying.dogend.mailbox.module.allmail.mail;

import com.tripleying.dogend.mailbox.api.mail.PlayerData;
import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.module.allmail.AllMail;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class AllSystemMail extends SystemMail {

    public AllSystemMail() {
        super(AllMail.type, AllMail.display);
    }
    
    public AllSystemMail(YamlConfiguration yml) {
        super(yml);
    }

    @Override
    public SystemMail createSystemMail() {
        return new AllSystemMail();
    }

    @Override
    public SystemMail loadSystemMail(YamlConfiguration yml) {
        return new AllSystemMail(yml);
    }

    @Override
    public boolean needCheckPlayerData() {
        return true;
    }

    @Override
    public void checkPlayerData(PlayerData pd) {
        this.checkPlayerDataByMaxId(pd);
    }

    @Override
    public boolean couldSend2Player(Player p) {
        if(this.isExpire()){
            this.deleteMail();
            return false;
        }else{
            return true;
        }
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

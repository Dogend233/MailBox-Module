package com.tripleying.dogend.mailbox.module.cdkeymail.mail;

import com.tripleying.dogend.mailbox.api.data.Data;
import com.tripleying.dogend.mailbox.api.data.DataType;
import com.tripleying.dogend.mailbox.api.mail.PlayerData;
import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.manager.DataManager;
import com.tripleying.dogend.mailbox.module.cdkeymail.CDKeyMail;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CDKeySystemMail extends SystemMail {
    
    @Data(type = DataType.Boolean)
    private boolean repeat;

    public CDKeySystemMail() {
        super(CDKeyMail.type, CDKeyMail.display);
        repeat = false;
    }
    
    public CDKeySystemMail(YamlConfiguration yml) {
        super(yml);
        repeat = yml.getBoolean("repeat");
    }

    @Override
    public SystemMail createSystemMail() {
        return new CDKeySystemMail();
    }

    @Override
    public SystemMail loadSystemMail(YamlConfiguration yml) {
        return new CDKeySystemMail(yml);
    }

    @Override
    public boolean needCheckPlayerData() {
        return false;
    }

    @Override
    public void checkPlayerData(PlayerData pd) {
        return;
    }

    @Override
    public boolean couldSend2Player(Player p) {
        if(this.isExpire()){
            this.deleteMail();
            return false;
        }
        Object d = DataManager.getDataManager().getPlayerData(p).getData(this.type);
        List<String> now  = d==null?new ArrayList():(List<String>)d;
        if(!now.contains(Long.toString(id))){
            return true;
        }
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
    
    public CDKeySystemMail setRepeat(boolean repeat){
        this.repeat = repeat;
        return this;
    }

    public boolean isRepeat() {
        return this.repeat;
    }

    @Override
    public boolean isExpire() {
        return false;
    }
    
    @Override
    public YamlConfiguration toYamlConfiguration(){
        YamlConfiguration yml = super.toYamlConfiguration();
        yml.set("repeat", this.repeat);
        return super.toYamlConfiguration();
    }
    
}
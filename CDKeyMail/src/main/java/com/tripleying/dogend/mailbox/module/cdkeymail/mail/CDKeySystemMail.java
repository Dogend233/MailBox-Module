package com.tripleying.dogend.mailbox.module.cdkeymail.mail;

import com.tripleying.dogend.mailbox.api.data.Data;
import com.tripleying.dogend.mailbox.api.data.DataType;
import com.tripleying.dogend.mailbox.api.event.mail.MailBoxPersonMailPreSendEvent;
import com.tripleying.dogend.mailbox.api.event.mail.MailBoxPersonMailSendEvent;
import com.tripleying.dogend.mailbox.api.mail.PersonMail;
import com.tripleying.dogend.mailbox.api.mail.PlayerData;
import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.manager.MailManager;
import com.tripleying.dogend.mailbox.module.cdkeymail.CDKeyMail;
import com.tripleying.dogend.module.singleplayermailapi.SinglePlayerMailAPI;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CDKeySystemMail extends SystemMail {
    
    /**
     * 一个人是否可以重复领取多个此ID邮件
     */
    @Data(type = DataType.Boolean)
    private boolean repeat;

    public CDKeySystemMail() {
        super(CDKeyMail.getMain().getType(), CDKeyMail.getMain().getDisplay());
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
    }

    @Override
    public boolean couldSend2Player(Player p) {
        return false;
    }
    
    @Override
    public boolean send2Player(Player p){
        PersonMail pm = new PersonMail(this);
        pm.setId(0);
        List<Player> pl = SinglePlayerMailAPI.send2Player(pm, p);
        return !pl.isEmpty() && pl.get(0)==p;
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
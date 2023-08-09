package com.tripleying.dogend.module.playermail.mail;

import com.tripleying.dogend.mailbox.api.data.Data;
import com.tripleying.dogend.mailbox.api.data.DataType;
import com.tripleying.dogend.mailbox.api.event.mail.MailBoxPersonMailPreSendEvent;
import com.tripleying.dogend.mailbox.api.event.mail.MailBoxPersonMailSendEvent;
import com.tripleying.dogend.mailbox.api.mail.PersonMail;
import com.tripleying.dogend.mailbox.api.mail.PlayerData;
import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.manager.MailManager;
import com.tripleying.dogend.module.playermail.PlayerMail;
import com.tripleying.dogend.module.singleplayermailapi.SinglePlayerMailAPI;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class AdminSystemMail extends SystemMail {
    
    @Data(type = DataType.YamlString)
    private List<String> players;

    public AdminSystemMail() {
        super(PlayerMail.admin_type, PlayerMail.admin_display);
        players = new ArrayList();
    }
    
    public AdminSystemMail(YamlConfiguration yml) {
        super(yml);
        players = yml.getStringList("players");
    }
    
    public List<String> getPlayers(){
        return players;
    }
    
    public void setPlayers(List<String> players){
        this.players = players;
    }
    
    public void addPlayer(String... player){
        for(String p:player){
            players.add(p);
        }
    }
    
    @Override
    public SystemMail createSystemMail() {
        return new AdminSystemMail();
    }

    @Override
    public SystemMail loadSystemMail(YamlConfiguration yml) {
        return new AdminSystemMail(yml);
    }

    @Override
    public void checkPlayerData(PlayerData pd) {}

    @Override
    public boolean needCheckPlayerData() {
        return false;
    }

    @Override
    public boolean couldSend2Player(Player p) {
        return true;
    }
    
    @Override
    public boolean send2Player(Player p){
        return false;
    }

    @Override
    public boolean autoCreateDatabaseTable() {
        return false;
    }

    @Override
    public boolean couldSendMail(CommandSender sender) {
        return sender.isOp();
    }

    @Override
    public boolean isExpire() {
        return false;
    }
    
    @Override
    public SystemMail sendMail(){
        String[] parr = new String[players.size()];
        players.toArray(parr);
        List<String> list = SinglePlayerMailAPI.send2Player(new PersonMail(this), parr);
        if(list.size()==players.size()){
            this.setId(1);
        }else{
            this.setId(0);
        }
        return this;
    }
    
    @Override
    public YamlConfiguration toYamlConfiguration(){
        YamlConfiguration yml = super.toYamlConfiguration();
        yml.set("players", this.players);
        return super.toYamlConfiguration();
    }
    
}

package com.tripleying.dogend.module.playermail.mail;

import com.tripleying.dogend.mailbox.api.data.Data;
import com.tripleying.dogend.mailbox.api.data.DataType;
import com.tripleying.dogend.mailbox.api.mail.PersonMail;
import com.tripleying.dogend.mailbox.api.mail.PlayerData;
import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.module.playermail.PlayerMail;
import com.tripleying.dogend.module.singleplayermailapi.SinglePlayerMailAPI;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PlayerSystemMail extends SystemMail {

    @Data(type = DataType.String)
    private String player;

    public PlayerSystemMail() {
        super(PlayerMail.player_type, PlayerMail.player_display);
        player = null;
    }
    
    public PlayerSystemMail(YamlConfiguration yml) {
        super(yml);
        player = yml.getString("player");
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    @Override
    public SystemMail createSystemMail() {
        return new PlayerSystemMail();
    }

    @Override
    public SystemMail loadSystemMail(YamlConfiguration yml) {
        return new PlayerSystemMail(yml);
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
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return false;
    }

    @Override
    public boolean isExpire() {
        return false;
    }
    
    @Override
    public SystemMail sendMail(){
        List<String> list = SinglePlayerMailAPI.send2Player(new PersonMail(this), player);
        if(list.isEmpty()){
            this.setId(0);
        }else{
            this.setId(1);
        }
        return this;
    }
    
    @Override
    public YamlConfiguration toYamlConfiguration(){
        YamlConfiguration yml = super.toYamlConfiguration();
        yml.set("player", this.player);
        return super.toYamlConfiguration();
    }
    
    
    
}
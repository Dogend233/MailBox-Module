package com.tripleying.dogend.module.singleplayermailapi;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import com.tripleying.dogend.mailbox.api.mail.PersonMail;
import com.tripleying.dogend.mailbox.api.module.MailBoxModule;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class ModuleEdition extends MailBoxModule {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.reloadConfig();
        YamlConfiguration config = this.getConfig();
        IdGeneratorOptions op = new IdGeneratorOptions(Short.parseShort(config.getString("yitter.WorkId", "1")));
        op.WorkerIdBitLength = Byte.parseByte(config.getString("yitter.WorkerIdBitLength", "6"));
        YitIdHelper.setIdGenerator(op);
    }
    
    public static List<String> send2Player(PersonMail pm, String... names){
        return SinglePlayerMailAPI.send2Player(pm, names);
    }
    
    public static List<Player> send2Player(PersonMail pm, Player... ps){
        return SinglePlayerMailAPI.send2Player(pm, ps);
    }
    
    public static boolean send2OnlinePlayer(PersonMail pm, Player p){
        return SinglePlayerMailAPI.send2OnlinePlayer(pm, p);
    }
    
    public static boolean send2OfflinePlayerByName(PersonMail pm, String name){
        return SinglePlayerMailAPI.send2OfflinePlayerByName(pm, name);
    }
    
    public static boolean send2OfflinePlayerByUUID(PersonMail pm, String uuid){
        return SinglePlayerMailAPI.send2OfflinePlayerByUUID(pm, uuid);
    }

}
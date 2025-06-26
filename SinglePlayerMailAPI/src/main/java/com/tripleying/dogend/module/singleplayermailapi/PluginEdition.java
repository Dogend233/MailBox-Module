package com.tripleying.dogend.module.singleplayermailapi;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import com.tripleying.dogend.mailbox.api.mail.PersonMail;
import com.tripleying.dogend.mailbox.util.FileUtil;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginEdition extends JavaPlugin {

    @Override
    public void onEnable() {
        YamlConfiguration config = getConfig("config.yml");
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
    
    public File getConfigFolder(){
        File file = new File("plugins/SinglePlayerMailAPI");
        if(!file.exists()) file.mkdirs();
        return file;
    }
    
    public YamlConfiguration getConfig(String file){
        File f = new File(getConfigFolder(), file);
        if(!f.exists()){
            try {
                try (InputStreamReader isr = FileUtil.getInputStreamReader(file)) {
                    if(FileUtil.saveYaml(isr, f)){
                        MessageUtil.log(MessageUtil.file_create.replaceAll("%file%", file));
                    }else{
                        throw new IOException();
                    }
                    isr.close();
                }
            } catch (IOException ex) {
                MessageUtil.error(MessageUtil.file_create_error.replaceAll("%file%", file));
                return null;
            }
        }
        MessageUtil.log(MessageUtil.file_read.replaceAll("%file%", file));
        try {
            return FileUtil.getYaml(f);
        } catch (Exception ex) {
            return new YamlConfiguration();
        }
    }

}
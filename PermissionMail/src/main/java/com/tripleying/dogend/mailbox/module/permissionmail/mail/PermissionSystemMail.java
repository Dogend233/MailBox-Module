package com.tripleying.dogend.mailbox.module.permissionmail.mail;

import com.tripleying.dogend.mailbox.api.data.Data;
import com.tripleying.dogend.mailbox.api.data.DataType;
import com.tripleying.dogend.mailbox.api.mail.PlayerData;
import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.manager.MailManager;
import com.tripleying.dogend.mailbox.module.permissionmail.PermissionMail;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PermissionSystemMail extends SystemMail {
    
    @Data(type = DataType.String)
    private String permission;

    public PermissionSystemMail() {
        super(PermissionMail.type, PermissionMail.display);
        permission = "";
    }
    
    public PermissionSystemMail(YamlConfiguration yml) {
        super(yml);
        permission = yml.getString("permission");
    }

    @Override
    public SystemMail createSystemMail() {
        return new PermissionSystemMail();
    }

    @Override
    public SystemMail loadSystemMail(YamlConfiguration yml) {
        return new PermissionSystemMail(yml);
    }

    @Override
    public boolean needCheckPlayerData() {
        return true;
    }

    @Override
    public void checkPlayerData(PlayerData pd) {
        Player p = pd.getPlayer();
        if(p==null) return;
        Object d = pd.getData(this.type);
        List<String> now  = d==null?new ArrayList():(List<String>)d;
        List<SystemMail> sml = MailManager.getMailManager().getSystemMailList(this.type);
        List<String> rl = new ArrayList();
        sml.forEach(sm -> {
            String id = Long.toString(sm.getId());
            rl.add(id);
            try {
                if (!now.contains(id) && sm.couldSend2Player(p) && sm.send2Player(p)) {
                    now.add(id);
                }
            }catch(Exception ex){}
        });
        Iterator<String> it = now.iterator();
        while(it.hasNext()){
            String i = it.next();
            if(!rl.contains(i)){
                it.remove();
            }
        }
        pd.setData(this.type, now);
        pd.saveData();
    }

    @Override
    public boolean couldSend2Player(Player p) {
        if(this.isExpire()){
            this.deleteMail();
            return false;
        }
        if(p.hasPermission(permission)){
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
    
    public PermissionSystemMail setPermission(String perm){
        this.permission = perm;
        return this;
    }

    public String getPermission() {
        return this.permission;
    }

    @Override
    public boolean isExpire() {
        return false;
    }
    
    @Override
    public YamlConfiguration toYamlConfiguration(){
        YamlConfiguration yml = super.toYamlConfiguration();
        yml.set("permission", this.permission);
        return super.toYamlConfiguration();
    }
    
}
package com.tripleying.dogend.mailbox.module.datemail.mail;

import com.tripleying.dogend.mailbox.api.data.Data;
import com.tripleying.dogend.mailbox.api.data.DataType;
import com.tripleying.dogend.mailbox.api.mail.PlayerData;
import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.manager.MailManager;
import com.tripleying.dogend.mailbox.module.datemail.DateMail;
import com.tripleying.dogend.mailbox.module.datemail.DateUtil;
import com.tripleying.dogend.mailbox.util.TimeUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class DateSystemMail extends SystemMail {
    
    @Data(type = DataType.DateTime)
    private String start;
    @Data(type = DataType.DateTime)
    private String end;

    public DateSystemMail() {
        super(DateMail.type, DateMail.display);
        start = TimeUtil.currentTimeString();
        end = start;
    }
    
    public DateSystemMail(YamlConfiguration yml) {
        super(yml);
        start = yml.getString("start");
        end = yml.getString("end");
    }

    @Override
    public SystemMail createSystemMail() {
        return new DateSystemMail();
    }

    @Override
    public SystemMail loadSystemMail(YamlConfiguration yml) {
        return new DateSystemMail(yml);
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
        if(DateUtil.isStart(this.start)){
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
    
    public DateSystemMail setStartTime(String time){
        if(this.start.equals(this.end)){
            this.end = time;
        }
        this.start = time;
        return this;
    }

    public String getStartTime() {
        return this.start;
    }
    
    public DateSystemMail setEndTime(String time){
        this.end = time;
        return this;
    }

    public String getEndTime() {
        return this.end;
    }

    @Override
    public boolean isExpire() {
        if(this.start.equals(this.end)) return false;
        return DateUtil.isEnd(this.end);
    }
    
    @Override
    public YamlConfiguration toYamlConfiguration(){
        YamlConfiguration yml = super.toYamlConfiguration();
        yml.set("start", this.start);
        yml.set("end", this.end);
        return super.toYamlConfiguration();
    }
    
}
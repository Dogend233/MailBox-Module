package com.tripleying.dogend.mailbox.module.cdkeymail.data;

import com.tripleying.dogend.mailbox.api.data.Data;
import com.tripleying.dogend.mailbox.api.data.DataType;
import com.tripleying.dogend.mailbox.api.mail.CustomData;
import com.tripleying.dogend.mailbox.module.cdkeymail.CDKeyMail;
import com.tripleying.dogend.mailbox.module.cdkeymail.mail.CDKeySystemMail;
import com.tripleying.dogend.mailbox.util.TimeUtil;
import java.util.LinkedHashMap;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CDKeyCollectedData extends CustomData {
    
    // id
    @Data(type = DataType.Primary)
    private long id;
    
    // CDKey
    @Data(type = DataType.String, size = 32)
    private String cdk;
    
    // 邮件ID
    @Data(type = DataType.Long)
    private long mail;
    
    // 玩家UUID
    @Data(type = DataType.String)
    private String uuid;
    
    // 领取时间
    @Data(type = DataType.DateTime)
    private String time;

    public CDKeyCollectedData() {
        super("mailbox_system_".concat(CDKeyMail.getMain().getType()).concat("_collected_data"));
    }

    public CDKeyCollectedData setCdk(String cdk) {
        this.cdk = cdk;
        return this;
    }

    public CDKeyCollectedData setMail(long mail) {
        this.mail = mail;
        return this;
    }

    public CDKeyCollectedData setUUID(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public CDKeyCollectedData setNowTime() {
        this.time = TimeUtil.currentTimeString();
        return this;
    }

    @Override
    public CDKeyCollectedData loadFromYamlConfiguration(YamlConfiguration yml) {
        CDKeyCollectedData cdcd = new CDKeyCollectedData();
        cdcd.id = yml.getLong("id");
        cdcd.cdk = yml.getString("cdk");
        cdcd.mail = yml.getLong("mail");
        cdcd.uuid = yml.getString("uuid");
        cdcd.time = yml.getString("time");
        return cdcd;
    }
    
    public static boolean isUsedCDKye(Player p, String cdkey){
        LinkedHashMap<String, Object> sel = new LinkedHashMap();
        sel.put("cdk", cdkey);
        sel.put("uuid", p.getUniqueId().toString());
        List<CustomData> list = new CDKeyCollectedData().selectCustomData(sel);
        if(list.isEmpty()){
            return false;
        }else{
            return true;
        }
    }
    
    public static boolean couldUseCDKey(Player p, CDKeyData cdkey){
        if(cdkey.isUsed()) return false;
        CDKeySystemMail cdkm = cdkey.getMail();
        if(cdkm.isRepeat()){
            return true;
        }else{
            LinkedHashMap<String, Object> sel = new LinkedHashMap();
            sel.put("mail", cdkm.getId());
            sel.put("uuid", p.getUniqueId().toString());
            List<CustomData> list = new CDKeyCollectedData().selectCustomData(sel);
            if(list.isEmpty()){
                return true;
            }else{
                return false;
            }
        }
    }
    
}

package com.tripleying.dogend.mailbox.module.cdkeymail.data;

import com.tripleying.dogend.mailbox.api.data.Data;
import com.tripleying.dogend.mailbox.api.data.DataType;
import com.tripleying.dogend.mailbox.api.mail.CustomData;
import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.manager.MailManager;
import com.tripleying.dogend.mailbox.module.cdkeymail.CDKeyMail;
import com.tripleying.dogend.mailbox.module.cdkeymail.mail.CDKeySystemMail;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CDKeyData extends CustomData {
    
    // id
    @Data(type = DataType.Primary)
    private long id;
    
    // CDKey
    @Data(type = DataType.String, size = 32)
    private String cdk;
    
    // 邮件ID
    @Data(type = DataType.Long)
    private long mail;
    
    // 是否已被使用
    @Data(type = DataType.Boolean)
    private boolean used;
    
    // 是否可以不同玩家重复使用(设置为true时used失效)
    @Data(type = DataType.Boolean)
    private boolean repeat;
    
    private CDKeySystemMail cdkey = null;
    
    private static Map<String, CDKeyData> map = new HashMap();

    public CDKeyData() {
        super("mailbox_system_".concat(CDKeyMail.getMain().getType()).concat("_cdkey_data"));
    }

    public CDKeyData(String cdk, long mail) {
        this();
        this.cdk = cdk;
        this.mail = mail;
        this.used = false;
    }

    public CDKeyData(String cdk, long mail, boolean repeat) {
        this(cdk, mail);
        this.repeat = repeat;
    }

    public CDKeySystemMail getMail() {
        if(cdkey==null){
            SystemMail sm = MailManager.getMailManager().getSystemMailById(CDKeyMail.getMain().getType(), mail);
            if(sm!=null){
                cdkey = (CDKeySystemMail)sm;
            }
        }
        return cdkey;
    }
    
    public long getMailId(){
        return this.mail;
    }
    
    public boolean sendMail(Player p){
        if(cdkey!=null && cdkey.send2Player(p)){
            if(!repeat) {
                setUsed();
                this.updateCustomDataByPrimaryKey();
            }
            new CDKeyCollectedData().setCdk(cdk).setMail(mail).setUUID(p.getUniqueId().toString()).setNowTime().insertCustomData();
            return true;
        }
        return false;
    }

    public String getCDKey() {
        return cdk;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed() {
        this.used = true;
    }

    public boolean isRepeat() {
        return repeat;
    }

    @Override
    public CustomData loadFromYamlConfiguration(YamlConfiguration yml) {
        CDKeyData cdkd = new CDKeyData();
        cdkd.id = yml.getLong("id");
        cdkd.cdk = yml.getString("cdk");
        cdkd.mail = yml.getLong("mail");
        cdkd.used = yml.getBoolean("used");
        cdkd.repeat = yml.getBoolean("repeat");
        return cdkd;
    }
    
    public static CDKeyData getCDKeyData(String cdk){
        if(map.containsKey(cdk)){
            return map.get(cdk);
        }else{
            LinkedHashMap<String, Object> sel = new LinkedHashMap();
            sel.put("cdk", cdk);
            List<CustomData> list = new CDKeyData().selectCustomData(sel);
            if(list.isEmpty()){
                return null;
            }else{
                CDKeyData cdkey = (CDKeyData)list.get(0);
                if(cdkey.isRepeat()){
                    map.put(cdk, cdkey);
                }
                return cdkey;
            }
        }
    }
    
    public static List<CDKeyData> getUnusedCDKey(long mail){
        LinkedHashMap<String, Object> sel = new LinkedHashMap();
        sel.put("mail", mail);
        sel.put("used", false);
        List<? extends CustomData> list = new CDKeyData().selectCustomData(sel);
        return (List<CDKeyData>)list;
    }
    
}

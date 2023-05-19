package com.tripleying.dogend.mailbox.module.cdkeymail.mail;

import com.tripleying.dogend.mailbox.api.data.Data;
import com.tripleying.dogend.mailbox.api.data.DataType;
import com.tripleying.dogend.mailbox.api.mail.CustomData;
import com.tripleying.dogend.mailbox.module.cdkeymail.CDKeyMail;
import org.bukkit.configuration.file.YamlConfiguration;

public class CDKeyData extends CustomData {
    
    @Data(type = DataType.String, size = 32)
    private String cdk;
    
    @Data(type = DataType.Long)
    private long id;

    public CDKeyData() {
        super("mailbox_system_".concat(CDKeyMail.type).concat("_data"));
    }

    @Override
    public CustomData loadFromYamlConfiguration(YamlConfiguration yml) {
        CDKeyData cdkd = new CDKeyData();
        cdkd.cdk = yml.getString("cdk");
        cdkd.id = yml.getLong("id");
        return cdkd;
    }
    
}

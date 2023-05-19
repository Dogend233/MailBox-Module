package com.tripleying.dogend.mailbox.module.commonguiapi;

import com.tripleying.dogend.mailbox.api.module.MailBoxModule;
import com.tripleying.dogend.mailbox.module.commonguiapi.gui.ReplaceConfig;
import org.bukkit.configuration.file.YamlConfiguration;

public class CommonGUIAPI extends MailBoxModule {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.reloadConfig();
        YamlConfiguration config = this.getConfig();
        ReplaceConfig.init(config);
    }
    
}

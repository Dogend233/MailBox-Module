package com.tripleying.dogend.mailbox.module.vaultmoney;

import com.tripleying.dogend.mailbox.api.module.MailBoxModule;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import org.bukkit.configuration.file.YamlConfiguration;

public class VaultMoney extends MailBoxModule {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.reloadConfig();
        YamlConfiguration config = this.getConfig();
        VaultDoubleMoney money = new VaultDoubleMoney(config.getString("type", "vault"), MessageUtil.color(config.getString("display", "&6金币")));
        if(money.isAvaliable()){
            this.registerMoney(money);
        }else{
            MessageUtil.error("VaultMoney: 插件初始化失败, 请检查是否安装Vault且有任何一个Bank(例如Essentials)");
        }
    }

    @Override
    public void onDisable() {
        this.unregisterAllMoney();
    }
    
}

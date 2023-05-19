package com.tripleying.dogend.mailbox.module.playerpointsmoney;

import com.tripleying.dogend.mailbox.api.module.MailBoxModule;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import org.bukkit.configuration.file.YamlConfiguration;


public class PlayerPointsMoney extends MailBoxModule {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.reloadConfig();
        YamlConfiguration config = this.getConfig();
        PlayerPointsIntegerMoney money = new PlayerPointsIntegerMoney(config.getString("type", "point"), MessageUtil.color(config.getString("display", "&b点券")));
        if(money.isAvaliable()){
            this.registerMoney(money);
        }else{
            MessageUtil.error("PlayerPointsMoney: 插件初始化失败, 请检查是否安装PlayerPoints");
        }
    }

    @Override
    public void onDisable() {
        this.unregisterAllMoney();
    }
}

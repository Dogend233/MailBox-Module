/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tripleying.dogend.mailbox.module.cdkeymail;

import com.tripleying.dogend.mailbox.api.command.BaseCommand;
import com.tripleying.dogend.mailbox.api.module.MailBoxModule;
import com.tripleying.dogend.mailbox.module.cdkeymail.mail.CDKeySystemMail;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author Dogend
 */
public class CDKeyMail extends MailBoxModule implements BaseCommand {
    
    public static String type;
    public static String display;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.reloadConfig();
        YamlConfiguration config = this.getConfig();
        type = config.getString("type", "cdkey");
        display = MessageUtil.color(config.getString("display", "&6兑换码"));
        this.registerSystemMail(new CDKeySystemMail());
//        if(ModuleManager.getModuleManager().hasModule("CommonGUIAPI")){
//            PermissionMailConfig.registerConfig();
//        }
    }

    @Override
    public void onDisable() {
        this.unregisterAllSystemMail();
    }

    @Override
    public String getLabel() {
        return "cdkey";
    }

    @Override
    public String getDescription(CommandSender sender) {
        return sender.isOp()
                ?"使用/生成/导出兑换码"
                :"使用兑换码";
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if(args.length>1){
            switch(args[1]){
                case "use":
                    if(sender instanceof Player && args.length>2){
                        String ucdk = args[2];
                    }
                    break;
                case "list":
                    if(sender.isOp()){
                        try{
                            int lpage = 1;
                            if(args.length>2){
                                lpage = Integer.parseInt(args[2]);
                            }
                        }catch(Exception e){
                            
                        }
                    }
                    break;
                case "create":
                    if(sender.isOp() && args.length>2){
                        try{
                            long cid = Long.parseLong(args[2]);
                            long ccount = 1;
                            if(args.length>3){
                                ccount = Long.parseLong(args[3]);
                            }
                        }catch(Exception e){
                            
                        }
                    }
                    break;
                case "export":
                    if(sender.isOp() && args.length>2){
                        try{
                            long eid = Long.parseLong(args[2]);
                        }catch(Exception e){
                            
                        }
                    }
                    break;
            }
        }
        helpCommand(sender);
        return true;
    }
    
    public void helpCommand(CommandSender cs){
        
    }
    
}
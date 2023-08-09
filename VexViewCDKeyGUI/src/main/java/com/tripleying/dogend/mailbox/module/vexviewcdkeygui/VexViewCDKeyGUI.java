package com.tripleying.dogend.mailbox.module.vexviewcdkeygui;

import com.tripleying.dogend.mailbox.api.command.BaseCommand;
import com.tripleying.dogend.mailbox.api.command.BaseTabCompleter;
import com.tripleying.dogend.mailbox.api.module.MailBoxModule;
import com.tripleying.dogend.mailbox.manager.ModuleManager;
import com.tripleying.dogend.mailbox.module.cdkeymail.CDKeyMail;
import com.tripleying.dogend.mailbox.module.vexviewcdkeygui.gui.CDKeyGUI;
import com.tripleying.dogend.mailbox.module.vexviewcdkeygui.gui.MailListGUI;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class VexViewCDKeyGUI extends MailBoxModule implements BaseCommand, BaseTabCompleter {
    
    private String cmd;
    private Map<String, String> message;
    private static CDKeyMail cdkey_mail = null;
    
    @Override
    public void onEnable() {
        String[] cfgs = new String[]{
            "config.yml",
            "gui/mail_list.yml",
            "gui/cdkey.yml"
        };
        this.saveDefaultConfig();
        this.reloadConfig();
        YamlConfiguration config = this.getConfig();
        cmd = config.getString("command", "cdk");
        message = new HashMap();
        config.getConfigurationSection("message").getValues(false).forEach((k,v) -> message.put(k,MessageUtil.color(v.toString()).replaceAll("%cmd%", cmd)));
        MailListGUI.init(this.getConfig("gui/mail_list.yml"));
        CDKeyGUI.init(this.getConfig("gui/cdkey.yml"));
        this.registerCommand(this);
        if(ModuleManager.getModuleManager().hasModule("CDKeyMail")){
            cdkey_mail = CDKeyMail.getMain();
        }
    }
    
    @Override
    public void onDisable(){
        this.unregisterAllCommand();
    }

    @Override
    public String getLabel() {
        return this.cmd;
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
                case "list":
                    if(sender.isOp() && sender instanceof Player){
                        MailListGUI.open((Player)sender);
                    }else{
                        help(sender);
                    }
                    break;
                case "help":
                default:
                    help(sender);
                    break;
            }
        }else{
            if(sender.isOp() && sender instanceof Player){
                CDKeyGUI.open((Player)sender);
            }else{
                help(sender);
            }
        }
        return true;
    }
    
    public void help(CommandSender sender){
        if(sender.isOp()){
            sender.sendMessage(message.get("help_admin"));
        }else{
            sender.sendMessage(message.get("help"));
        }
    }

    @Override
    public boolean allowTab(CommandSender cs) {
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        switch (args.length) {
            case 2:
                if(args[1].length()==0){
                    if(sender.isOp()){
                        return Arrays.asList("help","list");
                    }else{
                        return Arrays.asList("help");
                    }
                }else{
                    if("help".startsWith(args[1])){
                        return Arrays.asList("help");
                    }
                    if(sender.isOp()){
                        if("list".startsWith(args[1])){
                            return Arrays.asList("list");
                        }
                    }
                }
        }
        return null;
    }
    
    public static String getCDKeyMailLabel(){
        return cdkey_mail==null?"cdkey":cdkey_mail.getLabel();
    }
    
}
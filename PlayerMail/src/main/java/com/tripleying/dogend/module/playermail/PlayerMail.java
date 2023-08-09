package com.tripleying.dogend.module.playermail;

import com.tripleying.dogend.mailbox.api.command.BaseCommand;
import com.tripleying.dogend.mailbox.api.command.BaseTabCompleter;
import com.tripleying.dogend.mailbox.api.module.MailBoxModule;
import com.tripleying.dogend.mailbox.manager.ModuleManager;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import com.tripleying.dogend.module.playermail.gui.AdminMailConfig;
import com.tripleying.dogend.module.playermail.mail.AdminSystemMail;
import com.tripleying.dogend.module.playermail.template.TemplatePlayerMail;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerMail extends MailBoxModule implements BaseCommand, BaseTabCompleter {

    public static String player_type;
    public static String player_display;
    public static String admin_type;
    public static String admin_display;
    public static boolean enable;
    public static PlayerOption po;
    private List<String> help;
    private TemplatePlayerMail tpm;
    private boolean template;
    
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.reloadConfig();
        YamlConfiguration config = this.getConfig();
        player_type = config.getString("player.type", "player");
        player_display = MessageUtil.color(config.getString("player.display", "&6玩家邮件"));
        enable = config.getBoolean("player.enable", false);
        admin_type = config.getString("admin.type", "admin");
        admin_display = MessageUtil.color(config.getString("admin.display", "&6管理员邮件"));
        po = new PlayerOption(config);
//        if(enable) this.registerSystemMail(new PlayerSystemMail());
        this.registerSystemMail(new AdminSystemMail());
        if(ModuleManager.getModuleManager().hasModule("CommonGUIAPI")){
            //if(enable) PlayerSystemMail.registerConfig();
            AdminMailConfig.registerConfig();
        }
        if(ModuleManager.getModuleManager().hasModule("TemplateMail")){
            tpm = TemplatePlayerMail.newTemplatePlayerMail(ModuleManager.getModuleManager().getMailBoxModule("TemplateMail"));
            if(tpm==null){
                MessageUtil.error("PlayerMail: 模板邮件实例获取失败!");
            }else{
                MessageUtil.log("PlayerMail: 模板邮件实例获取成功!");
                help = Arrays.asList(
                    "==========§6[MailBox-PlayerMail]==========",
                    "/mailbox playermail §b主指令",
                    "/mailbox playermail help §b指令帮助",
                    "/mailbox playermail 玩家名 模板ID §b发送模板邮件"
                );
                this.registerCommand(this);
            }
        }else{
            tpm = null;
        }
        template = tpm!=null;
    }

    @Override
    public void onDisable() {
        this.unregisterAllSystemMail();
        this.unregisterAllCommand();
    }
    
    public static PlayerOption getOption(){
        return po;
    }

    @Override
    public String getLabel() {
        return "playermail";
    }

    @Override
    public String getDescription(CommandSender cs) {
        return cs.isOp()
                ?"玩家邮件指令"
                :null;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if(sender.isOp()){
            if(args.length>2){
                try{
                    String player = args[1];
                    long id = Long.parseLong(args[2]);
                    if(tpm.sendMail(id, player)){
                        MessageUtil.log(sender, "模板邮件 ".concat(Long.toString(id)).concat(" 已发送给玩家 ").concat(player));
                    }else{
                        MessageUtil.error(sender, "模板邮件发送失败");
                    }
                }catch(Exception e){
                    MessageUtil.error(sender, "模板邮件发送失败");
                }
            }else{
                help(sender);
            }
        }
        return true;
    }
    
    private void help(CommandSender sender){
        help.forEach(h -> MessageUtil.log(sender, h));
    }

    @Override
    public boolean allowTab(CommandSender cs) {
        return cs.isOp();
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, String[] strings) {
        return null;
    }
    
}

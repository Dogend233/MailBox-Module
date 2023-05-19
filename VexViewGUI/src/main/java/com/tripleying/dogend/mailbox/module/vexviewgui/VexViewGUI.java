package com.tripleying.dogend.mailbox.module.vexviewgui;

import com.tripleying.dogend.mailbox.api.command.BaseCommand;
import com.tripleying.dogend.mailbox.api.module.MailBoxModule;
import com.tripleying.dogend.mailbox.module.vexviewgui.gui.PersonListGUI;
import com.tripleying.dogend.mailbox.module.vexviewgui.gui.ReplaceMailGUI;
import com.tripleying.dogend.mailbox.module.vexviewgui.gui.SelectTypeGUI;
import com.tripleying.dogend.mailbox.module.vexviewgui.gui.SystemListGUI;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VexViewGUI extends MailBoxModule implements BaseCommand {
    
    private List<String> help;
    private List<String> admin_help;
    private static VexViewGUI vvgui;
    private VexViewGUIListener lr;
    
    @Override
    public void onEnable() {
        vvgui = this;
        String[] cfgs = new String[]{
            "config.yml",
            "gui/person_list.yml",
            "gui/select_type.yml",
            "gui/replace_mail.yml",
            "gui/system_list.yml"
        };
        help = Arrays.asList(
            "==========§6[MailBox-VexViewGUI]==========",
            "/mailbox vvgui §b主指令",
            "/mailbox vvgui new §b发送邮件",
            "/mailbox vvgui list §b邮件列表",
            "/mailbox vvgui help §b指令帮助"
        );
        admin_help = new ArrayList();
        admin_help.addAll(help);
        admin_help.add("/mailbox vvgui system §b系统邮件列表");
        admin_help.add("/mailbox vvgui reload §b重载模块");
        this.saveDefaultConfig(cfgs);
        this.reloadConfig(cfgs);
        PersonListGUI.init(this.getConfig("gui/person_list.yml"));
        SelectTypeGUI.init(this.getConfig("gui/select_type.yml"));
        ReplaceMailGUI.init(this.getConfig("gui/replace_mail.yml"));
        SystemListGUI.init(this.getConfig("gui/system_mail.yml"));
        this.registerCommand(this);
        lr = new VexViewGUIListener(this.getConfig());
        this.registerListener(lr);
    }

    @Override
    public void onDisable() {
        lr.unload();
        this.unregisterAllCommand();
        this.unregisterAllListener();
    }

    @Override
    public String getLabel() {
        return "vvgui";
    }

    @Override
    public String getDescription(CommandSender sender) {
        return "用VexView的GUI收发邮件";
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if(sender instanceof Player){
            if(args.length>1){
                switch(args[1]){
                    case "new":
                        SelectTypeGUI.open4ReplaceMail((Player)sender);
                        break;
                    case "list":
                        PersonListGUI.open((Player)sender);
                        break;
                    case "system":
                        if(sender.isOp()){
                            SelectTypeGUI.open4SystemList((Player)sender);
                            break;
                        }else{
                            help(sender);
                        }
                        break;
                    case "reload":
                        if(sender.isOp()){
                            this.onDisable();
                            this.onEnable();
                            MessageUtil.log(sender, "模块已重载");
                        }else{
                            help(sender);
                        }
                        break;
                    case "help":
                    default:
                        help(sender);
                }
            }else{
                PersonListGUI.open((Player)sender);
            }
        }else{
            if(args.length>1 && args[0].equals("reload")){
                if(sender.isOp()){
                    this.onDisable();
                    this.onEnable();
                    MessageUtil.log(sender, "模块已重载");
                }
            }else{
                help(sender);
            }
        }
        return true;
    }
    
    private void help(CommandSender sender){
        if(sender.isOp()){
            admin_help.forEach(h -> MessageUtil.log(sender, h));
        }else{
            help.forEach(h -> MessageUtil.log(sender, h));
        }
    }

    public static VexViewGUI getVexViewGUI() {
        return vvgui;
    }
    
}

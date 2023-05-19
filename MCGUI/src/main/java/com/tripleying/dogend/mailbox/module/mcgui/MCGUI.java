package com.tripleying.dogend.mailbox.module.mcgui;

import com.tripleying.dogend.mailbox.api.command.BaseCommand;
import com.tripleying.dogend.mailbox.api.mail.PlayerData;
import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.api.module.MailBoxModule;
import com.tripleying.dogend.mailbox.manager.DataManager;
import com.tripleying.dogend.mailbox.manager.MailManager;
import com.tripleying.dogend.mailbox.module.commonguiapi.gui.ReplaceConfig;
import com.tripleying.dogend.mailbox.module.mcgui.gui.PersonListGUI;
import com.tripleying.dogend.mailbox.module.mcgui.util.GUIUtil;
import com.tripleying.dogend.mailbox.module.mcgui.gui.PreviewMailGUI;
import com.tripleying.dogend.mailbox.module.mcgui.gui.ReplaceItemGUI;
import com.tripleying.dogend.mailbox.module.mcgui.gui.ReplaceListGUI;
import com.tripleying.dogend.mailbox.module.mcgui.gui.SelectTypeGUI;
import com.tripleying.dogend.mailbox.module.mcgui.gui.SystemListGUI;
import com.tripleying.dogend.mailbox.module.mcgui.util.MCReplacePackage;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MCGUI extends MailBoxModule implements BaseCommand {
    
    private static MCGUI mcgui;
    private String[] cfgs;
    private List<String> help;
    private List<String> admin_help;
    private MCGUIListener listener;
    private PersonListGUI plist;
    private SelectTypeGUI seltype;
    private PreviewMailGUI premail;
    private ReplaceListGUI rpclist;
    private ReplaceItemGUI rpcitem;
    private SystemListGUI slist;

    @Override
    public void onEnable() {
        mcgui = this;
        cfgs = new String[]{
            "config.yml",
            "gui/person_list.yml",
            "gui/select_type.yml",
            "gui/preview_mail.yml",
            "gui/replace_list.yml",
            "gui/replace_item.yml",
            "gui/system_list.yml"
        };
        help = Arrays.asList(
            "==========§6[MailBox-MCGUI]==========",
            "/mailbox mcgui §b主指令",
            "/mailbox mcgui new §b新建邮件",
            "/mailbox mcgui list §b个人邮件列表",
            "/mailbox mcgui help §b模块帮助"
        );
        admin_help = new ArrayList();
        admin_help.addAll(help);
        admin_help.add("/mailbox mcgui system [系统邮件类型] §b系统邮件列表");
        admin_help.add("/mailbox mcgui reload §b重载模块");
        this.saveDefaultConfig(cfgs);
        this.reloadConfig(cfgs);
        GUIUtil.init(this.getConfig());
        plist = new PersonListGUI(this.getConfig("gui/person_list.yml"));
        seltype = new SelectTypeGUI(this.getConfig("gui/select_type.yml"));
        premail = new PreviewMailGUI(this.getConfig("gui/preview_mail.yml"));
        rpclist = new ReplaceListGUI(this.getConfig("gui/replace_list.yml"));
        rpcitem = new ReplaceItemGUI(this.getConfig("gui/replace_item.yml"));
        slist = new SystemListGUI(this.getConfig("gui/system_list.yml"));
        this.registerCommand(this);
        listener = new MCGUIListener();
        this.registerListener(listener);
    }

    @Override
    public void onDisable() {
        this.unregisterAllListener();
        this.unregisterAllCommand();
    }

    @Override
    public String getLabel() {
        return "mcgui";
    }

    @Override
    public String getDescription(CommandSender sender) {
        return "用原版Minecraft的GUI收发邮件";
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if(sender instanceof Player){
            if(args.length>1){
                switch(args[1]){
                    case "new":
                        openSelectTypeGUI((Player)sender);
                        break;
                    case "list":
                        PlayerData pd = DataManager.getDataManager().getPlayerData((Player)sender);
                        if(pd!=null) MailManager.getMailManager().checkPlayerData(pd);
                        openPersonListGUI((Player)sender, 1);
                        break;
                    case "system":
                        if(sender.isOp() && args.length>2){
                            String type = args[2];
                            SystemMail sm = MailManager.getMailManager().createSystemMail(type);
                            if(sm==null || !sm.autoCreateDatabaseTable()){
                                MessageUtil.error(sender, "邮件类型不存在或没有默认的邮件存储表");
                            }else{
                                slist.open((Player)sender, sm, 1);
                            }
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
                        break;
                }
            }else{
                help(sender);
            }
            return true;
        }
        return false;
    }
    
    private void help(CommandSender sender){
        if(sender.isOp()){
            admin_help.forEach(h -> MessageUtil.log(sender, h));
            MailManager.getMailManager().getSystemMailTypeList().forEach(sm -> {
                if(sm.autoCreateDatabaseTable()){
                    MessageUtil.log(sender, "/mailbox mcgui system ".concat(sm.getType()).concat(" §b查看").concat(sm.getDisplay()).concat("邮件列表"));
                }
            });
        }else{
            help.forEach(h -> MessageUtil.log(sender, h));
        }
    }
    
    public void openPersonListGUI(Player p, int page){
        plist.open(p, page);
    }
    
    public void openSelectTypeGUI(Player p){
        seltype.open(p);
    }
    
    public void openPreviewMailGUI(Player p, SystemMail sm){
        ReplaceConfig rc = ReplaceConfig.getConfig(sm, p);
        MCReplacePackage mcrp = MCReplacePackage.getMCReplacePackage(rc);
        openPreviewMailGUI(rc, mcrp);
    }
    
    public void openPreviewMailGUI(ReplaceConfig rc, MCReplacePackage mcrp){
        premail.open(rc, mcrp);
    }
    
    public void openReplaceListGUI(ReplaceConfig rc, MCReplacePackage mcrp, String type){
        rpclist.openGui(rc, mcrp, type);
    }
    
    public void openReplaceItemGUI(ReplaceConfig rc, MCReplacePackage mcrp, String type){
        rpcitem.openGui(rc, mcrp, type);
    }

    public static MCGUI getMCGUI(){
        return mcgui;
    }
    
    public MCGUIListener getListener(){
        return listener;
    }
    
}

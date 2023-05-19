package com.tripleying.dogend.mailbox.module.vexviewgui.gui;

import com.tripleying.dogend.mailbox.MailBox;
import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.manager.MailManager;
import com.tripleying.dogend.mailbox.module.commonguiapi.gui.ReplaceConfig;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.ButtonPackage;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.GuiPackage;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.ListPackage;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.VexViewUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.components.ScrollingListComponent;
import lk.vexview.gui.components.VexComponents;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SelectTypeGUI {
    
    private static GuiPackage background;
    private static ListPackage list;
    private static ButtonPackage button;
    
    public static void open4ReplaceMail(Player p){
        MailManager mm = MailBox.getMailBox().getMailManager();
        List<String> tl = new ArrayList();
        for(SystemMail sm:mm.getSystemMailTypeList()){
            if(sm.couldSendMail(p)) tl.add(sm.getType());
        }
        Iterator<String> it = tl.iterator();
        List<VexComponents> vcl = Arrays.asList(list.getVexScrollingList(tl.size(), (ox, oy) -> {
            List<ScrollingListComponent> list = new ArrayList();
            if(it.hasNext()){
                String type = it.next();
                list.add(button.getButton("MailBoxSelectTypeGUI4ReplaceMail"+p.getName()+type, ox, oy, player -> {
                    SystemMail sm = mm.createSystemMail(type);
                    ReplaceMailGUI.open(ReplaceConfig.getConfig(sm, player));
                }, l -> l.replaceAll("%type%", mm.getSystemMailDisplay(type)), null));
            }
            return list;
        }));
        VexViewAPI.openGui(p, background.getVexGui(vcl));
    }
    
    public static void open4SystemList(Player p){
        List<SystemMail> sml = new ArrayList();
        MailManager.getMailManager().getSystemMailTypeList().forEach(sm -> {
            if(sm.autoCreateDatabaseTable()){
                sml.add(sm);
            }
        });
        Iterator<SystemMail> it = sml.iterator();
        List<VexComponents> vcl = Arrays.asList(list.getVexScrollingList(sml.size(), (ox, oy) -> {
            List<ScrollingListComponent> list = new ArrayList();
            if(it.hasNext()){
                SystemMail sm = it.next();
                list.add(button.getButton("MailBoxSelectTypeGUI4SystemList"+p.getName()+sm.getType(), ox, oy, player -> {
                    SystemListGUI.open(p, sm);
                }, l -> l.replaceAll("%type%", sm.getDisplay()), null));
            }
            return list;
        }));
        VexViewAPI.openGui(p, background.getVexGui(vcl));
    }
    
    public static boolean init(YamlConfiguration yml){
        try{
            background = VexViewUtil.getGuiPackage(yml.getConfigurationSection("gui"));
            list = VexViewUtil.getListPackage(yml.getConfigurationSection("list"));
            button = VexViewUtil.getButtonPackage(yml.getConfigurationSection("button"));
            return true;
        }catch(Exception ex){
            return false;
        }
    }
    
    
    
}
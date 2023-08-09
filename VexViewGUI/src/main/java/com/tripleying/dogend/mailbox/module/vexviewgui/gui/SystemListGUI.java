package com.tripleying.dogend.mailbox.module.vexviewgui.gui;

import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.api.mail.attach.AttachFile;
import com.tripleying.dogend.mailbox.api.mail.attach.AttachMoney;
import com.tripleying.dogend.mailbox.manager.MailManager;
import com.tripleying.dogend.mailbox.manager.MoneyManager;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.ButtonPackage;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.GuiPackage;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.ImagePackage;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.ItemListPackage;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.ListPackage;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.TextPackage;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.VexViewUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lk.vexview.api.VexViewAPI;
import lk.vexview.event.VexSlotClickEvent;
import lk.vexview.gui.OpenedVexGui;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.DynamicComponent;
import lk.vexview.gui.components.VexButton;
import lk.vexview.gui.components.VexEventCallable;
import lk.vexview.gui.components.VexSlot;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SystemListGUI extends VexGui implements VexEventCallable {
    
    private static GuiPackage background;
    private static ListPackage list_mail;
    private static int list_max;
    private static TextPackage empty_mail;
    private static TextPackage list_title;
    private static int list_title_max;
    private static TextPackage list_sender;
    private static ButtonPackage list_button;
    private static ButtonPackage list_pre;
    private static TextPackage list_page;
    private static ButtonPackage list_next;
    private static TextPackage mail_id;
    private static TextPackage mail_title;
    private static ListPackage mail_body;
    private static int mail_body_max;
    private static int mail_body_line;
    private static TextPackage mail_sender;
    private static ItemListPackage mail_item;
    private static ListPackage mail_money;
    private static TextPackage mail_money_text;
    private static Map<String, ImagePackage> mail_money_bg;
    private static ButtonPackage mail_delete;
    private final Set<DynamicComponent> dcl;
    private final Set<DynamicComponent> dcm;
    private boolean flag = true;
    private int list_page_now;
    private Player p;
    private SystemMail smt;
    
    public SystemListGUI(String img, int x, int y, int w, int h){
        super(img, x, y, w, h);
        dcl = new LinkedHashSet();
        dcm = new LinkedHashSet();
    }
    
    private void setPlayer(Player p){
        this.p = p;
    }
    
    private void setSystemMail(SystemMail smt){
        this.smt = smt;
    }
    
    public static void open(Player p, SystemMail smt){
        SystemListGUI gui = (SystemListGUI)background.getVexGui(SystemListGUI.class);
        if(gui!=null){
            gui.setPlayer(p);
            gui.setSystemMail(smt);
            p.closeInventory();
            VexViewAPI.openGui(p, gui);
        }
    }
    
    private List<DynamicComponent> getMailComponents4List(SystemMail sm, int ox, int oy){
        List<DynamicComponent> list = new ArrayList();
        if(sm!=null){
            list.add(list_button.getButton("MailBoxSystemListGUI"+p.getName()+sm.getType()+sm.getId(), ox, oy, p -> {
                changeSeeMail(sm);
            }, null, null));
            list.add(list_title.getVexText(ox, oy, t -> {
                t.replaceAll(s -> subMailTitle(s.replaceAll("%title%", sm.getTitle())));
                return t;
            }));
            list.add(list_sender.getVexText(ox, oy, t -> {
                t.replaceAll(s -> s.replaceAll("%sender%", sm.getSender()));
                return t;
            }));
        }
        return list;
    }
    
    public void changePage(int page){
        OpenedVexGui ogui = VexViewAPI.getPlayerCurrentGui(p);
        int wait = 0;
        while(ogui==null){
            if(wait==1000){
                return;
            }
            wait++;
            ogui = VexViewAPI.getPlayerCurrentGui(p);
        }
        if(!dcl.isEmpty()){
            for(DynamicComponent dc:dcl){
                ogui.removeDynamicComponent(dc);
            }
            dcl.clear();
        }
        List<DynamicComponent> list = new ArrayList();
        List<SystemMail> sml;
        int now;
        int all = MailManager.getMailManager().getSystemMailPages(smt, list_max);
        if(all==0){
            now = 0;
            sml = new ArrayList();
        }else{
            if(page>all){
                now = all;
            }else{
                now = page;
            }
            sml = MailManager.getMailManager().getSystemMailList(smt.getType(), list_max, now);
        }
        list_page_now = now;
        list.add(list_page.getVexText(0, 0, s -> {
            s.replaceAll(t -> t.replaceAll("%now%", now+"").replaceAll("%all%", all+""));
            return s;
        }));
        if(now>1){
            list.add(list_pre.getButton("MailBoxPersonListGUI"+p.getName()+"pre", 0, 0, p -> changePage(now-1), null, null));
        }
        if(now<all){
            list.add(list_next.getButton("MailBoxPersonListGUI"+p.getName()+"next", 0, 0, p -> changePage(now+1), null, null));
        }
        if(sml.isEmpty()){
            list.add(empty_mail.getVexText(0, 0, null));
        }else{
            Iterator<SystemMail> it = sml.iterator();
            list.addAll(list_mail.getDynamicComponentList(sml.size(), (ox, oy) -> getMailComponents4List(it.hasNext()?it.next():null, ox, oy)));
        }
        for(DynamicComponent dc:list){
            ogui.addDynamicComponent(dc);
            dcl.add(dc);
        };
    }
    
    private void clearMail(){
        OpenedVexGui ogui = VexViewAPI.getPlayerCurrentGui(p);
        if(!dcm.isEmpty()){
            for(DynamicComponent dc:dcm){
                ogui.removeDynamicComponent(dc);
            }
            dcm.clear();
        }
    }
    
    private void changeSeeMail(SystemMail sm){
        clearMail();
        OpenedVexGui ogui = VexViewAPI.getPlayerCurrentGui(p);
        List<DynamicComponent> cl = new ArrayList();
        cl.add(mail_id.getVexText(0, 0, t -> {
            t.replaceAll(s -> s.replaceAll("%id%", Long.toString(sm.getId())));
            return t;
        }));
        cl.add(mail_title.getVexText(0, 0, t -> {
            t.replaceAll(s -> s.replaceAll("%title%", sm.getTitle()));
            return t;
        }));
        cl.add(mail_sender.getVexText(0, 0, t -> {
            t.replaceAll(s -> s.replaceAll("%sender%", sm.getSender()));
            return t;
        }));
        cl.add(mail_body.getTextScrollingList(mail_body_max, mail_body_line, sm.getBody()));
        AttachFile af = sm.getAttachFile();
        if(af.hasAttach()){
            cl.addAll(mail_item.getItemDynamicComponentList(af.getItemStacks(), null));
            List<String> tl = new ArrayList();
            Map<String, AttachMoney> mm = af.getMoneys();
            mm.forEach((t,m) -> {
                if(mail_money_bg.containsKey(t)) tl.add(t);
            });
            Iterator<String> it = tl.iterator();
            cl.addAll(mail_money.getDynamicComponentList(tl.size(), (ox, oy) -> {
                List<DynamicComponent> list = new ArrayList();
                if(it.hasNext()){
                    String type = it.next();
                    String display = MoneyManager.getMoneyManager().getMoneyDisplay(type);
                    String count = mm.get(type).getCount().toString();
                    list.add(mail_money_text.getVexText(ox, oy, l -> {
                        l.replaceAll(s -> s.replaceAll("%type%", display).replaceAll("%money%", count));
                        return l;
                    }));
                    list.add(mail_money_bg.get(type).getVexImage(ox, oy));
                }
                return list;
            }));
        }
        cl.add(mail_delete.getButton("MailBoxPersonListGUI"+p.getName()+sm.getType()+sm.getId()+"delete", 0, 0, player -> {
            if(flag){
                flag = false;
                if(player.isOp()){
                    clearMail();
                    sm.deleteMail();
                    changePage(list_page_now);
                }
                flag = true;
            };
        }, null, null));
        cl.forEach(dc -> {
            ogui.addDynamicComponent(dc);
            dcm.add(dc);
        });
    }
    
    private String subMailTitle(String title){
        boolean suffix = false;
        if(title.length()>list_title_max){
            title = title.substring(0, list_title_max-1);
            suffix = true;
        }
        if('§'==title.charAt(title.length()-1)){
            title = title.substring(0, title.length()-1);
        }
        if(suffix) title += "...";
        return title;
    }
    
    public static boolean init(YamlConfiguration yml){
        try{
            background = VexViewUtil.getGuiPackage(yml.getConfigurationSection("gui"));
            list_mail = VexViewUtil.getListPackage(yml.getConfigurationSection("list"));
            list_max = yml.getInt("list.max");
            empty_mail = VexViewUtil.getTextPackage(yml.getConfigurationSection("list.empty"));
            list_title = VexViewUtil.getTextPackage(yml.getConfigurationSection("list.title"));
            list_title_max = yml.getInt("list.title.max");
            list_sender = VexViewUtil.getTextPackage(yml.getConfigurationSection("list.sender"));
            list_button = VexViewUtil.getButtonPackage(yml.getConfigurationSection("list.button"));
            list_pre = VexViewUtil.getButtonPackage(yml.getConfigurationSection("list.pre"));
            list_page = VexViewUtil.getTextPackage(yml.getConfigurationSection("list.page"));
            list_next = VexViewUtil.getButtonPackage(yml.getConfigurationSection("list.next"));
            mail_id = VexViewUtil.getTextPackage(yml.getConfigurationSection("mail.id"));
            mail_title = VexViewUtil.getTextPackage(yml.getConfigurationSection("mail.title"));
            mail_body = VexViewUtil.getListPackage(yml.getConfigurationSection("mail.body"));
            mail_body_max = yml.getInt("mail.body.max");
            mail_body_line = yml.getInt("mail.body.line");
            mail_sender = VexViewUtil.getTextPackage(yml.getConfigurationSection("mail.sender"));
            mail_item = VexViewUtil.getItemListPackage(yml.getConfigurationSection("mail.item"));
            mail_money = VexViewUtil.getListPackage(yml.getConfigurationSection("mail.money"));
            mail_money_text = VexViewUtil.getTextPackage(yml.getConfigurationSection("mail.money.text"));
            mail_money_bg = new HashMap();
            yml.getConfigurationSection("mail.money.bg").getKeys(false).forEach(k -> mail_money_bg.put(k, VexViewUtil.getImagePackage(yml.getConfigurationSection("mail.money.bg."+k))));
            mail_delete = VexViewUtil.getButtonPackage(yml.getConfigurationSection("mail.delete"));
            return true;
        }catch(Exception ex){
            return false;
        }
    }

    @Override
    public void onKeyPressed(Player player, int i, boolean bln) {
        
    }

    @Override
    public void onSlotClick(Player player, VexSlot vs, VexSlotClickEvent.ClickType ct) {
        
    }

    @Override
    public void onSlotInteractPre(Player player, int i, boolean bln) {
        
    }

    @Override
    public void onSlotInteractPost(Player player, int i, boolean bln) {
        
    }

    @Override
    public void onButtonClick(Player player, VexButton vb) {
        
    }

    @Override
    public void onGuiOpened(Player player) {
        changePage(1);
    }

    @Override
    public void onGuiClosed(Player player) {
        
    }
    
}

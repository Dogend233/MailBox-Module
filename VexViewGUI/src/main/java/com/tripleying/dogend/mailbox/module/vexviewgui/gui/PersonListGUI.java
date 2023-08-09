package com.tripleying.dogend.mailbox.module.vexviewgui.gui;

import com.tripleying.dogend.mailbox.api.mail.PersonMail;
import com.tripleying.dogend.mailbox.api.mail.attach.AttachFile;
import com.tripleying.dogend.mailbox.api.mail.attach.AttachMoney;
import com.tripleying.dogend.mailbox.manager.DataManager;
import com.tripleying.dogend.mailbox.manager.MailManager;
import com.tripleying.dogend.mailbox.manager.MoneyManager;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.*;
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
import lk.vexview.gui.components.ButtonFunction;
import lk.vexview.gui.components.DynamicComponent;
import lk.vexview.gui.components.VexButton;
import lk.vexview.gui.components.VexEventCallable;
import lk.vexview.gui.components.VexSlot;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PersonListGUI extends VexGui implements VexEventCallable {
    
    private static GuiPackage background;
    private static ListPackage list_mail;
    private static int list_max;
    private static TextPackage empty_mail;
    private static ButtonPackage list_tip;
    private static TextPackage list_title;
    private static int list_title_max;
    private static TextPackage list_sender;
    private static ButtonPackage list_button;
    private static ButtonPackage list_pre;
    private static TextPackage list_page;
    private static ButtonPackage list_next;
    private static ButtonPackage list_all_see;
    private static boolean list_all_see_enable;
    private static ButtonPackage list_all_receive;
    private static boolean list_all_receive_enable;
    private static ButtonPackage list_all_delete;
    private static boolean list_all_delete_enable;
    private static TextPackage mail_title;
    private static ListPackage mail_body;
    private static int mail_body_max;
    private static int mail_body_line;
    private static TextPackage mail_sender;
    private static ItemListPackage mail_item;
    private static ListPackage mail_money;
    private static TextPackage mail_money_text;
    private static Map<String, ImagePackage> mail_money_bg;
    private static ButtonPackage mail_receive;
    private static ButtonPackage mail_delete;
    private final Set<DynamicComponent> dcl;
    private final Set<DynamicComponent> dcm;
    private boolean flag = true;
    private int list_page_now;
    private Player p;
    
    
    public PersonListGUI(String img, int x, int y, int w, int h){
        super(img, x, y, w, h);
        dcl = new LinkedHashSet();
        dcm = new LinkedHashSet();
    }
    
    private void setPlayer(Player p){
        this.p = p;
    }
    
    public static void open(Player p){
        PersonListGUI gui = (PersonListGUI)background.getVexGui(PersonListGUI.class);
        if(gui!=null){
            gui.setPlayer(p);
            p.closeInventory();
            VexViewAPI.openGui(p, gui);
        }
    }
    
    private List<DynamicComponent> getMailComponents4List(PersonMail pm, int ox, int oy){
        List<DynamicComponent> list = new ArrayList();
        if(pm!=null){
            list.add(list_button.getButton("MailBoxPersonListGUI"+p.getName()+pm.getType()+pm.getId(), ox, oy, p -> {
                changeSeeMail(pm);
            }, null, null));
            if(!pm.isReceived()){
                list.add(list_tip.getButton("MailBoxPersonListGUI"+p.getName()+pm.getType()+pm.getId()+"tip", ox, oy, null, null, null));
            }
            list.add(list_title.getVexText(ox, oy, t -> {
                t.replaceAll(s -> subMailTitle(s.replaceAll("%title%", pm.getTitle())));
                return t;
            }));
            list.add(list_sender.getVexText(ox, oy, t -> {
                t.replaceAll(s -> s.replaceAll("%sender%", pm.getSender()));
                return t;
            }));
        }
        return list;
    }
    
    private void clearList(){
        OpenedVexGui ogui = VexViewAPI.getPlayerCurrentGui(p);
        if(!dcl.isEmpty()){
            for(DynamicComponent dc:dcl){
                ogui.removeDynamicComponent(dc);
            }
            dcl.clear();
        }
    }
    
    public void changePage(int page){
        clearList();
        OpenedVexGui ogui = VexViewAPI.getPlayerCurrentGui(p);
        int wait = 0;
        while(ogui==null){
            if(wait==1000){
                return;
            }
            wait++;
            ogui = VexViewAPI.getPlayerCurrentGui(p);
        }
        List<DynamicComponent> list = new ArrayList();
        List<PersonMail> pml;
        int now;
        int all;
        long count = DataManager.getDataManager().getPersonMailCount(p);
        if(count>0){
            long pagel = count/list_max;
            if(count%list_max!=0) pagel++;
            all = pagel>Integer.MAX_VALUE?Integer.MAX_VALUE:(int)pagel;
        }else{
            all = 0;
        }
        if(all==0){
            now = 0;
            pml = new ArrayList();
        }else{
            if(page>all){
                now = all;
            }else{
                now = page;
            }
            pml = MailManager.getMailManager().getPersonMailList(p, list_max, now);
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
        if(pml.isEmpty()){
            list.add(empty_mail.getVexText(0, 0, null));
        }else{
            Iterator<PersonMail> it = pml.iterator();
            list.addAll(list_mail.getDynamicComponentList(pml.size(), (ox, oy) -> getMailComponents4List(it.hasNext()?it.next():null, ox, oy)));
        }
        if(list_all_see_enable){
            list.add(list_all_see.getButton("MailBoxPersonListGUI"+p.getName()+"allsee", 0, 0, player -> {
                if(flag){
                    flag = false;
                    allSee();
                    flag = true;
                };
            }, null, null));
        }
        if(list_all_receive_enable){
            list.add(list_all_receive.getButton("MailBoxPersonListGUI"+p.getName()+"allreceive", 0, 0, player -> {
                if(flag){
                    flag = false;
                    allReceive();
                    flag = true;
                };
            }, null, null));
        }
        if(list_all_delete_enable){
            list.add(list_all_delete.getButton("MailBoxPersonListGUI"+p.getName()+"alldelete", 0, 0, player -> {
                if(flag){
                    flag = false;
                    allDelete();
                    flag = true;
                };
            }, null, null));
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
    
    private void changeSeeMail(PersonMail pm){
        clearMail();
        if(pm.seeMail()){
            changePage(list_page_now);
        }
        OpenedVexGui ogui = VexViewAPI.getPlayerCurrentGui(p);
        List<DynamicComponent> cl = new ArrayList();
        cl.add(mail_title.getVexText(0, 0, t -> {
            t.replaceAll(s -> s.replaceAll("%title%", pm.getTitle()));
            return t;
        }));
        cl.add(mail_sender.getVexText(0, 0, t -> {
            t.replaceAll(s -> s.replaceAll("%sender%", pm.getSender()));
            return t;
        }));
        cl.add(mail_body.getTextScrollingList(mail_body_max, mail_body_line, pm.getBody()));
        AttachFile af = pm.getAttachFile();
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
            if(!pm.isReceived()){
                VexButton vb = mail_receive.getButton("MailBoxPersonListGUI"+p.getName()+pm.getType()+pm.getId()+"receive", 0, 0, null, null, null);
                ButtonFunction bf = player -> {
                    if(flag){
                        flag = false;
                        if(!pm.isReceived()){
                            if(pm.receivedMail()){
                                changePage(list_page_now);
                                changeSeeMail(pm);
                            }
                        }
                        flag = true;
                    };
                };
                vb.setFunction(bf);
                cl.add(vb);
            }
        }
        if(pm.isReceived()){
            cl.add(mail_delete.getButton("MailBoxPersonListGUI"+p.getName()+pm.getType()+pm.getId()+"delete", 0, 0, player -> {
                if(flag){
                    flag = false;
                    clearMail();
                    pm.deleteMail();
                    changePage(list_page_now);
                    flag = true;
                };
            }, null, null));
        }
        cl.forEach(dc -> {
            ogui.addDynamicComponent(dc);
            dcm.add(dc);
        });
    }
    
    private void allSee(){
        setClosable(false);
        clearMail();
        clearList();
        List<PersonMail> pml = MailManager.getMailManager().getPersonMailList(p);
        for(PersonMail pm:pml){
            pm.seeMail();
        }
        changePage(list_page_now);
        setClosable(true);
    }
    
    private void allReceive(){
        setClosable(false);
        clearMail();
        clearList();
        List<PersonMail> pml = MailManager.getMailManager().getPersonMailList(p);
        for(PersonMail pm:pml){
            if(!pm.isReceived() && pm.getAttachFile().hasAttach()){
                if(!pm.receivedMail()){
                    break;
                }
            }
        }
        changePage(list_page_now);
        setClosable(true);
    }
    
    private void allDelete(){
        setClosable(false);
        clearMail();
        clearList();
        MailManager.getMailManager().clearPersonReceivedMail(p);
        changePage(list_page_now);
        setClosable(true);
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
            list_tip = VexViewUtil.getButtonPackage(yml.getConfigurationSection("list.tip"));
            list_title = VexViewUtil.getTextPackage(yml.getConfigurationSection("list.title"));
            list_title_max = yml.getInt("list.title.max");
            list_sender = VexViewUtil.getTextPackage(yml.getConfigurationSection("list.sender"));
            list_button = VexViewUtil.getButtonPackage(yml.getConfigurationSection("list.button"));
            list_pre = VexViewUtil.getButtonPackage(yml.getConfigurationSection("list.pre"));
            list_page = VexViewUtil.getTextPackage(yml.getConfigurationSection("list.page"));
            list_next = VexViewUtil.getButtonPackage(yml.getConfigurationSection("list.next"));
            list_all_see = VexViewUtil.getButtonPackage(yml.getConfigurationSection("list.all.see"));
            list_all_see_enable = yml.getBoolean("list.all.see.enable");
            list_all_receive = VexViewUtil.getButtonPackage(yml.getConfigurationSection("list.all.receive"));
            list_all_receive_enable = yml.getBoolean("list.all.receive.enable");
            list_all_delete = VexViewUtil.getButtonPackage(yml.getConfigurationSection("list.all.delete"));
            list_all_delete_enable = yml.getBoolean("list.all.delete.enable");
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
            mail_receive = VexViewUtil.getButtonPackage(yml.getConfigurationSection("mail.receive"));
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

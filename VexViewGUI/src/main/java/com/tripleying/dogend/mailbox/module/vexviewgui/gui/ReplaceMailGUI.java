package com.tripleying.dogend.mailbox.module.vexviewgui.gui;

import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.module.commonguiapi.gui.ReplaceConfig;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.ButtonPackage;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.InvGuiPackage;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.VexViewReplacePackage;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.VexViewUtil;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import lk.vexview.api.VexViewAPI;
import lk.vexview.event.VexSlotClickEvent;
import lk.vexview.gui.VexInventoryGui;
import lk.vexview.gui.components.VexButton;
import lk.vexview.gui.components.VexEventCallable;
import lk.vexview.gui.components.VexSlot;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ReplaceMailGUI extends VexInventoryGui implements VexEventCallable {
    
    private static InvGuiPackage background;
    private static ButtonPackage replace;
    private static ButtonPackage send;
    private static Map<String, VexViewReplacePackage> vvrpm;
    private final Map<String, Map<String, Integer>> mapstore;
    private ReplaceConfig rc;
    public boolean flag;
    public boolean replaceflag;
    
    public ReplaceMailGUI(String img, int x, int y, int w, int h, int iw, int ih){
        super(img, x, y, w, h, w, h, iw, ih);
        mapstore = new HashMap();
        flag = true;
        replaceflag = false;
    }
    
    private void setReplaceConfig(ReplaceConfig rc){
        this.rc = rc;
    }

    public Map<String, Map<String, Integer>> getMapstore() {
        return mapstore;
    }
    
    public void setButtonFlag(boolean flag){
        this.flag = flag;
    }
    
    public boolean getButtonFlage(){
        return flag;
    }
    
    public void setReplaceFlag(){
        replaceflag = true;
    }
    
    public static void open(ReplaceConfig rc){
        ReplaceMailGUI gui = (ReplaceMailGUI)background.getVexInventoryGui(ReplaceMailGUI.class);
        if(gui!=null){
            gui.setClosable(false);
            gui.setReplaceConfig(rc);
            Player p = rc.getPlayer();
            VexViewReplacePackage vvrp = gui.getVVRePackage();
            gui.addAllComponents(vvrp.getVexComponents(rc, gui.getMapstore()));
            gui.addComponent(replace.getButton("MailBoxReplaceMailGUI"+p.getName()+"replace", 0, 0, player -> {
                if(gui.getButtonFlage()){
                    gui.setButtonFlag(false);
                    vvrp.replaceMail(rc, gui.getMapstore());
                    gui.setReplaceFlag();
                    ReplaceMailGUI.open(rc);
                    gui.setButtonFlag(true);
                }
            }, null, null));
            gui.addComponent(send.getButton("MailBoxReplaceMailGUI"+p.getName()+"send", 0, 0, player -> {
                if(gui.getButtonFlage()){
                    gui.setButtonFlag(false);
                    player.closeInventory();
                    vvrp.replaceMail(rc, gui.getMapstore());
                    SystemMail sm = rc.getSystemMail().sendMail();
                    if(sm.getId()==0){
                        MessageUtil.error(p, "邮件发送失败!");
                    }else{
                        MessageUtil.log(p, "邮件发送成功!");
                    }
                    gui.setButtonFlag(true);
                }
            }, null, null));
            p.closeInventory();
            VexViewAPI.openGui(p, gui);
        }
    }
    
    public VexViewReplacePackage getVVRePackage(){
        String type = rc.getSystemMail().getType();
        VexViewReplacePackage vvrp;
        if(vvrpm.containsKey(type)){
            vvrp = vvrpm.get(type);
        }else{
            vvrp = new VexViewReplacePackage(rc.getReplaceMap());
            vvrpm.put(type, vvrp);
        }
        return vvrp;
    }
    
    public static boolean init(YamlConfiguration yml){
        vvrpm = new LinkedHashMap();
        try{
            background = VexViewUtil.getInvGuiPackage(yml.getConfigurationSection("gui"));
            replace = VexViewUtil.getButtonPackage(yml.getConfigurationSection("modify"));
            send = VexViewUtil.getButtonPackage(yml.getConfigurationSection("send"));
            return true;
        }catch(Exception ex){
            return false;
        }
    }

    @Override
    public void onKeyPressed(Player player, int i, boolean bln) {
        if(i==1){
            player.closeInventory();
        }
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
        
    }

    @Override
    public void onGuiClosed(Player player) {
        if(!replaceflag){
            ArrayList<ItemStack> lis = new ArrayList();
            this.getComponents().stream().filter(c -> c instanceof VexSlot).filter(s -> ((VexSlot)s).getItem()!=null).forEach(i -> lis.add(((VexSlot)i).getItem()));
            ItemStack[] is = new ItemStack[lis.size()];
            for(int i=0;i<is.length;i++){
                is[i] = lis.get(i);
            }
            player.getInventory().addItem(is);
        }
    }
    
}

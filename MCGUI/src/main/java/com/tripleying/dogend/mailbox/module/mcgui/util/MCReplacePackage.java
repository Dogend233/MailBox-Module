package com.tripleying.dogend.mailbox.module.mcgui.util;

import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.module.commonguiapi.gui.ReplaceConfig;
import com.tripleying.dogend.mailbox.module.commonguiapi.gui.ReplacePackage;
import com.tripleying.dogend.mailbox.module.commonguiapi.gui.ReplaceUtil;
import com.tripleying.dogend.mailbox.module.mcgui.MCGUI;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MCReplacePackage {
    
    private static Map<String, MCReplacePackage> mcrpm;
    private Map<String, ReplacePackage> rpm;
    
    static {
        mcrpm = new HashMap();
    }
    
    public MCReplacePackage(Map<String, ReplacePackage> rpm){
        this.rpm = rpm;
    }
    
    public static MCReplacePackage getMCReplacePackage(ReplaceConfig rc){
        String type = rc.getSystemMail().getType();
        MCReplacePackage mcrp;
        if(mcrpm.containsKey(type)){
            mcrp = mcrpm.get(type);
        }else{
            mcrp = new MCReplacePackage(rc.getReplaceMap());
            mcrpm.put(type, mcrp);
        }
        return mcrp;
        
    }
    
    public void replaceMethod(String type, ReplaceConfig rc){
            switch(rpm.get(type).getType()){
                case Integer:
                    MessageUtil.log(rc.getPlayer(), "请在下方输入一个整数:");
                    MCGUI.getMCGUI().getListener().addPlayer(rc.getPlayer(), (msg) -> {
                        rc.replaceValue(type, ReplaceUtil.parseInteger(msg.trim()));
                        MCGUI.getMCGUI().openPreviewMailGUI(rc, this);
                    });
                    break;
                case Double:
                    MessageUtil.log(rc.getPlayer(), "请在下方输入一个小数:");
                    MCGUI.getMCGUI().getListener().addPlayer(rc.getPlayer(), (msg) -> {
                        rc.replaceValue(type, ReplaceUtil.parseDouble(msg.trim()));
                        MCGUI.getMCGUI().openPreviewMailGUI(rc, this);
                    });
                    break;
                case DateTime:
                    MessageUtil.log(rc.getPlayer(), "请在下方输入一个时间(例如 2022-1-1 12:00:00 或 2022-1-1):");
                    MCGUI.getMCGUI().getListener().addPlayer(rc.getPlayer(), (msg) -> {
                        rc.replaceValue(type, ReplaceUtil.parseTime(msg.trim()));
                        MCGUI.getMCGUI().openPreviewMailGUI(rc, this);
                    });
                    break;
                case String:
                    MessageUtil.log(rc.getPlayer(), "请在下方输入你想要的文字:");
                    MCGUI.getMCGUI().getListener().addPlayer(rc.getPlayer(), (msg) -> {
                        rc.replaceValue(type, msg);
                        MCGUI.getMCGUI().openPreviewMailGUI(rc, this);
                    });
                    break;
                case Boolean:
                    MessageUtil.log(rc.getPlayer(), "请在下方输入一个布尔值(true 或 false):");
                    MCGUI.getMCGUI().getListener().addPlayer(rc.getPlayer(), (msg) -> {
                        rc.replaceValue(type, ReplaceUtil.parseBoolean(msg.trim()));
                        MCGUI.getMCGUI().openPreviewMailGUI(rc, this);
                    });
                    break;
                case StringList:
                    MCGUI.getMCGUI().openReplaceListGUI(rc, this, type);
                    break;
                case ItemStackList:
                    MCGUI.getMCGUI().openReplaceItemGUI(rc, this, type);
                    break;
            }
    }
    
    public void addList(String type, String value, SystemMail sm){
        ReplacePackage rp = rpm.getOrDefault(type, null);
        if(rp!=null){
            List<String> list = (List<String>)rp.getValue(sm);
            list.add(value);
            rp.ReplaceValue(sm, list);
        }
    }
    
    public void removeList(String type, String value, SystemMail sm){
        ReplacePackage rp = rpm.getOrDefault(type, null);
        if(rp!=null){
            List<String> list = (List<String>)rp.getValue(sm);
            list.remove(value);
            rp.ReplaceValue(sm, list);
        }
    }
    
}

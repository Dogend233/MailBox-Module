package com.tripleying.dogend.mailbox.module.allmail.gui;

import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.module.allmail.mail.AllSystemMail;
import com.tripleying.dogend.mailbox.module.commonguiapi.gui.ReplaceConfig;
import com.tripleying.dogend.mailbox.module.commonguiapi.gui.ReplacePackage;
import com.tripleying.dogend.mailbox.module.commonguiapi.gui.ReplaceType;
import com.tripleying.dogend.mailbox.module.commonguiapi.gui.ReplaceUtil;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AllMailConfig extends ReplaceConfig {
    
    private static final Map<String, ReplacePackage> rmap;
    
    static {
        rmap = new LinkedHashMap();
        rmap.put("title", new ReplacePackage(
                "title",
                "邮件标题",
                ReplaceType.String,
                sm -> sm.getTitle(),
                (sm, o) -> sm.setTitle((String)o))
        );
        rmap.put("body", new ReplacePackage(
                "body",
                "邮件内容",
                ReplaceType.StringList,
                sm -> sm.getBody(),
                (sm, o) -> sm.setBody((List<String>)o))
        );
        rmap.put("sender", new ReplacePackage(
                "sender",
                "发件人",
                ReplaceType.String,
                sm -> sm.getSender(),
                (sm, o) -> sm.setSender((String)o))
        );
        rmap.put("command", new ReplacePackage(
                "command",
                "附件指令",
                ReplaceType.StringList,
                sm -> ReplaceUtil.command2String(sm.getAttachFile().getCommands()),
                (sm, o) -> ReplaceUtil.parseCommand(sm.getAttachFile().getCommands(), (List<String>)o))
        );
        rmap.put("money", new ReplacePackage(
                "money",
                "附件金钱",
                ReplaceType.StringList,
                sm -> ReplaceUtil.money2String(sm.getAttachFile()),
                (sm, o) -> ReplaceUtil.parseMoney(sm.getAttachFile(), (List<String>)o))
        );
        rmap.put("itemstack", new ReplacePackage(
                "itemstack",
                "附件物品",
                ReplaceType.ItemStackList,
                sm -> sm.getAttachFile().getItemStacks(),
                (sm, o) -> ReplaceUtil.parseItemStack(sm.getAttachFile(), (List<ItemStack>)o))
        );
    }
    
    public AllMailConfig(SystemMail sm, Player p){
        super(sm, p);
    }

    @Override
    public Map<String, ReplacePackage> getReplaceMap() {
        return rmap;
    }

    @Override
    public void replaceValue(String type, Object o) {
        if(rmap.containsKey(type)){
            rmap.get(type).ReplaceValue(sm, o);
        }
    }
    
    public static void registerConfig(){
        ReplaceConfig.registerConfig(AllSystemMail.class, AllMailConfig.class);
    }
    
    public static void unregisterConfig(){
        ReplaceConfig.unregisterConfig(AllSystemMail.class);
    }
    
}
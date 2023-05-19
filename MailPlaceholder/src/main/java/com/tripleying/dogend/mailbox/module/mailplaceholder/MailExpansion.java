package com.tripleying.dogend.mailbox.module.mailplaceholder;

import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class MailExpansion extends PlaceholderExpansion {
    
    private static MailExpansion me;
    private final MailPlaceholder mp;
    private final Map<String, Function<Player, String>> pm;
    private final Map<String, Supplier<String>> sm;
    
    public MailExpansion(MailPlaceholder mp){
        me = this;
        this.mp = mp;
        pm = new HashMap();
        sm = new HashMap();
    }

    @Override
    public String getIdentifier() {
        return "mailbox";
    }
    
    @Override
    public String onPlaceholderRequest(Player p, String args){
        if(args.startsWith("player_") && p!=null){
            Function<Player, String> fun = pm.getOrDefault(args, null);
            if(fun!=null) return fun.apply(p);
        }
        if(args.startsWith("server_")){
            Supplier<String> sup = sm.getOrDefault(args, null);
            if(sup!=null) return sup.get();
        }
        return "";
    }

    @Override
    public String getAuthor() {
        return mp.getInfo().getAuthors().get(0);
    }

    @Override
    public String getVersion() {
        return mp.getInfo().getVersion().toString();
    }
    
    @Override
    public boolean persist(){
        return true;
    }
    
    @Override
    public boolean canRegister(){
        return true;
    }
    
    public boolean registerPlayerPlaceholder(String key, Function<Player, String> value){
        if(pm.containsKey(key)){
            return false;
        }else{
            pm.put(key, value);
            MessageUtil.log(MessageUtil.color("&6MailPlaceholder: &a注册变量: &b".concat("mailbox_").concat(key)));
            return true;
        }
    }
    
    public void unregisterPlayerPlaceholder(String key){
        if(pm.containsKey(key)){
            pm.remove(key);
            MessageUtil.error(MessageUtil.color("&6MailPlaceholder: &c注销变量: &b".concat("mailbox_").concat(key)));
        }
    }
    
    public boolean registerServerPlaceholder(String key, Supplier<String> value){
        if(sm.containsKey(key)){
            return false;
        }else{
            sm.put(key, value);
            MessageUtil.log(MessageUtil.color("&6MailPlaceholder: &a注册变量: &b".concat("mailbox_").concat(key)));
            return true;
        }
    }
    
    public void unregisterServerPlaceholder(String key){
        if(sm.containsKey(key)){
            sm.remove(key);
            MessageUtil.error(MessageUtil.color("&6MailPlaceholder: &c注销变量: &b".concat("mailbox_").concat(key)));
        }
    }
    
    public static MailExpansion getMailExpansion(){
        return me;
    }
    
}

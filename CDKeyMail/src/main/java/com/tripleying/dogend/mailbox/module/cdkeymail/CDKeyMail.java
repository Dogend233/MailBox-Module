package com.tripleying.dogend.mailbox.module.cdkeymail;

import com.tripleying.dogend.mailbox.api.command.BaseCommand;
import com.tripleying.dogend.mailbox.api.command.BaseTabCompleter;
import com.tripleying.dogend.mailbox.api.event.MailBoxLoadFinishEvent;
import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.api.module.MailBoxModule;
import com.tripleying.dogend.mailbox.manager.MailManager;
import com.tripleying.dogend.mailbox.manager.ModuleManager;
import com.tripleying.dogend.mailbox.module.cdkeymail.gui.CDKeyMailConfig;
import com.tripleying.dogend.mailbox.module.cdkeymail.data.CDKeyCollectedData;
import com.tripleying.dogend.mailbox.module.cdkeymail.data.CDKeyData;
import com.tripleying.dogend.mailbox.module.cdkeymail.mail.CDKeySystemMail;
import com.tripleying.dogend.mailbox.module.cdkeymail.placeholder.CDKeyPlaceHolder;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CDKeyMail extends MailBoxModule implements BaseCommand, BaseTabCompleter, Listener {
    
    private static CDKeyMail main;
    private String type;
    private String display;
    private String cmd;
    private long lastTime;
    private int input_every_day;
    private long input_every_times;
    private String export_repeat;
    private String export_norepeat;
    private Map<String, String> message;
    private List<String> cdking;
    private final Map<UUID, Integer> count_map = new HashMap();
    private final Map<UUID, Long> time_map = new HashMap();

    @Override
    public void onEnable() {
        main = this;
        this.saveDefaultConfig();
        this.reloadConfig();
        YamlConfiguration config = this.getConfig();
        cmd = config.getString("command", "cdkey");
        type = config.getString("type", "cdkey");
        display = MessageUtil.color(config.getString("display", "&6兑换码"));
        input_every_day = config.getInt("input_every_day", 5);
        input_every_times = config.getLong("input_every_times", 5)*1000;
        export_repeat = config.getString("export.repeat", "可复用 - %cdk%");
        export_norepeat = config.getString("export.norepeat", "一次性 - %cdk%");
        message = new HashMap();
        config.getConfigurationSection("message").getValues(false).forEach((k,v) -> message.put(k,MessageUtil.color(v.toString()).replaceAll("%cmd%", cmd)));
        lastTime = System.currentTimeMillis();
        cdking = new ArrayList();
        this.registerCommand(this);
        this.registerListener(this);
        this.registerSystemMail(new CDKeySystemMail());
        if(ModuleManager.getModuleManager().hasModule("CommonGUIAPI")){
            CDKeyMailConfig.registerConfig();
        }
        if(ModuleManager.getModuleManager().hasModule("MailPlaceholder")){
            CDKeyPlaceHolder.registerPlaceHolder();
        }
    }
    
    @EventHandler
    public void onMailBoxLoadFinish(MailBoxLoadFinishEvent evt){
        new CDKeyData().createStorage();
        new CDKeyCollectedData().createStorage();
    }

    @Override
    public void onDisable(){
        this.unregisterAllCommand();
        this.unregisterAllListener();
        this.unregisterAllSystemMail();
        if(ModuleManager.getModuleManager().hasModule("CommonGUIAPI")){
            CDKeyMailConfig.unregisterConfig();
        }
        if(ModuleManager.getModuleManager().hasModule("MailPlaceholder")){
            CDKeyPlaceHolder.unregisterPlaceHolder();
        }
    }

    @Override
    public String getLabel() {
        return this.cmd;
    }

    @Override
    public String getDescription(CommandSender sender) {
        return sender.isOp()
                ?"使用/生成/导出兑换码"
                :"使用兑换码";
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if(args.length>1){
            switch(args[1]){
                case "list":
                    if(sender.isOp()){
                        try{
                            int lpage = 1;
                            if(args.length>2){
                                lpage = Integer.parseInt(args[2]);
                            }
                            List<SystemMail> sml = MailManager.getMailManager().getSystemMailList(type, 10, lpage);
                            if(sml.isEmpty()){
                                sender.sendMessage(message.get("list_no_mail"));
                            }else{
                                
                                sml.forEach(sm -> {
                                    sender.sendMessage(message.get("list_mail").replaceAll("%id%", Long.toString(sm.getId())).replaceAll("%title%", sm.getTitle()));
                                });
                            }
                        }catch(Exception e){
                            sender.sendMessage(message.get("number_error"));
                        }
                    }
                    break;
                case "create":
                    if(sender.isOp() && args.length>2){
                        try{
                            long cid = Long.parseLong(args[2]);
                            long ccount = 1;
                            if(args.length>3){
                                ccount = Long.parseLong(args[3]);
                            }
                            List<CDKeyData> list = new ArrayList();
                            for(int i=0;i<ccount;i++){
                                list.add(new CDKeyData(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase(), cid, false));
                            }
                            List<CDKeyData> fail = new ArrayList();
                            list.forEach(cdk -> {
                                if(cdk.insertCustomData()){
                                    sender.sendMessage(message.get("create_cdkey").replaceAll("%cdk%", cdk.getCDKey()));
                                }else{
                                    fail.add(cdk);
                                }
                            });
                            sender.sendMessage(message.get("error_cdkey").replaceAll("%count%", Integer.toString(fail.size())));
                        }catch(Exception e){
                            sender.sendMessage(message.get("number_error"));
                        }
                    }
                    break;
                case "repeat":
                    if(sender.isOp() && args.length>2){
                        try{
                            long rid = Long.parseLong(args[2]);
                            long rcount = 1;
                            if(args.length>3){
                                rcount = Long.parseLong(args[3]);
                            }
                            List<CDKeyData> list = new ArrayList();
                            for(int i=0;i<rcount;i++){
                                list.add(new CDKeyData(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase(), rid, true));
                            }
                            List<CDKeyData> fail = new ArrayList();
                            list.forEach(cdk -> {
                                if(cdk.insertCustomData()){
                                    sender.sendMessage(message.get("create_cdkey").replaceAll("%cdk%", cdk.getCDKey()));
                                }else{
                                    fail.add(cdk);
                                }
                            });
                            sender.sendMessage(message.get("error_cdkey").replaceAll("%count%", Integer.toString(fail.size())));
                        }catch(Exception e){
                            sender.sendMessage(message.get("number_error"));
                        }
                    }
                    break;
                case "export":
                    if(sender.isOp() && args.length>2){
                        try{
                            long eid = Long.parseLong(args[2]);
                            List<CDKeyData> cdkList = CDKeyData.getUnusedCDKey(eid);
                            if(cdkList.isEmpty()){
                                sender.sendMessage(message.get("mail_no_cdkey").replaceAll("%id%", Long.toString(eid)));
                            }else{
                                File file = exportCdkey(eid, cdkList);
                                if(file!=null && file.exists()){
                                    sender.sendMessage(message.get("export_success").replaceAll("%file%", file.getAbsolutePath()));
                                }else{
                                    sender.sendMessage(message.get("export_fail"));
                                }
                            }
                        }catch(Exception e){
                            sender.sendMessage(message.get("number_error"));
                        }
                    }
                    break;
                case "help":
                    help(sender);
                    break;
                default:
                    if(args[1].trim().length()==32){
                        if(sender instanceof Player){
                            updateLastTime();
                            Player p = (Player)sender;
                            UUID uuid = p.getUniqueId();
                            if(count_map.getOrDefault(uuid, 0)>=input_every_day){
                                sender.sendMessage(message.get("try_error"));
                                break;
                            }
                            long now = System.currentTimeMillis();
                            if(time_map.containsKey(uuid)){
                                Long last = time_map.get(uuid);
                                if((now-last)<input_every_times){
                                    sender.sendMessage(message.get("try_fast"));
                                    break;
                                }
                            }
                            time_map.put(uuid, now);
                            String cdk = args[1].trim().toUpperCase();
                            CDKeyData cdkey = CDKeyData.getCDKeyData(cdk);
                            if(cdkey==null){
                                errorCount(uuid);
                                sender.sendMessage(message.get("cdk_absent"));
                                break;
                            }
                            if(cdkey.isUsed() || CDKeyCollectedData.isUsedCDKye(p, cdk)){
                                errorCount(uuid);
                                sender.sendMessage(message.get("cdk_used"));
                            }else if(!cdkey.isRepeat() && cdking.contains(cdk)){
                                sender.sendMessage(message.get("cdk_using"));
                            }else{
                                if(!cdkey.isRepeat()){
                                    cdking.add(cdk);
                                }
                                CDKeySystemMail mail = cdkey.getMail();
                                if(mail==null){
                                    sender.sendMessage(message.get("cdk_no_mail").replaceAll("%id%", Long.toString(cdkey.getMailId())));
                                }else{
                                    if(CDKeyCollectedData.couldUseCDKey(p, cdkey)){
                                        if(cdkey.sendMail(p)){
                                            sender.sendMessage(message.get("cdk_success"));
                                        }else{
                                            sender.sendMessage(message.get("cdk_fail"));
                                        }
                                    }else{
                                        sender.sendMessage(message.get("cdk_type"));
                                    }
                                }
                                cdking.remove(cdk);
                                break;
                            }
                        }else{
                            sender.sendMessage(message.get("console_error"));
                            break;
                        }
                    }else{
                        help(sender);
                        break;
                    }
            }
        }else{
            help(sender);
        }
        return true;
    }
    
    public int getPlayerInputDay(Player p){
        return this.count_map.getOrDefault(p.getUniqueId(), 0);
    }
    
    private File exportCdkey(long id, List<CDKeyData> list){
        try {
            File folder = new File(getDataFolder(), "export");
            if(!folder.exists()) folder.mkdirs();
            File file = new File(folder, id+".txt");
            if(!file.exists()) file.createNewFile();
            try (FileOutputStream fs = new FileOutputStream(file);
                PrintStream p = new PrintStream(fs)) {
                list.forEach(cdk -> {
                    p.println((cdk.isRepeat()?export_repeat:export_norepeat).replaceAll("%cdk%", cdk.getCDKey()));
                });
            }
            return file;
        }   catch (IOException ex) {
            return null;
        }
    }
    
    private void updateLastTime(){
        long newTime = System.currentTimeMillis();
        if(newTime>lastTime){
            count_map.clear();
            time_map.clear();
        }
        long day = 86400000L;
        lastTime = System.currentTimeMillis()/day*day+day;
    }
    
    private void errorCount(UUID uuid){
        Integer count = count_map.getOrDefault(uuid, 0)+1;
        count_map.put(uuid, count);
    }
    
    public void help(CommandSender sender){
        if(sender.isOp()){
            sender.sendMessage(message.get("help_admin"));
        }else{
            sender.sendMessage(message.get("help"));
        }
    }

    @Override
    public boolean allowTab(CommandSender sender) {
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        switch (args.length) {
            case 2:
                if(args[1].length()==0){
                    if(sender.isOp()){
                        return Arrays.asList("list","create","repeat","export");
                    }else{
                        return Arrays.asList("help");
                    }
                }else{
                    if("help".startsWith(args[1])){
                        return Arrays.asList("help");
                    }
                    if("list".startsWith(args[1])){
                        return Arrays.asList("list");
                    }
                    if("create".startsWith(args[1])){
                        return Arrays.asList("create");
                    }
                    if("repeat".startsWith(args[1])){
                        return Arrays.asList("repeat");
                    }
                    if("export".startsWith(args[1])){
                        return Arrays.asList("export");
                    }
                }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public String getDisplay() {
        return display;
    }

    public int getInput_every_day() {
        return input_every_day;
    }

    public long getInput_every_times() {
        return input_every_times;
    }

    public static CDKeyMail getMain() {
        return main;
    }
    
}
package com.tripleying.dogend.mailbox.module.vexviewcdkeygui.command;

import com.tripleying.dogend.mailbox.module.vexviewcdkeygui.VexViewCDKeyGUI;
import java.util.HashMap;
import java.util.Map;

public enum CDKeyCommand {
    
    USE("%cdkey%"),
    CREATE("create %id%"),
    REPEAT("repeat %id%"),
    EXPORT("export %id%");
    
    private final String content;
    private final Map<String, String> args;
    
    CDKeyCommand(String cmd){
        args = new HashMap();
        content = "mailbox %cmd% ".concat(cmd);
    }
    
    public CDKeyCommand addArg(String key, String value){
        args.put(key, value);
        return this;
    }
    
    public CDKeyCommand removeArg(String key){
        args.remove(key);
        return this;
    }
    
    public String getCommandString(){
        String cmd = content.replace("%cmd%", getCDKeyMailLabel());
        for(Map.Entry<String, String> entry:args.entrySet()){
            cmd = cmd.replace("%"+entry.getKey()+"%", entry.getValue());
        }
        return cmd;
    }
    
    private static String getCDKeyMailLabel(){
        return VexViewCDKeyGUI.getCDKeyMailLabel();
    }
    
}

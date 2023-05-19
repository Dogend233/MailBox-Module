package com.tripleying.dogend.module.playermail;

import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerOption {
    
    private final boolean offline;
    private final int expire;
    private final boolean reattach;
    private final int max;
    
    public PlayerOption(YamlConfiguration yml){
        offline = yml.getBoolean("offline");
        expire = yml.getInt("expire");
        reattach = yml.getBoolean("reattach");
        max = yml.getInt("max");
    }
    
}

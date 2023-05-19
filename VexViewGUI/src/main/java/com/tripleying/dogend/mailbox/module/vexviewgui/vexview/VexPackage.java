package com.tripleying.dogend.mailbox.module.vexviewgui.vexview;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class VexPackage {
    
    /**
     * 克隆一个String列表
     * @param list List
     * @return List
     */
    protected List<String> cloneList(List<String> list){
        List<String> clone = new ArrayList();
        clone.addAll(list);
        return clone;
    }
    
    public static ConfigurationSection createConfigurationSection(){
        return new YamlConfiguration();
    };
    
}

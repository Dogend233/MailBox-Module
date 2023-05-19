package com.tripleying.dogend.mailbox.module.commonguiapi.gui;

import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 * 替换配置类
 * @author Dogend
 */
public abstract class ReplaceConfig {
    
    private static Map<Class<? extends SystemMail>, Class<? extends ReplaceConfig>> cfgs;
    private static String register;
    private static String unregister;
    
    protected final SystemMail sm;
    protected final Player p;
    
    /**
     * 构造方法, 继承类必须有一个与此相同参数的构造方法
     * @param sm 系统邮件
     * @param p 玩家
     */
    protected ReplaceConfig(SystemMail sm, Player p){
        this.sm = sm;
        this.p = p;
    }
    
    /**
     * 获取需要替换的替换包
     * @return Map
     */
    public abstract Map<String, ReplacePackage> getReplaceMap();
    
    /**
     * 替换某个属性的值
     * @param type 属性名
     * @param o 值
     */
    public abstract void replaceValue(String type, Object o);
    
    /**
     * 获取某个属性的替换包
     * @param type 属性
     * @return ReplacePackage
     */
    public ReplacePackage getValue(String type){
        return getReplaceMap().getOrDefault(type, null);
    }
    
    /**
     * 获取系统邮件
     * @return SystemMail
     */
    public SystemMail getSystemMail() {
        return sm;
    }
    
    /**
     * 获取玩家
     * @return Player
     */
    public Player getPlayer() {
        return p;
    }
    
    /**
     * 注册配置
     * @param sm 系统邮件类
     * @param rc 替换配置类
     */
    public static void registerConfig(Class<? extends SystemMail> sm, Class<? extends ReplaceConfig> rc){
        cfgs.put(sm, rc);
        MessageUtil.log(register.replace("%mail%", sm.getName()));
    }
    
    /**
     * 注销配置
     * @param sm 系统邮件类 
     */
    public static void unregisterConfig(Class<? extends SystemMail> sm){
        if(cfgs.containsKey(sm)){
            cfgs.remove(sm);
            MessageUtil.log(unregister.replace("%mail%", sm.getName()));
        }
    }
    
    /**
     * 创建一个替换配置
     * @param sm 系统邮件
     * @param p 玩家
     * @return ReplaceConfig
     */
    public static ReplaceConfig getConfig(SystemMail sm, Player p){
        try {
            Class<? extends ReplaceConfig> rc = cfgs.getOrDefault(sm.getClass(), null);
            if(rc!=null){
                Constructor<? extends ReplaceConfig> constructor = rc.getConstructor(SystemMail.class, Player.class);
                return constructor.newInstance(sm, p);
            }
        }catch(Exception ex){}
        return null;
    }
    
    /**
     * 初始化
     * @param yml Yaml
     */
    public static void init(YamlConfiguration yml){
        cfgs = new LinkedHashMap();
        register = MessageUtil.color(yml.getString("register"));
        unregister = MessageUtil.color(yml.getString("unregister"));
    }
    
}
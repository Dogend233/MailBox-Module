package com.tripleying.dogend.mailbox.module.commonguiapi.gui;

import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 替换包类
 * @author Dogend
 */
public class ReplacePackage {
    
    // 替换属性名
    private final String name;
    // 描述
    private final String description;
    // 属性的值类型
    private final ReplaceType type;
    // 获取值的方法
    private final Function<SystemMail, Object> getValue;
    // 替换值的方法
    private final BiConsumer<SystemMail, Object> replace;

    public ReplacePackage(String name, String description, ReplaceType type, Function<SystemMail, Object> getValue, BiConsumer<SystemMail, Object> replace ) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.getValue = getValue;
        this.replace = replace;
    }
    
    /**
     * 获取属性名
     * @return String
     */
    public String getName() {
        return name;
    }
    
    /**
     * 获取属性描述
     * @return String
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * 获取值类型
     * @return ReplaceType
     */
    public ReplaceType getType() {
        return type;
    }
    
    /**
     * 获取属性值
     * @param sm 系统邮件
     * @return Object
     */
    public Object getValue(SystemMail sm) {
        return getValue.apply(sm);
    }
    
    /**
     * 替换属性值
     * @param sm 系统邮件
     * @param o 值
     */
    public void ReplaceValue(SystemMail sm, Object o){
        replace.accept(sm, o);
    }
    
}
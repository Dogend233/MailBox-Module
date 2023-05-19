package com.tripleying.dogend.mailbox.module.commonguiapi.gui;

import com.tripleying.dogend.mailbox.api.mail.attach.AttachCommand;
import com.tripleying.dogend.mailbox.api.mail.attach.AttachDoubleMoney;
import com.tripleying.dogend.mailbox.api.mail.attach.AttachFile;
import com.tripleying.dogend.mailbox.api.mail.attach.AttachIntegerMoney;
import com.tripleying.dogend.mailbox.api.mail.attach.AttachMoney;
import com.tripleying.dogend.mailbox.manager.MoneyManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.inventory.ItemStack;

/**
 * 替换工具类
 * @author Dogend
 */
public class ReplaceUtil {
    
    private static final SimpleDateFormat sdf1;
    private static final SimpleDateFormat sdf2;
    
    static{
        sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    }
    
    /**
     * 将字符串转换为int, 失败返回null
     * @param str 字符串
     * @return int
     */
    public static Integer parseInteger(String str){
        Integer result = null;
        try{
            result = Integer.parseInt(str);
        }catch(NumberFormatException ex){}
        return result;
    }
    
    /**
     * 将字符串转换为double, 失败返回null
     * @param str 字符串
     * @return double
     */
    public static Double parseDouble(String str){
        Double result = null;
        try{
            result = Double.parseDouble(str);
        }catch(NumberFormatException ex){}
        return result;
    }
    
    /**
     * 将字符串转换为boolean, 失败返回null
     * @param str 字符串
     * @return boolean
     */
    public static Boolean parseBoolean(String str){
        Boolean result = null;
        try{
            result = Boolean.parseBoolean(str);
        }catch(NumberFormatException ex){}
        return result;
    }
    
    /**
     * 将物品列表设置为附件物品
     * @param af 附件
     * @param isl 物品列表
     */
    public static void parseItemStack(AttachFile af, List<ItemStack> isl){
        af.removeAllItemStack();
        isl.forEach(is -> af.addItemStack(is));
    }
    
    /**
     * 将指令列表设置为附件指令
     * @param ac 附件指令
     * @param list 指令列表
     */
    public static void parseCommand(AttachCommand ac, List<String> list){
        ac.clearConsoleCommand();
        ac.clearPlayerCommand();
        ac.clearOPCommand();
        list.forEach(c -> {
            if(c.startsWith("op:")){
                ac.addOPCommand(c.substring(3));
                return;
            }
            if(c.startsWith("player:")){
                ac.addPlayerCommand(c.substring(7));
                return;
            }
            if(c.startsWith("p:")){
                ac.addPlayerCommand(c.substring(2));
                return;
            }
            if(c.startsWith("console:")){
                ac.addConsoleCommand(c.substring(8));
                return;
            }
            ac.addConsoleCommand(c);
        });
    }
    
    /**
     * 将附件指令转换为字符串列表
     * @param ac 附件指令
     * @return List
     */
    public static List<String> command2String(AttachCommand ac){
        List<String> cmds = new ArrayList();
        ac.getCommand().forEach((t,l) -> {
            l.forEach(c -> cmds.add(t+":"+c));
        });
        return cmds;
    }
    
    /**
     * 将金钱列表设置为附件金钱
     * @param af 附件
     * @param list 金钱列表
     */
    public static void parseMoney(AttachFile af, List<String> list){
        af.removeAllMoney();
        MoneyManager mm = MoneyManager.getMoneyManager();
        list.forEach(l -> {
            if(l.contains(":")){
                String[] st = l.split(":");
                if(st.length>1){
                    String s1 = st[0];
                    String s2 = st[1].trim();
                    Object count = null;
                    AttachMoney am = mm.getAttachMoney(st[0]);
                    if(am instanceof AttachIntegerMoney){
                        count = parseInteger(s2);
                    }else if(am instanceof AttachDoubleMoney){
                        count = parseDouble(s2);
                    }
                    if(count!=null){
                        af.addMoney(s1, count);
                    }
                }
            }
        });
    }
    
    /**
     * 将附件金钱转换为字符串列表
     * @param af 附件
     * @return List
     */
    public static List<String> money2String(AttachFile af){
        List<String> cmds = new ArrayList();
        af.getMoneys().forEach((t,m) -> {
            cmds.add(t+":"+m.getCount().toString());
        });
        return cmds;
    }
    
    /**
     * 将字符串格式化为时间, 失败返回null
     * @param str 字符串
     * @return String
     */
    public static String parseTime(String str){
        try{
            return sdf1.format(sdf1.parse(str));
        }catch(ParseException ex1){
            try{
                return sdf1.format(sdf2.parse(str));
            }catch(ParseException ex2){
                return null;
            }
        }
    }
    
}

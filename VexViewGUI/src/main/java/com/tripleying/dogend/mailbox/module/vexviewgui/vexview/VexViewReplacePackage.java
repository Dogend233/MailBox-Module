package com.tripleying.dogend.mailbox.module.vexviewgui.vexview;

import com.tripleying.dogend.mailbox.api.mail.SystemMail;
import com.tripleying.dogend.mailbox.module.commonguiapi.gui.ReplaceConfig;
import com.tripleying.dogend.mailbox.module.commonguiapi.gui.ReplacePackage;
import com.tripleying.dogend.mailbox.module.commonguiapi.gui.ReplaceUtil;
import com.tripleying.dogend.mailbox.module.vexviewgui.VexViewGUI;
import com.tripleying.dogend.mailbox.util.MessageUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.VexComponents;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class VexViewReplacePackage {
    
    private Map<String, ReplacePackage> rpm;
    private Map<String, VexPackage> vpm;
    
    public VexViewReplacePackage(Map<String, ReplacePackage> rpm){
        this.rpm = rpm;
        vpm = new HashMap();
        rpm.forEach((t, rp) -> {
            VexPackage vp = null;
            switch(rp.getType()){
                case Integer:
                case Double:
                case DateTime:
                case String:
                    vp = VexViewUtil.getTextFieldPackage(getConfigurationSection(rp.getName(), TextFieldPackage.class));
                    break;
                case Boolean:
                    vp = VexViewUtil.getCheckBoxPackage(getConfigurationSection(rp.getName(), CheckBoxPackage.class));
                    break;
                case StringList:
                    vp = VexViewUtil.getTextAreaPackage(getConfigurationSection(rp.getName(), TextAreaPackage.class));
                    break;
                case ItemStackList:
                    vp = VexViewUtil.getItemListPackage(getConfigurationSection(rp.getName(), ItemListPackage.class));
                    break;
            }
            if(vp!=null) vpm.put(t, vp);
        });
    }
    
    public List<VexComponents> getVexComponents(ReplaceConfig rc, Map<String, Map<String, Integer>> mapstore){
        List<VexComponents> vcl = new ArrayList();
        SystemMail sm = rc.getSystemMail();
        int field = 0;
        int check = 0;
        int area = 0;
        for(Map.Entry<String, ReplacePackage> me:rpm.entrySet()){
            String t = me.getKey();
            ReplacePackage rp = me.getValue();
            VexPackage vp = vpm.get(t);
            String v = null;
            int id = -1;
            Object value = rp.getValue(sm);
            switch(rp.getType()){
                case Integer:
                case Double:
                case DateTime:
                case String:
                    v = "TextField";
                    id = field++;
                    vcl.add(((TextFieldPackage)vp).getVexTextField(id, 0, 0, value.toString()));
                    break;
                case Boolean:
                    v = "CheckBox";
                    id = check++;
                    vcl.add(((CheckBoxPackage)vp).getVexCheckBox(id, 0, 0, (boolean)value));
                    break;
                case StringList:
                    v = "TextArea";
                    id = area++;
                    vcl.add(((TextAreaPackage)vp).getVexTextArea(id, 0, 0, (List<String>)value));
                    break;
                case ItemStackList:
                    v = "ItemStackList";
                    id = 35;
                    vcl.addAll(((ItemListPackage)vp).getItemComponentList((List<ItemStack>)value, 36));
                    break;
            }
            if(vp instanceof DescriptionTextPackage){
                vcl.add(((DescriptionTextPackage)vp).getDescriptionText(Arrays.asList(rp.getDescription())));
            }
            if(v!=null && id!=-1){
                Map<String, Integer> map = mapstore.getOrDefault(v, new LinkedHashMap());
                map.put(t, id);
                if(!mapstore.containsKey(v)) mapstore.put(v, map);
            }
        }
        return vcl;
    }
    
    public void replaceMail(ReplaceConfig rc, Map<String, Map<String, Integer>> mapstore){
        VexGui gui = VexViewAPI.getPlayerCurrentGui(rc.getPlayer()).getVexGui();
        mapstore.forEach((v, im) -> im.forEach((t, i) -> {
            Object value = null;
            switch(v){
                case "TextField":
                    String text = MessageUtil.color(gui.getTextField(i).getTypedText());
                    switch(rpm.get(t).getType()){
                        case Integer:
                            value = ReplaceUtil.parseInteger(text.trim());
                            break;
                        case Double:
                            value = ReplaceUtil.parseDouble(text.trim());
                            break;
                        case DateTime:
                            value = ReplaceUtil.parseTime(text.trim());
                            break;
                        case String:
                        default:
                            value = text;
                            break;
                    }
                    break;
                case "CheckBox":
                    value = gui.getCheckBoxById(i).isChecked();
                    break;
                case "TextArea":
                    List<String> textlist = gui.getTextArea(i).getTypedText();
                    textlist.replaceAll(l -> MessageUtil.color(l));
                    value = textlist;
                    break;
                case "ItemStackList":
                    List<ItemStack> isl = new ArrayList();
                    for(int j=0;j<=i;j++){
                        ItemStack is = gui.getSlotById(j).getItem();
                        if(is!=null) isl.add(is);
                    }
                    value = isl;
                    break;
            }
            if(value!=null) rpm.get(t).ReplaceValue(rc.getSystemMail(), value);
        }));
    }
    
    private ConfigurationSection getConfigurationSection(String name, Class<? extends VexPackage> clazz){
        YamlConfiguration yml = VexViewGUI.getVexViewGUI().getConfig("gui/replace_mail.yml");
        ConfigurationSection cs;
        String key = "replace."+name;
        if(yml.contains(key)){
            cs = yml.getConfigurationSection(key);
        }else{
            try {
                MessageUtil.error(MessageUtil.color("[VexViewGUI]: 检测到发件GUI组件缺失, 正在尝试补全"));
                cs = (ConfigurationSection)clazz.getMethod("createConfigurationSection").invoke(null);
                yml.set(key, cs);
                File file = new File(VexViewGUI.getVexViewGUI().getDataFolder(), "gui");
                if(!file.exists()) file.mkdirs();
                file = new File(file, "replace_mail.yml");
                yml.save(file);
                MessageUtil.log(MessageUtil.color("[VexViewGUI]: 已补全: "+name+" , 请自行修改配置文件"));
                VexViewGUI.getVexViewGUI().reloadConfig("gui/replace_mail.yml");
            } catch (Exception ex) {
                cs = new YamlConfiguration();
            }
        }
        return cs;
    }
    
}

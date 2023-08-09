package com.tripleying.dogend.mailbox.module.vexviewcdkeygui.gui;

import com.tripleying.dogend.mailbox.module.vexviewcdkeygui.command.CDKeyCommand;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.ButtonPackage;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.GuiPackage;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.TextFieldPackage;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.TextPackage;
import com.tripleying.dogend.mailbox.module.vexviewgui.vexview.VexViewUtil;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexGui;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CDKeyGUI extends VexGui {
    
    private static GuiPackage background;
    private static TextPackage title;
    private static TextFieldPackage cdkey;
    private static ButtonPackage use;
    private boolean flag = true;
    
    public CDKeyGUI(String img, int x, int y, int w, int h){
        super(img, x, y, w, h);
        this.addComponent(title.getVexText(0, 0, null));
        this.addComponent(cdkey.getVexTextField(0, 0, 0));
        this.addComponent(use.getButton("MailBoxCDKeyGUICDKeyuse", 0, 0, player -> {
            if(flag){
                flag = false;
                Bukkit.getServer().dispatchCommand(player, CDKeyCommand.USE.addArg("cdkey", this.getTextField(0).getTypedText().trim()).getCommandString());
                player.closeInventory();
                flag = true;
            };
        }, null, null));
    }
    
    public static void open(Player p){
        CDKeyGUI gui = (CDKeyGUI)background.getVexGui(CDKeyGUI.class);
        if(gui!=null){
            p.closeInventory();
            VexViewAPI.openGui(p, gui);
        }
    }
    
    public static boolean init(YamlConfiguration yml){
        try{
            background = VexViewUtil.getGuiPackage(yml.getConfigurationSection("gui"));
            title = VexViewUtil.getTextPackage(yml.getConfigurationSection("title"));
            cdkey = VexViewUtil.getTextFieldPackage(yml.getConfigurationSection("cdkey"));
            use = VexViewUtil.getButtonPackage(yml.getConfigurationSection("use"));
            return true;
        }catch(Exception ex){
            return false;
        }
    }
    
}

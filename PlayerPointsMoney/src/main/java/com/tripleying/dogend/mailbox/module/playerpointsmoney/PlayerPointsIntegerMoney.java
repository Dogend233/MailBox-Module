package com.tripleying.dogend.mailbox.module.playerpointsmoney;

import com.tripleying.dogend.mailbox.api.money.IntegerMoney;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlayerPointsIntegerMoney extends IntegerMoney {
    
    private PlayerPoints point;

    public PlayerPointsIntegerMoney(String name, String display) {
        super(name, display);
        point = null;
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("PlayerPoints");
        if(plugin!=null){
            point = PlayerPoints.class.cast(plugin);
        }
    }

    @Override
    protected boolean givePlayerBalance(Player p, int i) {
        return point.getAPI().give(p.getUniqueId(), i);
    }

    @Override
    protected boolean removePlayerBalance(Player p, int i) {
        return point.getAPI().take(p.getUniqueId(), i);
    }

    @Override
    protected boolean hasPlayerBalance(Player p, int i) {
        return point.getAPI().look(p.getUniqueId())>=i;
    }

    @Override
    public Object getPlayerBalance(Player p) {
        return point.getAPI().look(p.getUniqueId());
    }
    
    public boolean isAvaliable(){
        return point!=null;
    }
    
}

package com.tripleying.dogend.mailbox.module.vaultmoney;

import com.tripleying.dogend.mailbox.api.money.DoubleMoney;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultDoubleMoney extends DoubleMoney {
    
    private Economy economy;

    public VaultDoubleMoney(String name, String display) {
        super(name, display);
        economy = null;
        if(Bukkit.getServer().getPluginManager().getPlugin("Vault") == null){
        }else{
            RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp!=null){
                economy = rsp.getProvider();
            }
        }
    }

    @Override
    protected boolean givePlayerBalance(Player p, double i) {
        EconomyResponse r = economy.depositPlayer(p, i);
        return r.transactionSuccess();
    }

    @Override
    protected boolean removePlayerBalance(Player p, double i) {
        EconomyResponse r = economy.withdrawPlayer(p, i);
        return r.transactionSuccess();
    }

    @Override
    protected boolean hasPlayerBalance(Player p, double i) {
        return economy.has(p, i);
    }

    @Override
    public Object getPlayerBalance(Player p) {
        return economy.getBalance(p);
    }
    
    public boolean isAvaliable(){
        return economy!=null;
    }
    
}

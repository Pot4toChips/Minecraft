package com.travila.achievementhunt;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Achievement {
    private ItemStack item;
    public boolean isDone;

    public Achievement(ItemStack itemStack) {
        this.item = itemStack;
        isDone = false;
    }

    public void CheckForItem(Player p){
        if (p.getInventory().contains(item)){
            isDone = true;
        }
    }

    public String GetName() {
        return "§eObtain: §f" + item.getAmount() + " " + item.getType().name();
    }
}

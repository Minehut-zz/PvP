package com.minehut.pvp.stats;

import com.google.gson.Gson;
import com.minehut.commons.common.chat.F;
import com.minehut.pvp.arena.ArenaType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Created by luke on 5/18/15.
 */
public class DeathStat {
    /* Killer */
    private UUID killer;
    private ArrayList<Map<String, Object>> killerInv;
    private ArrayList<Map<String, Object>> killerArmor;

    /* Dead */
    private UUID dead;
    private ArrayList<Map<String, Object>> deadInv;
    private ArrayList<Map<String, Object>> deadArmor;

    /* Queue */
    private ArenaType arenaType;

    public DeathStat(Player killer, Player dead, ArenaType arenaType) {
        /* Killer Player */
        this.killer = killer.getUniqueId();
        this.killerInv = getSerializedItems(killer.getInventory().getContents());
        this.killerArmor = getSerializedItems(killer.getInventory().getArmorContents());

        /* Dead Player */
        this.dead = dead.getUniqueId();
        this.deadInv = getSerializedItems(dead.getInventory().getContents());
        this.deadArmor = getSerializedItems(dead.getInventory().getArmorContents());

        this.arenaType = arenaType;
        this.upload();
    }

    private void upload() {
        F.debug(new Gson().toJson(this));
    }

    private ArrayList<Map<String, Object>> getSerializedItems(ItemStack[] contents) {
        ArrayList<Map<String, Object>> items = new ArrayList<Map<String, Object>>();

        for (ItemStack itemStack : contents) {
            /* Air is null, bukkit devs ur dumb */
            if (itemStack == null) {
                continue;
            }
            Map<String, Object> ser = itemStack.serialize();
            items.add(ser);
        }

        return items;
    }

}

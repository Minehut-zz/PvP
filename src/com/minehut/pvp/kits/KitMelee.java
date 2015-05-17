package com.minehut.pvp.kits;

import com.minehut.api.util.kit.Kit;
import com.minehut.api.util.player.GamePlayer;
import com.minehut.commons.common.items.ItemStackFactory;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by luke on 5/16/15.
 */
public class KitMelee extends Kit {

    public KitMelee(JavaPlugin plugin) {
        super(plugin, "Melee", Material.STONE_SWORD, 0);

        super.addItem(ItemStackFactory.createItem(Material.STONE_SWORD));

        super.addItem(ItemStackFactory.createItem(Material.IRON_HELMET));
        super.addItem(ItemStackFactory.createItem(Material.IRON_CHESTPLATE));
        super.addItem(ItemStackFactory.createItem(Material.IRON_LEGGINGS));
        super.addItem(ItemStackFactory.createItem(Material.IRON_BOOTS));
    }

    @Override
    public void extraEquip(GamePlayer gamePlayer) {
        /* On Equip effects (potion effects, etc) */
    }
}

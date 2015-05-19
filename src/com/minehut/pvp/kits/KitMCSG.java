package com.minehut.pvp.kits;

import com.minehut.api.util.kit.Kit;
import com.minehut.api.util.player.GamePlayer;
import com.minehut.commons.common.items.ItemStackFactory;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by luke on 5/16/15.
 */
public class KitMCSG extends Kit {

    public KitMCSG(JavaPlugin plugin) {
        super(plugin, "MCSG", Material.FISHING_ROD, 0);

        super.addItem(ItemStackFactory.createItem(Material.STONE_SWORD));
        super.addItem(ItemStackFactory.createItem(Material.FISHING_ROD));
        super.addItem(ItemStackFactory.createItem(Material.BOW));

        super.addItem(ItemStackFactory.createItem(Material.GOLDEN_APPLE));
        super.addItem(ItemStackFactory.createItem(Material.GOLDEN_CARROT));
        super.addItem(ItemStackFactory.createItem(Material.PUMPKIN_PIE));
        super.addItem(ItemStackFactory.createItem(Material.GOLDEN_CARROT, 2));
        super.addItem(ItemStackFactory.createItem(Material.MELON, 2));
        super.addItem(ItemStackFactory.createItem(Material.BREAD));
        super.addItem(ItemStackFactory.createItem(Material.ARROW, 8));

        super.addItem(ItemStackFactory.createItem(Material.IRON_HELMET));
        super.addItem(ItemStackFactory.createItem(Material.CHAINMAIL_CHESTPLATE));
        super.addItem(ItemStackFactory.createItem(Material.GOLD_LEGGINGS));
        super.addItem(ItemStackFactory.createItem(Material.IRON_BOOTS));
    }

    @Override
    public void extraEquip(GamePlayer gamePlayer) {
        /* On Equip effects (potion effects, etc) */
    }
}

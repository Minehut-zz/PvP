package com.minehut.pvp.kits;

import com.minehut.api.util.kit.Kit;
import com.minehut.api.util.player.GamePlayer;
import com.minehut.commons.common.items.GameItemStackEnchantment;
import com.minehut.commons.common.items.ItemStackFactory;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by luke on 5/16/15.
 */
public class KitCQB extends Kit {

    public KitCQB(JavaPlugin plugin) {
        super(plugin, "CQB", Material.STONE_SWORD, 0);

        super.addItem(ItemStackFactory.createItem(Material.STONE_SWORD, new GameItemStackEnchantment(Enchantment.DURABILITY, 5)));

        super.addItem(ItemStackFactory.createItem(Material.IRON_HELMET, new GameItemStackEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)));
        super.addItem(ItemStackFactory.createItem(Material.IRON_CHESTPLATE, new GameItemStackEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)));
        super.addItem(ItemStackFactory.createItem(Material.IRON_LEGGINGS, new GameItemStackEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)));
        super.addItem(ItemStackFactory.createItem(Material.IRON_BOOTS, new GameItemStackEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)));
    }

    @Override
    public void extraEquip(GamePlayer gamePlayer) {
        /* On Equip effects (potion effects, etc) */
    }
}

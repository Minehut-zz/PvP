package com.minehut.pvp.kits;

import com.minehut.api.util.kit.Kit;
import com.minehut.api.util.player.GamePlayer;
import com.minehut.commons.common.items.GameItemStackEnchantment;
import com.minehut.commons.common.items.ItemStackFactory;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

/**
 * Created by luke on 5/16/15.
 */
public class KitRanged extends Kit {

    public KitRanged(JavaPlugin plugin) {
        super(plugin, "RANGED", Material.BOW, 0);

        super.addItem(ItemStackFactory.createItem(Material.BOW, new GameItemStackEnchantment(Enchantment.ARROW_INFINITE, 1), new GameItemStackEnchantment(Enchantment.ARROW_KNOCKBACK, 2)));
        super.addItem(ItemStackFactory.createItem(Material.ARROW, 64));

        super.addItem(ItemStackFactory.createItem(Material.ENDER_PEARL, 4));

        /* Speed Pots */
        Potion speed = new Potion(PotionType.SPEED, 1, true);
        super.addItem(speed.toItemStack(1));
        super.addItem(speed.toItemStack(1));
        super.addItem(speed.toItemStack(1));
        super.addItem(speed.toItemStack(1));
        super.addItem(speed.toItemStack(1));

        super.addItem(ItemStackFactory.createItem(Material.COOKED_BEEF, 64));

        super.addItem(ItemStackFactory.createItem(Material.LEATHER_HELMET));
        super.addItem(ItemStackFactory.createItem(Material.LEATHER_CHESTPLATE));
        super.addItem(ItemStackFactory.createItem(Material.LEATHER_LEGGINGS));
        super.addItem(ItemStackFactory.createItem(Material.LEATHER_BOOTS));
    }

    @Override
    public void extraEquip(GamePlayer gamePlayer) {
        /* On Equip effects (potion effects, etc) */
    }
}

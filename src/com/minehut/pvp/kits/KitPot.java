package com.minehut.pvp.kits;

import com.minehut.api.util.kit.Kit;
import com.minehut.api.util.player.GamePlayer;
import com.minehut.commons.common.items.GameItemStackEnchantment;
import com.minehut.commons.common.items.ItemStackFactory;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

/**
 * Created by luke on 5/16/15.
 */
public class KitPot extends Kit {

    public KitPot(JavaPlugin plugin) {
        super(plugin, "POT", Material.POTION, 0);

        super.addItem(ItemStackFactory.createItem(Material.DIAMOND_SWORD, new GameItemStackEnchantment(Enchantment.DAMAGE_ALL, 5), new GameItemStackEnchantment(Enchantment.DURABILITY, 3), new GameItemStackEnchantment(Enchantment.FIRE_ASPECT, 2)));
        super.addItem(ItemStackFactory.createItem(Material.BOW, new GameItemStackEnchantment(Enchantment.ARROW_DAMAGE, 2), new GameItemStackEnchantment(Enchantment.ARROW_FIRE, 1), new GameItemStackEnchantment(Enchantment.ARROW_INFINITE, 1)));

        super.addItem(ItemStackFactory.createItem(Material.ENDER_PEARL, 4));
        super.addItem(ItemStackFactory.createItem(Material.COOKED_BEEF, 64));

        Potion healingPot = new Potion(PotionType.INSTANT_HEAL, 2, true);
        super.addItem(healingPot.toItemStack(1));

        Potion fireResistance = new Potion(PotionType.FIRE_RESISTANCE, 1, false);
        super.addItem(fireResistance.toItemStack(1));

        Potion regen = new Potion(PotionType.REGEN, 2, false);
        super.addItem(regen.toItemStack(1));

        Potion strength = new Potion(PotionType.STRENGTH, 2, false);
        super.addItem(strength.toItemStack(1));

        Potion speed = new Potion(PotionType.SPEED, 2, false);
        super.addItem(speed.toItemStack(1));

        /* One row of health pots, non stacked */
        super.addItem(healingPot.toItemStack(1));
        super.addItem(healingPot.toItemStack(1));
        super.addItem(healingPot.toItemStack(1));
        super.addItem(healingPot.toItemStack(1));
        super.addItem(healingPot.toItemStack(1));
        super.addItem(healingPot.toItemStack(1));
        super.addItem(healingPot.toItemStack(1));
        super.addItem(healingPot.toItemStack(1));
        super.addItem(healingPot.toItemStack(1));

        super.addItem(ItemStackFactory.createItem(Material.ARROW, 64));

        super.addItem(ItemStackFactory.createItem(Material.DIAMOND_HELMET, new GameItemStackEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4), new GameItemStackEnchantment(Enchantment.DURABILITY, 3)));
        super.addItem(ItemStackFactory.createItem(Material.DIAMOND_CHESTPLATE, new GameItemStackEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4), new GameItemStackEnchantment(Enchantment.DURABILITY, 3)));
        super.addItem(ItemStackFactory.createItem(Material.DIAMOND_LEGGINGS, new GameItemStackEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4), new GameItemStackEnchantment(Enchantment.DURABILITY, 3)));
        super.addItem(ItemStackFactory.createItem(Material.DIAMOND_BOOTS, new GameItemStackEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4), new GameItemStackEnchantment(Enchantment.DURABILITY, 3)));
    }

    @Override
    public void extraEquip(GamePlayer gamePlayer) {
        /* On Equip effects (potion effects, etc) */
    }
}

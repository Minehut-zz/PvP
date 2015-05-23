package com.minehut.pvp.gui;

import com.minehut.commons.common.chat.C;
import com.minehut.commons.common.items.EnchantGlow;
import com.minehut.commons.common.items.ItemStackFactory;
import com.minehut.commons.common.sound.S;
import com.minehut.pvp.Core;
import com.minehut.pvp.arena.ArenaType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by luke on 5/17/15.
 */
public class GUIMenus implements Listener {
    private Core core;

    /* Queue */
    private Inventory queueInv;
    private ItemStack queueItem;

    /* ELO */
    private ItemStack eloItem;

    public GUIMenus(Core core) {
        this.core = core;
        Bukkit.getServer().getPluginManager().registerEvents(this, core);

        /* Queue */
        this.queueItem = ItemStackFactory.createItem(Material.DIAMOND_SWORD, C.aqua + C.bold + "Join Queue");
        this.createQueueInv();
        this.queueMenuUpdater();

        /* ELO */
        ItemStack book = ItemStackFactory.createItem(Material.BOOK, C.green + "/elo");
        EnchantGlow.addGlow(book);
        this.eloItem = book;

    }

    private void queueMenuUpdater() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.core, new Runnable() {
            @Override
            public void run() {
                queueInv.clear();

                for (ArenaType arenaType : ArenaType.values()) {
                    queueInv.addItem(getQueueIcon(arenaType));
                }
                queueInv.setItem(8, ItemStackFactory.createItem(Material.REDSTONE, C.red + C.bold + "LEAVE QUEUE"));
            }
        }, 20, 20 * 3);
    }

    private void createQueueInv() {
        this.queueInv = Bukkit.getServer().createInventory(null, 9, C.underline + "Queue Menu");

        for (ArenaType arenaType : ArenaType.values()) {
            this.queueInv.addItem(getQueueIcon(arenaType));
        }

        this.queueInv.setItem(8, ItemStackFactory.createItem(Material.REDSTONE, C.red + C.bold + "LEAVE QUEUE"));
    }

    private ItemStack getQueueIcon(ArenaType arenaType) {
        int queueSize = this.core.getQueueManager().getPlayersInQueue(arenaType).size();

        int playersInGame = 0;
        ArrayList<Player> playersInGameList = this.core.getArenaManager().getPlayersInArena(arenaType);
        if (playersInGameList != null) {
            playersInGame = playersInGameList.size();
        }
        int totalPlayers = queueSize + playersInGame;

        return ItemStackFactory.createItem(arenaType.getMaterial(), arenaType.getDisplayName(),
                Arrays.asList("", C.gray + "Players: " + C.aqua + Integer.toString(totalPlayers), ""));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (this.core.arenaManager.isPlayerInArena((Player) event.getWhoClicked())) {
            return;
        }
        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getName().equalsIgnoreCase(this.queueInv.getName())) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) {
                return;
            }

            if (event.getCurrentItem().getType() == null) {
                return;
            }

            if (event.getCurrentItem().getItemMeta() == null || event.getCurrentItem().getItemMeta().getDisplayName() == null) {
                return;
            }

            /* Leave Queue button */
            if (event.getCurrentItem().getType() == Material.REDSTONE) {
                if (this.core.queueManager.isPlayerInQueue(player.getUniqueId())) {
                    this.core.queueManager.leaveQueue(player.getUniqueId());
                }
                player.closeInventory();
                S.pling(player);
                player.sendMessage("");
                player.sendMessage(C.white + "You have " + C.red + "left " + C.white + "the queue.");
                player.sendMessage("");

                return;
            }

            if (!this.core.queueManager.isPlayerInQueue(player.getUniqueId())) {
                ArenaType type = ArenaType.getArenaType(event.getCurrentItem().getType());
                this.core.queueManager.joinQueue(player.getUniqueId(), type);

                S.pling(player);
                player.closeInventory();

                player.sendMessage("");
                player.sendMessage(C.white + "You have joined the " + C.aqua + type.getDisplayName() + C.white + " queue.");
                player.sendMessage("");
            } else {

                /* Remove from queue */
                this.core.queueManager.leaveQueue(player.getUniqueId());

                /* Add into new queue */
                ArenaType type = ArenaType.getArenaType(event.getCurrentItem().getType());
                this.core.queueManager.joinQueue(player.getUniqueId(), type);

                S.pling(player);
                player.closeInventory();

                player.sendMessage("");
                player.sendMessage(C.white + "You have joined the " + C.aqua + type.getDisplayName() + C.white + " queue.");
                player.sendMessage("");
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand() != null && player.getItemInHand().getItemMeta() != null) {
            if (player.getItemInHand().getItemMeta().getDisplayName() != null) {

                /* Queue Inventory */
                if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(queueItem.getItemMeta().getDisplayName())) {
                    event.setCancelled(true);
                    player.openInventory(this.queueInv);
                    return;
                }

                /* ELO Item */
                if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(eloItem.getItemMeta().getDisplayName())) {
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerCommandPreprocessEvent(player, "/elo"));
                    return;
                }
            }
        }
    }

    public ItemStack getQueueItem() {
        return queueItem;
    }

    public ItemStack getEloItem() {
        return eloItem;
    }
}

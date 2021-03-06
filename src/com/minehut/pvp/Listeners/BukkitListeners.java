package com.minehut.pvp.listeners;

import com.minehut.api.API;
import com.minehut.api.util.player.GamePlayer;
import com.minehut.api.util.player.Rank;
import com.minehut.commons.common.chat.C;
import com.minehut.commons.common.chat.F;
import com.minehut.commons.common.items.ItemStackFactory;
import com.minehut.commons.common.level.Level;
import com.minehut.commons.common.player.PlayerUtil;
import com.minehut.pvp.ELOManager;
import com.minehut.pvp.arena.Arena.Team;
import com.minehut.pvp.Core;

import com.minehut.pvp.events.EventCaller;
import com.minehut.pvp.events.events.SpawnPreparePlayerEvent;
import com.minehut.pvp.stats.DeathStat;
import org.bukkit.*;
import org.bukkit.entity.Fish;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by luke on 5/16/15.
 */
public class BukkitListeners implements Listener {
    private Core core;

    public BukkitListeners(Core core) {
        this.core = core;
        Bukkit.getServer().getPluginManager().registerEvents(this, core);

        Bukkit.getServer().getWorlds().get(0).setSpawnLocation(0, 72, 0);
    }

    @EventHandler
    public void onSpawnPreparePlayer(SpawnPreparePlayerEvent event) {
        Player player = event.getPlayer();
        player.setPlayerWeather(WeatherType.CLEAR);
        PlayerUtil.clearAll(player);
        player.getInventory().setItem(1, this.core.getGuiMenus().getQueueItem());
        player.getInventory().setItem(8, this.core.getGuiMenus().getEloItem());

//        if(this.core.getEloManager().hasELO(event.getPlayer())) {
//            /* Elo */
//            ArrayList<ELOManager.ELO> oldElo = this.core.getEloManager().getCachedElos(player);
//            ArrayList<ELOManager.ELO> newElo = this.core.getEloManager().updatedCachedElos(player);
//
//            /* Make sure player didn't just log in */
//            if (oldElo != null && !oldElo.isEmpty()) {
//                for (ELOManager.ELO old : oldElo) {
//                    ELOManager.Division oldDivision = this.core.getEloManager().getPlayerDivisionForElo(old);
//                    ELOManager.Division newDivision = this.core.getEloManager().getPlayerDivisionForElo(newElo.get(oldElo.indexOf(old)));
//
//                    /* Old was worse than new */
//                    if (oldDivision.compareTo(newDivision) < 0) {
//                        Bukkit.broadcastMessage("");
//                        Bukkit.broadcastMessage(C.aqua + player.getName() + C.white + " was promoted to " + C.yellow + C.bold + newDivision.name().toUpperCase());
//                        Bukkit.broadcastMessage("");
//                    } else {
//                        F.log("ELO: " + player.getName() + " | Both were same: " + newDivision.name() + " / " + oldDivision.name());
//                    }
//                }
//            } else {
//                F.log("either oldElo or newElo was null");
//            }

            /* Equip armor */
            this.equipDivisionalArmor(player);
    }

    public void equipDivisionalArmor(Player player) {
        ELOManager.Division division = this.core.getEloManager().getPlayerDivisionForElo(this.core.eloManager.getHighestELOAsELO(player));
        if (division == ELOManager.Division.Bronze) {
            player.getInventory().setHelmet(ItemStackFactory.createItem(Material.LEATHER_HELMET, C.yellow + "Bronze Division Helmet"));
            player.getInventory().setChestplate(ItemStackFactory.createItem(Material.LEATHER_CHESTPLATE, C.yellow + "Bronze Division Chest Plate"));
            player.getInventory().setLeggings(ItemStackFactory.createItem(Material.LEATHER_LEGGINGS, C.yellow + "Bronze Division Leggings"));
            player.getInventory().setBoots(ItemStackFactory.createItem(Material.LEATHER_BOOTS, C.yellow + "Bronze Division Boots"));
        }
        else if (division == ELOManager.Division.Silver) {
            player.getInventory().setHelmet(ItemStackFactory.createItem(Material.IRON_HELMET, C.yellow + "Iron Division Helmet"));
            player.getInventory().setChestplate(ItemStackFactory.createItem(Material.IRON_CHESTPLATE, C.yellow + "Iron Division Chest Plate"));
            player.getInventory().setLeggings(ItemStackFactory.createItem(Material.IRON_LEGGINGS, C.yellow + "Iron Division Leggings"));
            player.getInventory().setBoots(ItemStackFactory.createItem(Material.IRON_BOOTS, C.yellow + "Iron Division Boots"));
        }
        else if (division == ELOManager.Division.Gold) {
            player.getInventory().setHelmet(ItemStackFactory.createItem(Material.GOLD_HELMET, C.yellow + "Gold Division Helmet"));
            player.getInventory().setChestplate(ItemStackFactory.createItem(Material.GOLD_CHESTPLATE, C.yellow + "Gold Division Chest Plate"));
            player.getInventory().setLeggings(ItemStackFactory.createItem(Material.GOLD_LEGGINGS, C.yellow + "Gold Division Leggings"));
            player.getInventory().setBoots(ItemStackFactory.createItem(Material.GOLD_BOOTS, C.yellow + "Gold Division Boots"));
        }
        else if (division == ELOManager.Division.Platinum) {
            player.getInventory().setHelmet(ItemStackFactory.createColoredArmorWithGlow(Material.LEATHER_HELMET, C.yellow + "Platinum Division Helmet", Color.FUCHSIA));
            player.getInventory().setChestplate(ItemStackFactory.createColoredArmorWithGlow(Material.LEATHER_CHESTPLATE, C.yellow + "Platinum Division Chest Plate", Color.FUCHSIA));
            player.getInventory().setLeggings(ItemStackFactory.createColoredArmorWithGlow(Material.LEATHER_LEGGINGS, C.yellow + "Platinum Division Leggings", Color.FUCHSIA));
            player.getInventory().setBoots(ItemStackFactory.createColoredArmorWithGlow(Material.LEATHER_BOOTS, C.yellow + "Platinum Division Boots", Color.FUCHSIA));
        }
        else if (division == ELOManager.Division.Diamond) {
            player.getInventory().setHelmet(ItemStackFactory.createItemWithGlow(Material.DIAMOND_HELMET, C.yellow + "Diamond Division Helmet"));
            player.getInventory().setChestplate(ItemStackFactory.createItemWithGlow(Material.DIAMOND_CHESTPLATE, C.yellow + "Diamond Division Chest Plate"));
            player.getInventory().setLeggings(ItemStackFactory.createItemWithGlow(Material.DIAMOND_LEGGINGS, C.yellow + "Diamond Division Leggings"));
            player.getInventory().setBoots(ItemStackFactory.createItemWithGlow(Material.DIAMOND_BOOTS, C.yellow + "Diamond Division Boots"));
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        /* Clear Item Drops */
        event.getDrops().clear();

        if (event.getEntity().getKiller() != null) {
//            new DeathStat(this.core, event.getEntity().getKiller(), event.getEntity(), core.getArenaManager().getPlayerArena(event.getEntity()).getType());
            event.setDeathMessage(C.gray + "Death> " + C.green + event.getEntity().getKiller().getName() + C.purple + " beat " + C.red + event.getEntity().getName()
                    + C.purple + " in the " + core.getArenaManager().getPlayerArena(event.getEntity()).getType().getDisplayName() + C.purple + " ladder.");

            this.core.getKillsManager().addKill(event.getEntity().getKiller());
            this.core.getKillsManager().addDeath(event.getEntity());
        } else {
            event.setDeathMessage("");
        }

        core.getArenaManager().getPlayerArena(event.getEntity()).end(core.getArenaManager().getPlayerArena(event.getEntity()).getEnemyTeam(event.getEntity()));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {

        // Make sure its a player
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player hurtPlayer = (Player) event.getEntity();
        Projectile projectile = getProjectile(event);
        LivingEntity damagerEntity = null;

        // Check to see if there is a damager
        if (event instanceof EntityDamageByEntityEvent) {
            if (((EntityDamageByEntityEvent) event).getDamager() != null && ((EntityDamageByEntityEvent) event).getDamager() instanceof LivingEntity) {
                damagerEntity = (LivingEntity) ((EntityDamageByEntityEvent) event).getDamager();
            }
        }

        // Get the arrow shooter
        if (projectile != null) {
            if (projectile.getShooter() instanceof LivingEntity) {
                damagerEntity = (LivingEntity) projectile.getShooter();
            }
            projectile.remove(); //Stop arrows from sticking in players/ground.
        }

        /* Check if hurt player is in arena */
        if (!core.getArenaManager().isPlayerInArena(hurtPlayer)) {
            event.setCancelled(true);
            return;
        }

        /* Check if damager player is in arena with hurt player */
        if (damagerEntity instanceof Player) { //Allow mobs to damage player
            if (!core.getArenaManager().isPlayerInArena((Player) damagerEntity)) {
                event.setCancelled(true); // Spectator
                return;
            }
        }

        /* Friendly fire */
        if (damagerEntity instanceof Player) {
            Player damagerPlayer = (Player) damagerEntity;

            Team damagerTeam = core.getArenaManager().getPlayerArena(damagerPlayer).getPlayerTeam(damagerPlayer);
            Team hurtTeam = core.getArenaManager().getPlayerArena(hurtPlayer).getPlayerTeam(hurtPlayer);

            if (damagerTeam == hurtTeam) {
                event.setCancelled(true); //Friendly fire
                return;
            }
        }
    }

    Projectile getProjectile(EntityDamageEvent event) {
        if (!(event instanceof EntityDamageByEntityEvent)) {
            return null;
        }
        EntityDamageByEntityEvent eventEE = (EntityDamageByEntityEvent)event;

        if ((eventEE.getDamager() instanceof Projectile)) {
            if (eventEE.getDamager() instanceof Fish) {
                return null;
            }
            return (Projectile) eventEE.getDamager();
        }
        return null;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(this.getSpawn());
        event.getPlayer().teleport(this.getSpawn());
        EventCaller.callSpawnPreparePlayer(event.getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().teleport(this.getSpawn());

        if (this.core.queueManager.isPlayerInQueue(event.getPlayer().getUniqueId())) {
            this.core.queueManager.leaveQueue(event.getPlayer().getUniqueId());
        }

        this.core.getKillsManager().setupPlayer(event.getPlayer());

        EventCaller.callSpawnPreparePlayer(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");

        if (this.core.queueManager.isPlayerInQueue(event.getPlayer().getUniqueId())) {
        	this.core.queueManager.leaveQueue(event.getPlayer().getUniqueId());
        }
        if (this.core.arenaManager.isPlayerInArena(event.getPlayer())) {
            Team enemyTeam = core.getArenaManager().getPlayerArena(event.getPlayer()).getEnemyTeam(event.getPlayer());
            if (enemyTeam == Team.TEAM1) {
                for (UUID uuid : this.core.getArenaManager().getPlayerArena(event.getPlayer()).getTeam1()) {
                    Player player = Bukkit.getServer().getPlayer(uuid);
                    if (player != null) {
                        this.core.getKillsManager().addKill(player);
                    }
                }
            } else if (enemyTeam == Team.TEAM2) {
                for (UUID uuid : this.core.getArenaManager().getPlayerArena(event.getPlayer()).getTeam2()) {
                    Player player = Bukkit.getServer().getPlayer(uuid);
                    if (player != null) {
                        this.core.getKillsManager().addKill(player);
                    }
                }
            }

            this.core.getKillsManager().addDeath(event.getPlayer());
            /* todo: this will need changing for teams */
            Player enemy = Bukkit.getPlayer(this.core.getArenaManager().getPlayerArena(event.getPlayer()).getEnemyTeamList(event.getPlayer()).get(0));
            this.core.getKillsManager().addKill(enemy);

            event.setQuitMessage(C.gray + "Death> " + C.green + enemy.getName() + C.purple + " beat " + C.red + event.getPlayer().getName()
                    + C.purple + " in the " + core.getArenaManager().getPlayerArena(event.getPlayer()).getType().getDisplayName() + C.purple + " ladder.");

            core.getArenaManager().getPlayerArena(event.getPlayer()).end(enemyTeam);
        }

        this.core.getEloManager().clearCachedEdlos(event.getPlayer());
    }
    
    @EventHandler
    public void onKick(PlayerKickEvent event) {
        event.setLeaveMessage("");

        if (this.core.queueManager.isPlayerInQueue(event.getPlayer().getUniqueId())) {
        	this.core.queueManager.leaveQueue(event.getPlayer().getUniqueId());
        }
        if (this.core.arenaManager.isPlayerInArena(event.getPlayer())) {
            Team enemyTeam = core.getArenaManager().getPlayerArena(event.getPlayer()).getEnemyTeam(event.getPlayer());
            if (enemyTeam == Team.TEAM1) {
                for (UUID uuid : this.core.getArenaManager().getPlayerArena(event.getPlayer()).getTeam1()) {
                    Player player = Bukkit.getServer().getPlayer(uuid);
                    if (player != null) {
                        this.core.getKillsManager().addKill(player);
                    }
                }
            } else if (enemyTeam == Team.TEAM2) {
                for (UUID uuid : this.core.getArenaManager().getPlayerArena(event.getPlayer()).getTeam2()) {
                    Player player = Bukkit.getServer().getPlayer(uuid);
                    if (player != null) {
                        this.core.getKillsManager().addKill(player);
                    }
                }
            }

            this.core.getKillsManager().addDeath(event.getPlayer());
            /* todo: this will need changing for teams */
            Player enemy = Bukkit.getPlayer(this.core.getArenaManager().getPlayerArena(event.getPlayer()).getEnemyTeamList(event.getPlayer()).get(0));
            this.core.getKillsManager().addKill(enemy);

            event.setLeaveMessage(C.gray + "Death> " + C.green + enemy.getName() + C.purple + " beat " + C.red + event.getPlayer().getName()
                    + C.purple + " in the " + core.getArenaManager().getPlayerArena(event.getPlayer()).getType().getDisplayName() + C.purple + " ladder.");

            core.getArenaManager().getPlayerArena(event.getPlayer()).end(enemyTeam);
        }

        this.core.getEloManager().clearCachedEdlos(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        /* Fall below spawn */
        if (event.getPlayer().getLocation().getY() <= 20) {
            event.setTo(this.getSpawn());
        }
    }

	@EventHandler
	public void weatherChange(WeatherChangeEvent event) {
		if (event.getWorld().getName().equals(Bukkit.getWorlds().get(0).getName())) {
			event.setCancelled(true);
		}
	}
	

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
            event.getPlayer().setItemInHand(ItemStackFactory.createItem(Material.AIR));
            event.getPlayer().setItemInHand(event.getItemDrop().getItemStack());
            event.getPlayer().updateInventory();
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (this.core.arenaManager.isPlayerInArena(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }

        GamePlayer gamePlayer = API.getAPI().getGamePlayer(event.getPlayer());
        if (!gamePlayer.getRank().has(null, Rank.Admin, false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (this.core.arenaManager.isPlayerInArena(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }

        GamePlayer gamePlayer = API.getAPI().getGamePlayer(event.getPlayer());
        if (!gamePlayer.getRank().has(null, Rank.Admin, false)) {
            event.setCancelled(true);
            event.getPlayer().updateInventory();
        }
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        if (!this.core.getArenaManager().isPlayerInArena(((Player) event.getWhoClicked()))) {
            if (((Player) event.getWhoClicked()).getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (this.core.getArenaManager().isPlayerInArena((Player) event.getWhoClicked())) {
            return;
        }

        if (event.getWhoClicked().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryMoveItemEvent event) {
        if (this.core.getArenaManager().isPlayerInArena(((Player) event.getSource().getHolder()))) {
            return;
        }

        if (((Player) event.getSource().getHolder()).getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (this.core.getArenaManager().isPlayerInArena((Player) event.getWhoClicked())) {
            return;
        }

        if (event.getWhoClicked().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent event) {
        if (this.core.getArenaManager().isPlayerInArena((Player) event.getWhoClicked())) {
            return;
        }

        if (event.getWhoClicked().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled())
            return;
        Player player = event.getPlayer();
        GamePlayer gamePlayer = API.getAPI().getGamePlayer(player);
        Rank rank = gamePlayer.getRank();
        int level = gamePlayer.getLevel();

        event.setFormat(rank.getTag() + player.getDisplayName() + ((rank == Rank.regular) ? C.gray : C.white) + ": " + ((rank == Rank.regular) ? C.gray : C.white) + "%2$s");
        
    }

    Location getSpawn() {
        return Bukkit.getServer().getWorlds().get(0).getSpawnLocation();
    }

    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent event) {
        /* Only full hunger to those not in combat */
        if (!this.core.arenaManager.isPlayerInArena((Player) event.getEntity())) {
            event.setFoodLevel(20);
        }
    }

    @EventHandler
    public void onLeaveDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }
}

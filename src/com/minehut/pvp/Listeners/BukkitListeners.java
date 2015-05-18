package com.minehut.pvp.Listeners;

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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;

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

        /* Divisional Armor */
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
            new DeathStat(this.core, event.getEntity().getKiller(), event.getEntity(), core.getArenaManager().getPlayerArena(event.getEntity()).getType());
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

        EventCaller.callSpawnPreparePlayer(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");

        if (this.core.queueManager.isPlayerInQueue(event.getPlayer().getUniqueId())) {
        	this.core.queueManager.leaveQueue(event.getPlayer().getUniqueId());
        }
        if (this.core.arenaManager.isPlayerInArena(event.getPlayer())) {

            core.getArenaManager().getPlayerArena(event.getPlayer()).end(core.getArenaManager().getPlayerArena(event.getPlayer()).getEnemyTeam(event.getPlayer()));
            Bukkit.broadcastMessage(C.red + event.getPlayer().getName() + C.white + " has lost!");

        }


    }
    
    @EventHandler
    public void onKick(PlayerKickEvent event) {
        event.setLeaveMessage("");

        if (this.core.queueManager.isPlayerInQueue(event.getPlayer().getUniqueId())) {
        	this.core.queueManager.leaveQueue(event.getPlayer().getUniqueId());
        }
        if (this.core.arenaManager.isPlayerInArena(event.getPlayer())) {

            core.getArenaManager().getPlayerArena(event.getPlayer()).end(core.getArenaManager().getPlayerArena(event.getPlayer()).getEnemyTeam(event.getPlayer()));
            Bukkit.broadcastMessage(C.red + event.getPlayer().getName() + C.white + " has lost!");
        }
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
        if (this.core.arenaManager.isPlayerInArena(event.getPlayer())) {
            return; //Allow drops in game
        }

        if (!API.getAPI().getGamePlayer(event.getPlayer()).getRank().has(null, Rank.Admin, false)) {
            event.setCancelled(true);
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
    public void onMoveInventory(InventoryMoveItemEvent event) {
        if (this.core.arenaManager.isPlayerInArena((Player) event.getSource().getHolder())) {
            return; //Allow inv move in game
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled())
            return;
        Player player = event.getPlayer();
        GamePlayer gamePlayer = API.getAPI().getGamePlayer(player);
        Rank rank = gamePlayer.getRank();
        int level = gamePlayer.getLevel();

        int currentELO = this.core.eloManager.getHighestELO(player);
        event.setFormat(Level.getLevelColor(level) + Integer.toString(level) + " " + 
        rank.getTag() + player.getDisplayName() + C.white + "(" + ((currentELO > 500)?currentELO:"Unranked") +")" + ((rank == Rank.regular)?C.gray:C.white) + ": " + ((rank == Rank.regular)?C.gray:C.white)+ "%2$s");
        
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

}

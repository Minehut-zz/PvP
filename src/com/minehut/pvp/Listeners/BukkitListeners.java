package com.minehut.pvp.Listeners;

import com.minehut.api.API;
import com.minehut.api.util.player.GamePlayer;
import com.minehut.api.util.player.Rank;
import com.minehut.commons.common.chat.C;
import com.minehut.commons.common.chat.F;
import com.minehut.commons.common.level.Level;
import com.minehut.commons.common.player.PlayerUtil;
import com.minehut.pvp.arena.Arena.Team;
import com.minehut.pvp.Core;

import com.minehut.pvp.events.EventCaller;
import com.minehut.pvp.events.events.SpawnPreparePlayerEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WeatherType;
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
        event.getPlayer().setPlayerWeather(WeatherType.CLEAR);
        PlayerUtil.clearAll(event.getPlayer());
        event.getPlayer().getInventory().setItem(1, this.core.getGuiMenus().getQueueItem());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
    	core.getArenaManager().getPlayerArena(event.getEntity()).end(core.getArenaManager().getPlayerArena(event.getEntity()).getEnemyTeam(event.getEntity()));

        /* Clear Item Drops */
        event.getDrops().clear();

        /* todo: database stat updates (kills/deaths). */
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
        if (!this.core.arenaManager.isPlayerInArena(event.getPlayer())) {
        	//event.getPlayer().setHealth(20D);
        	//event.getPlayer().setFoodLevel(20);
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
        if (!API.getAPI().getGamePlayer(event.getPlayer()).getRank().has(null, Rank.Admin, false)) {
            event.setCancelled(true);
            event.getPlayer().updateInventory();
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        GamePlayer gamePlayer = API.getAPI().getGamePlayer(event.getPlayer());
        if (!gamePlayer.getRank().has(null, Rank.Admin, false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        GamePlayer gamePlayer = API.getAPI().getGamePlayer(event.getPlayer());
        if (!gamePlayer.getRank().has(null, Rank.Admin, false)) {
            event.setCancelled(true);
            event.getPlayer().updateInventory();
        }
    }

    @EventHandler
    public void onMoveInventory(InventoryMoveItemEvent event) {
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

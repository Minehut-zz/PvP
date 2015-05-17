package com.minehut.pvp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.minehut.api.util.kit.Kit;
import com.minehut.pvp.Listeners.BukkitListeners;
import com.minehut.pvp.arena.Arena;
import com.minehut.pvp.arena.ArenaManager;
import com.minehut.pvp.commands.CreateArenaCommand;
import com.minehut.pvp.commands.JoinCommand;
import com.minehut.pvp.kits.KitBoth;
import com.minehut.pvp.kits.KitMelee;
import com.minehut.pvp.kits.KitRanged;
import com.minehut.pvp.queue.QueueManager;
import com.minehut.pvp.queue.QueueRunnable;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.minehut.api.API;

public class Core extends JavaPlugin implements Listener {

	public Gson gson = new Gson();
	
	public API api;
	
	public QueueManager queueManager;
	
	public ArenaManager arenaManager;

	public ELOManager eloManager;
	
	public BukkitListeners bukkitListeners;

	public ArrayList<Kit> kits;
	
	@Override
	public void onEnable() {
		if (Bukkit.getPluginManager().getPlugin("API") != null) {
			this.api = (API)Bukkit.getPluginManager().getPlugin("API");
		} else {
			Bukkit.getPluginManager().disablePlugin(this);
			System.out.println("!!API NOT FOUND!!");
			
		}
		for (World world : Bukkit.getWorlds()) {
			System.out.println(world.getUID());
		}
		
		Bukkit.getPluginManager().registerEvents(this, this);
		this.queueManager = new QueueManager(this);
		this.arenaManager = new ArenaManager(this);
		this.bukkitListeners = new BukkitListeners(this);
		this.eloManager = new ELOManager(this);

		/* Commands */
		new CreateArenaCommand(this);
		new JoinCommand(this);
		
		/* Kits */
		this.kits = new ArrayList<>();
		this.kits.add(new KitMelee(this));
		this.kits.add(new KitRanged(this));
		this.kits.add(new KitBoth(this));
		
		new QueueRunnable(this).runTaskTimer(this, 0L, 20L * 5);
		
	//	Arena testArena = new Arena();
	//	System.out.println(gson.toJson(testArena));
	}
	
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("play")) {
        	if (args.length>=1) {
        		if (args[0]!=null) {
        			if (args[0].equalsIgnoreCase("debug")) {
        				Player player = (Player)sender;
        				for (Arena arena : this.arenaManager.getArenas()) {
        					Bukkit.broadcastMessage(arena.id + ":" +arena.name + " | active:" + arena.isActive());
        					Bukkit.broadcastMessage("You are " + ((this.queueManager.isPlayerInQueue(player.getUniqueId())?"":"not ") + "in queue."));
        					if (this.queueManager.isPlayerInQueue(player.getUniqueId())) {
        						Date date = this.queueManager.getJoinDate(player.getUniqueId());
        						Bukkit.broadcastMessage("You joined the queue at " + date);
        					}
        				}
        			} else
        			if (args[0].equalsIgnoreCase("join")) {
        				if (sender instanceof Player) {
        					Player player = (Player)sender;
        					if (!this.queueManager.isPlayerInQueue(player.getUniqueId())) {
        						this.queueManager.joinQueue(player.getUniqueId());
        						player.sendMessage("You have joined the queue!");
        					} else {
        						player.sendMessage("You are already in the queue!");
        					}
        				}
        			}
        		}
        	}
        }
        return false;
    }
	
	public static int getSecondsFromDate(Date date) {
		int out = 0;
		Map<TimeUnit, Long> tempTimes = Core.computeDiff(date, new Date());
		for (Entry<TimeUnit, Long> set : tempTimes.entrySet()) {
			if (set.getKey().equals(TimeUnit.SECONDS)) {
				out += set.getValue();
			} else
			if (set.getKey().equals(TimeUnit.MINUTES)) {
				out += set.getValue() * 60;
			} else
			if (set.getKey().equals(TimeUnit.HOURS)) {
				out += (set.getValue() * 60) * 60;
			}
		}
		return out;
	}
	
	public static Map<TimeUnit,Long> computeDiff(Date date1, Date date2) {
    	long diffInMillies = date2.getTime() - date1.getTime();
        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
     	Collections.reverse(units);
     	
     	Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
     	long milliesRest = diffInMillies;
        for ( TimeUnit unit : units ) {
        	long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
        	long diffInMilliesForUnit = unit.toMillis(diff);
        	milliesRest = milliesRest - diffInMilliesForUnit;
            result.put(unit,diff);
        }
        return result;
    }

	public QueueManager getQueueManager() {
		return queueManager;
	}

	public ArenaManager getArenaManager() {
		return arenaManager;
	}

	public Kit getKit(String name) {
		for (Kit kit : this.kits) {
			if (kit.getName().equalsIgnoreCase(name)) {
				return kit;
			}
		}
		return null;
	}
}

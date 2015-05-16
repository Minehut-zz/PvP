package com.minehut.pvp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.Bukkit;
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
	
	private QueueManager queueManager;
	
	@Override
	public void onEnable() {
		if (Bukkit.getPluginManager().getPlugin("API") != null) {
			this.api = (API)Bukkit.getPluginManager().getPlugin("API");
		} else {
			Bukkit.getPluginManager().disablePlugin(this);
			System.out.println("!!API NOT FOUND!!");
			
		}
		Bukkit.getPluginManager().registerEvents(this, this);
		this.queueManager = new QueueManager(this);
		
		
		
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
        				for (Arena arena : this.getArenas()) {
        					Bukkit.broadcastMessage(arena.id + ":" +arena.name + " | active:" + arena.active);
        					Bukkit.broadcastMessage("�cYou are " + ((this.queueManager.isPlayerInQueue(player.getUniqueId())?"":"not ") + "in queue."));
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
        						player.sendMessage("�cYou have joined the queue!");
        					} else {
        						player.sendMessage("�cYou are already in the queue!");
        					}
        				}
        			}
        		}
        	}
        }
        return false;
    }
    
	public ArrayList<Arena> getArenas() {
		try {
			PreparedStatement statement = this.api.getStatManager().getMySQL().getConnection().prepareStatement("select * from `arenas`");
			ResultSet res = statement.executeQuery();
			ArrayList<Arena> arenas = new ArrayList<Arena>();
			while (res.next()) {
				Arena loadedArena = this.gson.fromJson(res.getString("arena_json"), Arena.class);
				loadedArena.id = res.getInt("id");
				arenas.add(loadedArena);
			}
			return arenas;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}

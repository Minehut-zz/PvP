package com.minehut.pvp;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

public class QueueRunnable extends BukkitRunnable {

	private Core core;
	
	public QueueRunnable(Core core) {
		this.core = core;
	}

	@Override
	public void run() {
		for (ArenaType type : ArenaType.values()) {
			System.out.println("Checking for open arenas with the type: " + type.toString());
			ArrayList<Arena> tempArenas = this.core.arenaManager.getEmptyArenasOfType(type); //Less sql calls
			if (tempArenas.size()>=1) {
				ArrayList<QueuePlayer> tempPlayers = this.core.queueManager.getPlayersInQueue(type);
				if (tempPlayers.size()>=2) {
					System.out.println("Found players in " + type.toString() + " queue.");
					
				} else {
					System.out.println("Not enough players in " + type.toString() + " queue.");
				}
			}
		}
	}
	
}

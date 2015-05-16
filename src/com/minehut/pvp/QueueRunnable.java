package com.minehut.pvp;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

public class QueueRunnable extends BukkitRunnable {

	private Core core;
	
	public QueueRunnable(Core core) {
		this.core = core;
	}

	private int teamSize = 1;
	
	@Override
	public void run() {
		for (ArenaType type : ArenaType.values()) {
			System.out.println("Checking for open arenas with the type: " + type.toString());
			ArrayList<Arena> tempArenas = this.core.arenaManager.getEmptyArenasOfType(type); //Less sql calls
			if (tempArenas.size()>=1) {
				ArrayList<QueuePlayer> tempPlayers = this.core.queueManager.getPlayersInQueue(type);
				if (tempPlayers.size()>=2) {
					System.out.println("Found players in " + type.toString() + " queue.");
					int pos = 1;
					for (QueuePlayer qPlayer : tempPlayers) {
						System.out.println(qPlayer.getPlayer().getName() + " has been in the queue for " + qPlayer.getSecondsInQueue() + " seconds, they are in position " + pos + ".");
						
						pos++;
					}
					for (Arena arena : tempArenas ) {
						System.out.println("Starting Arena " + arena.name + "!");
						arena.active = true;
						for (int i = 0; i < teamSize; i++) {
							System.out.println("Adding player " + tempPlayers.get(0).getPlayer().getName() + " to team 1");
							arena.team1.add(tempPlayers.get(0).uuid);
							tempPlayers.get(0).leaveQueue();
							tempPlayers.remove(0);
						}
						for (int i = 0; i < teamSize; i++) {
							System.out.println("Adding player " + tempPlayers.get(0).getPlayer().getName() + " to team 2");
							arena.team2.add(tempPlayers.get(0).uuid);
							tempPlayers.get(0).leaveQueue();
							tempPlayers.remove(0);
						}
						arena.start = System.currentTimeMillis();
						
						System.out.println(new com.google.gson.Gson().toJson(arena));
						
						arena.updateArena();
					}
				} else {
					System.out.println("Not enough players in " + type.toString() + " queue.");
				}
			}
		}
		for (Arena arena : this.core.arenaManager.getArenas()) {
			
		}
	}
	
}

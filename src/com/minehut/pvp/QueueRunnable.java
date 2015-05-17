package com.minehut.pvp;

import java.util.ArrayList;
import java.util.Date;

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
						
						boolean safeArena = true;
						
						for (int i = 0; i < teamSize; i++) {
							System.out.println("Adding player " + tempPlayers.get(0).getPlayer().getName() + " to team 1");
							
							if (tempPlayers.get(0).getPlayer().isOnline()) {
								arena.team1.add(tempPlayers.get(0).uuid);
								
								
								
								arena.joinArena(tempPlayers.get(0).getPlayer().getPlayer());
								
								tempPlayers.get(0).leaveQueue();
								tempPlayers.remove(0);
							} else {
								//TODO: RIP ARENA END AND YOUR TE
								safeArena = false;
							}
							
							
						}
						for (int i = 0; i < teamSize; i++) {
							System.out.println("Adding player " + tempPlayers.get(0).getPlayer().getName() + " to team 2");
							
							if (tempPlayers.get(0).getPlayer().isOnline()) {
								arena.team2.add(tempPlayers.get(0).uuid);
								
								
								
								arena.joinArena(tempPlayers.get(0).getPlayer().getPlayer());
								
								tempPlayers.get(0).leaveQueue();
								tempPlayers.remove(0);
							} else {
								//TODO: RIP ARENA END AND YOUR TE
								safeArena = false;
							}
						}
						arena.start = System.currentTimeMillis();
						
						//System.out.println(new com.google.gson.Gson().toJson(arena));
						if (safeArena) {
							arena.updateArena();
						} else {
							for (QueuePlayer qPlayer : tempPlayers) {
								if (qPlayer.getPlayer().isOnline()) {
									qPlayer.getPlayer().getPlayer().sendMessage("Your match has been closed due to an error, please re-queue!");
								}
							}
						}
						
					}
				} else {
					System.out.println("Not enough players in " + type.toString() + " queue.");
				}
			}
		}
		/*
		for (Arena arena : this.core.arenaManager.getArenas()) {
			if (arena.active && !arena.playing) {
				Date startDate = new Date(arena.start);
				int time = Core.getSecondsFromDate(startDate);
				if (time >= arena.maxWait) {
					
				}
			}
		}*/
	}
	
}

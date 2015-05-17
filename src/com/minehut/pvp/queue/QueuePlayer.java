package com.minehut.pvp.queue;

import java.util.Date;
import java.util.UUID;

import com.minehut.pvp.Core;
import com.minehut.pvp.arena.ArenaType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class QueuePlayer {

	public UUID uuid;
	public long join_time;
	public ArenaType type;
	private QueueManager parentManager;
	
	public QueuePlayer(QueueManager parentManager, UUID uuid, long join_time, ArenaType type) {
		this.parentManager = parentManager;
		this.uuid = uuid;
		this.join_time = join_time;
		this.type = type;
	}
	
	public void joinQueue()  {
		this.parentManager.joinQueue(this.uuid);
	}
	
	public void leaveQueue() {
		this.parentManager.leaveQueue(this.uuid);
	}
	
	public Date getJoinDate() {
		return new Date(this.join_time);
	}
	
	public OfflinePlayer getPlayer() {
		return Bukkit.getOfflinePlayer(this.uuid);
	}
	
	public int getSecondsInQueue() {
		return Core.getSecondsFromDate(this.getJoinDate());
	}
	

	
}

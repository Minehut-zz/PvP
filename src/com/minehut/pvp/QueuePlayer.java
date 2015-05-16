package com.minehut.pvp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

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
		int out = 0;
		Map<TimeUnit, Long> tempTimes = this.computeDiff(this.getJoinDate(), new Date());
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
	
	public Map<TimeUnit,Long> computeDiff(Date date1, Date date2) {
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
	
}

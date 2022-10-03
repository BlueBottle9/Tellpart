package bluescreen9.minecraft.bukkit.tellpart;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class TeleportPoint {//传送点处理
					protected static HashMap<UUID, HashMap<String, Location>> tpPoints = new HashMap<UUID,HashMap<String,Location>>();//传送点
					
					public static HashMap<String, Location> getPoints(Player player) {//得到一个玩家的所有传送点
						if (!tpPoints.containsKey(player.getUniqueId())) {
							return new HashMap<String, Location>();	
						}
						return tpPoints.get(player.getUniqueId());
					}
					
					public static void remove(Player player,String key) {//移除玩家的传送点
								tpPoints.get(player.getUniqueId()).remove(key);
					}
					
					public static void add(Player player,String key,Location loc) {//添加传送点
							if (tpPoints.get(player.getUniqueId()) == null) {
								tpPoints.put(player.getUniqueId(), new HashMap<String, Location>());
							}
								tpPoints.get(player.getUniqueId()).put(key, loc);
					}
					
					public static void add(OfflinePlayer player,String key,Location loc) {//添加传送点(离线玩家),用于加载阶段
						if (tpPoints.get(player.getUniqueId()) == null) {
							tpPoints.put(player.getUniqueId(), new HashMap<String, Location>());
						}
							tpPoints.get(player.getUniqueId()).put(key, loc);
				}
}

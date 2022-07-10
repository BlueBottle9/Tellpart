package bluescreen9.minecraft.bukkit.tellpart;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Data {
				private static HashMap<UUID, Location> homes = new HashMap<UUID, Location>();
				private static byte[] data;
				public static Location getHome(Player player) {
					if (!homes.containsKey(player.getUniqueId())) {
						return null;	
					}
					return homes.get(player.getUniqueId());
				}
				
				public static void setHome(Player player,Location loc) {
						if (homes.containsKey(player.getUniqueId())) {
								homes.replace(player.getUniqueId(), loc);
								return;
						}
						homes.put(player.getUniqueId(), loc);
				}
				
				public static void uploadData() {
					JSONObject json = new JSONObject();
					JSONObject jsonObject = new JSONObject();
					for (UUID uuid:homes.keySet()) {
						JSONObject object = new JSONObject();
						Location loc = homes.get(uuid);
						object.put("world", loc.getWorld().getUID().toString());
						object.put("x", loc.getX());
						object.put("y", loc.getY());
						object.put("z", loc.getZ());
						jsonObject.put(uuid.toString(), object);
					}
					json.put("homes", jsonObject);
					JSONObject jsonObject2 = new JSONObject();
					for (UUID u:TeleportPoint.tpPoints.keySet()) {
						JSONObject object = new JSONObject();
						for (String s:TeleportPoint.tpPoints.get(u).keySet()) {
							Location loc = TeleportPoint.tpPoints.get(u).get(s);
							JSONObject jo = new JSONObject();
							jo.put("world", loc.getWorld().getUID());
							jo.put("x", loc.getX());
							jo.put("y", loc.getY());
							jo.put("z", loc.getZ());
								object.put(s, jo);
						}
						jsonObject2.put(u.toString(), object);
					}
					json.put("teleport-points", jsonObject2);
					data = json.toJSONString().getBytes(Charset.forName("utf-8"));
				}
				
				public static void loadData() {
						try {
							File dataFile = new File(Main.Tellpart.getDataFolder(), "data.json");
							if (!dataFile.exists()) {
								dataFile.createNewFile();
							}
							if (!dataFile.isFile()) {
								dataFile.delete();
								dataFile.createNewFile();
							}
							BufferedInputStream in = new BufferedInputStream(new FileInputStream(dataFile));
							data = in.readAllBytes();
							in.close();
							String str = new String(data, Charset.forName("utf-8"));
							JSONObject json = JSON.parseObject(str);
							if (json == null) {
								return;
							}
							JSONObject jsonObject = json.getJSONObject("homes");
							for (String s:jsonObject.keySet()) {
									JSONObject object = jsonObject.getJSONObject(s);
									Location loc = new Location(Bukkit.getWorld(UUID.fromString(object.getString("world"))), 
											object.getDoubleValue("x"), object.getDoubleValue("y"), object.getDoubleValue("z"));
									homes.put(UUID.fromString(s), loc);
							}
							JSONObject jsonObject2 = json.getJSONObject("teleport-points");
							for (String s:jsonObject2.keySet()) {
								UUID uuid = UUID.fromString(s);
								Player p = Bukkit.getServer().getPlayer(uuid);
								if (p == null) {
									p = Bukkit.getServer().getOfflinePlayer(uuid).getPlayer();
								}
								JSONObject locs = jsonObject2.getJSONObject(uuid.toString());
								for (String key:locs.keySet()) {
										JSONObject object = locs.getJSONObject(key);
										Location loc = new Location(Bukkit.getWorld(UUID.fromString(object.getString("world"))),
												object.getDoubleValue("x"), object.getDoubleValue("y"), object.getDoubleValue("z"));
										TeleportPoint.add(p, key, loc);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
				
				public static void saveData() {
						try {
							File dataFile = new File(Main.Tellpart.getDataFolder(), "data.json");
							if (!dataFile.exists()) {
								dataFile.createNewFile();
							}
							if (!dataFile.isFile()) {
								dataFile.delete();
								dataFile.createNewFile();
							}
							
							BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dataFile));
							out.write(data);
							out.flush();
							out.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
}

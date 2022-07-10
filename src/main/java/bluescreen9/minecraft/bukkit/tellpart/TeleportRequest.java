package bluescreen9.minecraft.bukkit.tellpart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeleportRequest {
				protected static HashMap<UUID, HashMap<UUID, Integer>> tphRequests = new HashMap<UUID, HashMap<UUID, Integer>>();
				protected static HashMap<UUID, HashMap<UUID, Integer>> tptRequests = new HashMap<UUID, HashMap<UUID, Integer>>();
				
				public static void send(Player sender,Player target,TeleportType type) {
					if (type.equals(TeleportType.TPH)) {
						HashMap<UUID, Integer> map = null;
						if (get(sender, type) == null) {
							map = new HashMap<UUID, Integer>();
						}else {
							map = get(target, type);
						}
						map.put(sender.getUniqueId(), Main.Config.getInt("tph.valid-time"));
						tphRequests.put(target.getUniqueId(), map);
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(sender, "tph.request.sender")).
								replaceAll("<%target-name>", target.getName()).replaceAll("<%sender-name>", sender.getName()).
								replaceAll("<%valid-time>", "" + Main.Config.getInt("tph.valid-time")));
						target.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(sender, "tph.request.target")).
								replaceAll("<%target-name>", target.getName()).replaceAll("<%sender-name>", sender.getName()).
								replaceAll("<%valid-time>", "" + Main.Config.getInt("tph.valid-time")));
					}
					if (type.equals(TeleportType.TPT)) {
						HashMap<UUID, Integer> map = null;
						if (get(target, type) == null) {
							map = new HashMap<UUID, Integer>();
						}else {
							map = get(target, type);
						}
						map.put(sender.getUniqueId(), Main.Config.getInt("tpt.valid-time"));
						tptRequests.put(target.getUniqueId(), map);
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(sender, "tpt.request.sender")).
								replaceAll("<%target-name>", target.getName()).replaceAll("<%sender-name>", sender.getName()).
								replaceAll("<%valid-time>", "" + Main.Config.getInt("tpt.valid-time")));
						target.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(sender, "tpt.request.target")).
								replaceAll("<%target-name>", target.getName()).replaceAll("<%sender-name>", sender.getName()).
								replaceAll("<%valid-time>", "" + Main.Config.getInt("tph.valid-time")));
					}
				}
				
				public static HashMap<UUID, Integer> get(Player player,TeleportType type) {
					if (type.equals(TeleportType.TPH)) {
						if (tphRequests.get(player.getUniqueId()) == null) {
							tphRequests.put(player.getUniqueId(), new HashMap<UUID, Integer>());
						}
						return tphRequests.get(player.getUniqueId());
					}
					if (type.equals(TeleportType.TPT)) {
						if (tptRequests.get(player.getUniqueId()) == null) {
							tptRequests.put(player.getUniqueId(), new HashMap<UUID, Integer>());
						}
						return tptRequests.get(player.getUniqueId());
					}
					return new HashMap<UUID, Integer>();
				}
				
				public static List<String> getNames(Player player,TeleportType type) {
					if (type.equals(TeleportType.TPH)) {
						HashMap<UUID, Integer> uuids = tphRequests.get(player.getUniqueId());
						ArrayList<String> names = new ArrayList<String>();
						for (UUID u:uuids.keySet()) {
							names.add(Bukkit.getPlayer(u).getName());
						}
						return names;
					}
					if (type.equals(TeleportType.TPT)) {
						HashMap<UUID, Integer> uuids = tptRequests.get(player.getUniqueId());
						ArrayList<String> names = new ArrayList<String>();
						for (UUID u:uuids.keySet()) {
							names.add(Bukkit.getPlayer(u).getName());
						}

					}
					return null;
				}
				
				public static void remove(Player sender,Player target,TeleportType type) {
						if (type.equals(TeleportType.TPH)) {
							HashMap<UUID, Integer> replace = tphRequests.get(sender.getUniqueId());
							replace.remove(target.getUniqueId());
							tphRequests.replace(sender.getUniqueId(), replace);
						}
						if (type.equals(TeleportType.TPT)) {
							HashMap<UUID, Integer> replace = tptRequests.get(sender.getUniqueId());
							replace.remove(target.getUniqueId());
							tptRequests.replace(sender.getUniqueId(), replace);
						}
							
						}
}

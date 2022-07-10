package bluescreen9.minecraft.bukkit.tellpart;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class HomeCommand implements TabExecutor{
	private static ArrayList<String> complete = new ArrayList<String>();
	static {
		complete.add("set");
	} 
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			return complete;
		}
		return Main.EmptyComplete;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			if (!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(Main.Language.getDefaultLang(), "command.playeronly")));
					return true;
			}
			Player player = (Player) sender;
			if (!Main.Config.getBoolean("home.enable")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.home.disabled")));
				return true;
			}
			if (args.length != 0 && args.length != 1) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.home.wrongsusage")));
				return true;
			}
			if (args.length == 1 && !(args[0].equals("set"))) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.home.wrongsusage")));
				return true;
			}
			
			if (ColdDown.get(player, TeleportType.HOME) > 0) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.home.coldingdown"))
						.replaceAll("<%home-colding>","" + ColdDown.get(player, TeleportType.HOME)));
				return true;
			}
			
			if (args.length == 1) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.home.homeset")));
				Data.setHome(player, player.getLocation());
				return true;
			}
			
			if (args.length == 0) {
				if (Data.getHome(player) == null) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.home.homeunset")));
					return true;
				}
				Teleporter.home(player);
			}
		return true;
	}

}

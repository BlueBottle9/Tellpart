package bluescreen9.minecraft.bukkit.tellpart;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class TPTCommand implements TabExecutor{
	protected static ArrayList<String> complete1 = new ArrayList<String>();
	static {
		complete1.add("to");
		complete1.add("refuse");
		complete1.add("accept");
	}
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			return complete1;
		} 
		if (args.length == 2 && args[0].equals("to")) {
			return null;
		}
		if (args[0].equals("accept") || args[0].equals("refuse")) {
			ArrayList<String> complete = new ArrayList<String>();
			 for (UUID uuid:TeleportRequest.get((Player) sender, TeleportType.TPT).keySet()) {
				 	Player p = Bukkit.getPlayer(uuid);
				 	if (p != null) {
				 		complete.add(p.getName());
				 	}
			 }
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
		if (!Main.Config.getBoolean("tpt.enable")) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tpt.disabled")));
			return true;
		}
		
		if (args.length != 2) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tpt.wrongusage")));
			return true;
		}
		
		if (!TPTCommand.complete1.contains(args[0])) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tpt.wrongusage")));
			return true;
		}
		
		Player target = Bukkit.getPlayer(args[1]);
		if (target == null) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tpt.targetnotexist")).
					replaceAll("<%target-name>", args[1]).replaceAll("<%sender-name>", player.getName()));
			return true;
		}
		
		if (target.getUniqueId().equals(player.getUniqueId())) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tph.tpself")).
					replaceAll("<%target-name>", args[1]).replaceAll("<%sender-name>", player.getName()));
			return true;
		}
		
		if (args[0].equals("to") && TeleportRequest.get(target, TeleportType.TPT).keySet().contains(player.getUniqueId())) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tpt.areadysent")).
					replaceAll("<%target-name>", args[1]).replaceAll("<%sender-name>", player.getName()));
			return true;
		}
		
		if ((args[0].equals("accept") || args[0].equals("refuse")) &&
				(!TeleportRequest.get(player, TeleportType.TPT).keySet().contains(target.getUniqueId()))) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tpt.norequest")).
					replaceAll("<%target-name>", args[1]).replaceAll("<%sender-name>", player.getName()));
			return true;
		}
		
		if (ColdDown.get(player, TeleportType.TPT) > 0) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tpt.coldingdown")).
					replaceAll("<%tpt-colding>", "" + ColdDown.get(player, TeleportType.TPT)));
			return true;
		}
		
		if (args[0].equals("to")) {
			TeleportRequest.send(player, target, TeleportType.TPT);
		}
		
		if (args[0].equals("accept")) {
			Teleporter.tpt(player, target);
		}
		
		if (args[0].equals("refuse")) {
			TeleportRequest.remove(player, target, TeleportType.TPT);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tpt.refuse.sender")).
					replaceAll("<%target-name>", player.getName()).replaceAll("<%sender-name>", target.getName()));
			target.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tpt.refuse.target")).
					replaceAll("<%target-name>", player.getName()).replaceAll("<%sender-name>", target.getName()));
		}
		return true;
	}
}

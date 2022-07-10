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

public class TPHCommand implements TabExecutor{
	
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			return TPTCommand.complete1;
		}
		if (args.length == 2 && args[0].equals("to")) {
			return null;
		}
		if (args[0].equals("accept") || args[0].equals("refuse")) {
			ArrayList<String> complete = new ArrayList<String>();
			 for (UUID uuid:TeleportRequest.get((Player) sender, TeleportType.TPH).keySet()) {
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
		if (!Main.Config.getBoolean("tph.enable")) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tph.disabled")));
			return true;
		}
		
		if (args.length != 2) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tph.wrongusage")));
			return true;
		}
		
		if (!TPTCommand.complete1.contains(args[0])) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tph.wrongusage")));
			return true;
		}
		
		Player target = Bukkit.getPlayer(args[1]);
		if (target == null) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tph.targetnotexist")).
					replaceAll("<%target-name>", args[1]).replaceAll("<%sender-name>", player.getName()));
			return true;
		}
		
		if (target.getUniqueId().equals(player.getUniqueId())) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tph.tpself")).
					replaceAll("<%target-name>", args[1]).replaceAll("<%sender-name>", player.getName()));
			return true;
		}
		
		if (args[0].equals("to") && TeleportRequest.get(target, TeleportType.TPH).keySet().contains(player.getUniqueId())) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tph.areadysent")).
					replaceAll("<%target-name>", args[1]).replaceAll("<%sender-name>", player.getName()));
			return true;
		}
		
		if ((args[0].equals("accept") || args[0].equals("refuse")) &&
				(!TeleportRequest.get(player, TeleportType.TPH).keySet().contains(target.getUniqueId()))) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tph.norequest")).
					replaceAll("<%target-name>", args[1]).replaceAll("<%sender-name>", player.getName()));
			return true;
		}
		
		if (ColdDown.get(player, TeleportType.TPH) > 0) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tph.coldingdown")).
					replaceAll("<%tph-colding>", "" + ColdDown.get(player, TeleportType.TPH)));
			
			return true;
		}
		
		if (args[0].equals("to")) {
			TeleportRequest.send(player, target, TeleportType.TPH);
		}
		
		if (args[0].equals("accept")) {
			Teleporter.tph(player, target);
		}
		
		if (args[0].equals("refuse")) {
			TeleportRequest.remove(player, target, TeleportType.TPH);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tph.refuse.target")).
					replaceAll("<%target-name>", player.getName()).replaceAll("<%sender-name>", target.getName()));
			target.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tph.refuse.sender")).
					replaceAll("<%target-name>", player.getName()).replaceAll("<%sender-name>", target.getName()));
		}
		
		return true;
	}
					
}

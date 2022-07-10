package bluescreen9.minecraft.bukkit.tellpart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class TPPCommand implements TabExecutor{
	
	private static ArrayList<String> complete1 = new ArrayList<String>();
	static {
		complete1.add("set");
		complete1.add("to");
		complete1.add("remove");
	}
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			return complete1;
		}
		if (args.length == 2 && (!args[0].equals("set"))) {
			ArrayList<String> complete = new ArrayList<String>();
			complete.addAll(TeleportPoint.getPoints((Player)sender).keySet());
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
		if (!Main.Config.getBoolean("tpp.enable")) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(Main.Language.getDefaultLang(), "command.tpp.disabled")));
			return true;
		}
		HashMap<String, Location> points = TeleportPoint.getPoints(player);
		if (args.length != 2) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tpp.wrongusage")));
			return true;
		}
		if (!complete1.contains(args[0])) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tpp.wrongusage")));
			return true;
		}
		
		if ((args[0].equals("to")  || args[0].equals("remove")) && !points.containsKey(args[1])) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tpp.pointnotexist")).
					replaceAll("<%point-name>", args[1]));
			return true;
		}
		if (args[0].equals("set") && points.containsKey(args[1])) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tpp.pointalreadyexsit")).
					replaceAll("<%point-name>", args[1]));
			return true;
		}
		if (ColdDown.get(player, TeleportType.TPP) > 0) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tpp.coldingdown")).
					replaceAll("<%tpp-colding>", "" + ColdDown.get(player, TeleportType.TPP)));
			return true;
		}
		
		if (args[0].equals("to")) {
			Teleporter.tpp(player,args[1]);
			return true;
		}
		
		
		if (args[0].equals("set")) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tpp.pointset")).
					replaceAll("<%point-name>", args[1]));
			TeleportPoint.add(player, args[1], player.getLocation());
			return true;
		}
		
		if (args[0].equals("remove")) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tpp.pointremoved")).
					replaceAll("<%point-name>", args[1]));
			TeleportPoint.remove(player, args[1]);
		}
		
		return true;
	}

}

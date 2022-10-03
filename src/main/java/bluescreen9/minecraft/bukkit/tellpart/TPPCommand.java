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

import bluescreen9.minecraft.bukkit.lang.Lang;
import bluescreen9.minecraft.bukkit.lang.MessageProcessor;

public class TPPCommand implements TabExecutor{//TPP 指令
	
	private static ArrayList<String> complete1 = new ArrayList<String>();//补全
	static {
		complete1.add("set");
		complete1.add("to");
		complete1.add("remove");
	}
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			return complete1;
		}
		if (args.length == 2 && (!args[0].equals("set"))) {//返回
			ArrayList<String> complete = new ArrayList<String>();
			complete.addAll(TeleportPoint.getPoints((Player)sender).keySet());
			return complete;
		}
		return Main.EmptyComplete;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, final String[] args) {
		if (!(sender instanceof Player)) {//不是玩家？
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(Main.Language.getDefaultLang(), "command.playeronly")));
			return true;
		}
		final Player player = (Player) sender;
		if (!Main.Config.getBoolean("tpp.enable")) {//未启用？
			Main.Language.sendMessage(player, "command.tpp.disabled", Lang.ColorProcessor);
			return true;
		}
		HashMap<String, Location> points = TeleportPoint.getPoints(player);
		if (args.length != 2) {//用法错误
			Main.Language.sendMessage(player, "command.tpp.wrongusage", Lang.ColorProcessor);
			return true;
		}
		if (!complete1.contains(args[0])) {
			Main.Language.sendMessage(player, "command.tpp.wrongusage", Lang.ColorProcessor);
			return true;
		}
		
		if (Teleporter.Teleporting.containsKey(player.getUniqueId())) {
			Main.Language.sendMessage(player, "command.tpp.alreadyteleporting", Lang.ColorProcessor);
			return true; 
		}
		
		//传送点不存在
		if ((args[0].equals("to")  || args[0].equals("remove")) && !points.containsKey(args[1])) {
			Main.Language.sendMessage(player, "command.tpp.pointnotexist", Lang.ColorProcessor,new MessageProcessor() {
				public String process(String original) {
					return original.replaceAll("<%point-name>", args[1]);
				}
			});
			return true;
		}
		if (args[0].equals("set") && points.containsKey(args[1])) {//传送点已存在
			Main.Language.sendMessage(player, "command.tpp.pointalreadyexsit", Lang.ColorProcessor,new MessageProcessor() {
				public String process(String original) {
					return original.replaceAll("<%point-name>", args[1]);
				}
			});
			return true;
		}
		if (ColdDown.get(player, TeleportType.TPP) > 0) {//冷却中?
			Main.Language.sendMessage(player, "command.tpp.coldingdown", Lang.ColorProcessor,new MessageProcessor() {
				public String process(String original) {
					return original.replaceAll("<%tpp-colding>", "" + ColdDown.get(player, TeleportType.TPP));
				}
			});
			return true;
		}
		
		if (args[0].equals("to")) {
			Teleporter.tpp(player,args[1]);//开始传送
			return true;
		}
		
		
		if (args[0].equals("set")) {
			Main.Language.sendMessage(player, "command.tpp.pointset", Lang.ColorProcessor,new MessageProcessor() {
				public String process(String original) {
					return original.replaceAll("<%point-name>", args[1]);
				}
			});
			TeleportPoint.add(player, args[1], player.getLocation());
			return true;
		}
		
		if (args[0].equals("remove")) {
			Main.Language.sendMessage(player, "command.tpp.pointremoved", Lang.ColorProcessor,new MessageProcessor() {
				public String process(String original) {
					return original.replaceAll("<%point-name>", args[1]);
				}
			});
			TeleportPoint.remove(player, args[1]);
		}
		
		return true;
	}

}

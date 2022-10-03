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

import bluescreen9.minecraft.bukkit.lang.Lang;
import bluescreen9.minecraft.bukkit.lang.MessageProcessor;

public class TPHCommand implements TabExecutor{//TPH指令
	
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

	public boolean onCommand(CommandSender sender, Command command, String label, final String[] args) {
		if (!(sender instanceof Player)) {//不是玩家？
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(Main.Language.getDefaultLang(), "command.playeronly")));
			return true;
		}
		final Player player = (Player) sender;
		if (!Main.Config.getBoolean("tph.enable")) {//未启用
			Main.Language.sendMessage(player, "command.tph.disabled", Lang.ColorProcessor);
			return true;
		}
		
		if (args.length != 2) {//命令正确？
			Main.Language.sendMessage(player, "command.tph.wrongusage", Lang.ColorProcessor);
			return true;
		}
		
		if (!TPTCommand.complete1.contains(args[0])) {
			Main.Language.sendMessage(player, "command.tph.wrongusage", Lang.ColorProcessor);
			return true;
		}
		
		if (Teleporter.Teleporting.containsKey(player.getUniqueId())) {
			Main.Language.sendMessage(player, "command.tpp.alreadyteleporting", Lang.ColorProcessor);
			return true;
		}
		
		Player target = Bukkit.getPlayer(args[1]);
		if (target == null) {//目标传送
			Main.Language.sendMessage(player, "command.tph.targetnotexist", Lang.ColorProcessor,new MessageProcessor() {
				public String process(String original) {
					return original.replaceAll("<%target-name>", args[1]).replaceAll("<%sender-name>", player.getName());
				}
			});
			return true;
		}
		
		if (target.getUniqueId().equals(player.getUniqueId())) {//试图自己传自己？
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tph.tpself")).
					replaceAll("<%target-name>", args[1]).replaceAll("<%sender-name>", player.getName()));
			return true;
		}
		
		if (args[0].equals("to") && TeleportRequest.get(target, TeleportType.TPH).keySet().contains(player.getUniqueId())) {//已经发送过了
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tph.areadysent")).
					replaceAll("<%target-name>", args[1]).replaceAll("<%sender-name>", player.getName()));
			return true;
		}
		//没有请求？
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
		//发送请求
		if (args[0].equals("to")) {
			TeleportRequest.send(player, target, TeleportType.TPH);
		}
		
		if (args[0].equals("accept")) {//接受请求
			Teleporter.tph(target,player);
		}
		
		if (args[0].equals("refuse")) {//拒绝请求
			TeleportRequest.remove(player, target, TeleportType.TPH);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tph.refuse.target")).
					replaceAll("<%target-name>", player.getName()).replaceAll("<%sender-name>", target.getName()));
			target.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tph.refuse.sender")).
					replaceAll("<%target-name>", player.getName()).replaceAll("<%sender-name>", target.getName()));
		}
		
		return true;
	}
					
}

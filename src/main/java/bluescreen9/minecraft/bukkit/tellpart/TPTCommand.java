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
//tpt 指令
public class TPTCommand implements TabExecutor{
	protected static ArrayList<String> complete1 = new ArrayList<String>();//补全
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

	public boolean onCommand(CommandSender sender, Command command, String label, final String[] args) {
		if (!(sender instanceof Player)) {//不是玩家
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(Main.Language.getDefaultLang(), "command.playeronly")));
			return true;
		}
		final Player player = (Player) sender;
		if (!Main.Config.getBoolean("tpt.enable")) {//未启用
			Main.Language.sendMessage(player, "command.tpt.disabled", Lang.ColorProcessor);
			return true;
		}
		
		if (args.length != 2) {//错误语法
			Main.Language.sendMessage(player, "command.tpt.wrongusage", Lang.ColorProcessor);
			return true;
		}
		
		if (!TPTCommand.complete1.contains(args[0])) {
			Main.Language.sendMessage(player, "command.tpt.wrongusage", Lang.ColorProcessor);
			return true;
		}
		
		if (Teleporter.Teleporting.containsKey(player.getUniqueId())) {
			Main.Language.sendMessage(player, "command.tpt.alreadyteleporting", Lang.ColorProcessor);
			return true;
		}
		
		Player target = Bukkit.getPlayer(args[1]);
		if (target == null) {//目标不存在
			Main.Language.sendMessage(player, "command.tpt.targetnotexist", Lang.ColorProcessor,new MessageProcessor() {
				public String process(String original) {
					return original.replaceAll("<%target-name>", args[1]).replaceAll("<%sender-name>", player.getName());
				}
			});
			return true;
		}
		
		if (target.getUniqueId().equals(player.getUniqueId())) {//试图自己传自己
			Main.Language.sendMessage(player, "command.tph.tpself", Lang.ColorProcessor,new MessageProcessor() {
				public String process(String original) {
					return original.replaceAll("<%target-name>", args[1]).replaceAll("<%sender-name>", player.getName());
				}
			});
			return true;
		}
		//已发送请求
		if (args[0].equals("to") && TeleportRequest.get(target, TeleportType.TPT).keySet().contains(player.getUniqueId())) {
			Main.Language.sendMessage(player, "command.tpt.areadysent", Lang.ColorProcessor,new MessageProcessor() {
				public String process(String original) {
					return original.replaceAll("<%target-name>", args[1]).replaceAll("<%sender-name>", player.getName());
				}
			});
			return true;
		}
		//没有请求
		if ((args[0].equals("accept") || args[0].equals("refuse")) &&
				(!TeleportRequest.get(player, TeleportType.TPT).keySet().contains(target.getUniqueId()))) {
			Main.Language.sendMessage(player, "command.tpt.norequest", Lang.ColorProcessor,new MessageProcessor() {
				public String process(String original) {
					return original.replaceAll("<%target-name>", args[1]).replaceAll("<%sender-name>", player.getName());
				}
			});
			return true;
		}
		//冷却中
		if (ColdDown.get(player, TeleportType.TPT) > 0) {
			Main.Language.sendMessage(player, "command.tpt.coldingdown", Lang.ColorProcessor,new MessageProcessor() {
				public String process(String original) {
					return original.replaceAll("<%target-name>", args[1]).replaceAll("<%sender-name>", player.getName());
				}
			});
			return true;
		}
		
		if (args[0].equals("to")) {
			TeleportRequest.send(player, target, TeleportType.TPT);//发送请求
			target.sendRawMessage("{\"text\":\"&a[Tellpart] &e<%sender-name>&a请求传送到你处,点击接受\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tpt accept <%sender-name>\"}}");
		}
		
		if (args[0].equals("accept")) {
			Teleporter.tpt(target, player);//接受请求
		}
		
		if (args[0].equals("refuse")) {//拒绝请求
			TeleportRequest.remove(player, target, TeleportType.TPT);
			Main.Language.sendMessage(player, "command.tpt.refuse.sender", Lang.ColorProcessor,new MessageProcessor() {
				public String process(String original) {
					return original.replaceAll("<%target-name>", args[1]).replaceAll("<%sender-name>", player.getName());
				}
			});
			Main.Language.sendMessage(target, "command.tpt.refuse.target", Lang.ColorProcessor,new MessageProcessor() {
				public String process(String original) {
					return original.replaceAll("<%target-name>", args[1]).replaceAll("<%sender-name>", player.getName());
				}
			});
		}
		return true;
	}
}

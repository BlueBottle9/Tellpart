package bluescreen9.minecraft.bukkit.tellpart;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import bluescreen9.minecraft.bukkit.lang.Lang;
import bluescreen9.minecraft.bukkit.lang.MessageProcessor;
// /tpr 指令
public class TPRCommand implements TabExecutor{

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return Main.EmptyComplete;//空补全
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			if (!(sender instanceof Player)) {//不是玩家
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(Main.Language.getDefaultLang(), "command.playeronly")));
				return true;
			}
			final Player player = (Player) sender;
			if (!Main.Config.getBoolean("tpr.enable")) {//未启用
				Main.Language.sendMessage(player, "command.tpr.disabled", Lang.ColorProcessor);
				return true;
			}
			if (args.length != 0) {//错误语法
				Main.Language.sendMessage(player, "command.tpr.wrongusage", Lang.ColorProcessor);
				return true;
			}
			
			if (Teleporter.Teleporting.containsKey(player.getUniqueId())) {
				Main.Language.sendMessage(player, "command.tpr.alreadyteleporting", Lang.ColorProcessor);
				return true;
			}
			
			if (ColdDown.get(player, TeleportType.TPR) > 0) {//冷却中
				Main.Language.sendMessage(player, "command.tpr.coldingdown", Lang.ColorProcessor,new MessageProcessor() {
					public String process(String original) {
						return original.replaceAll("<%tpr-colding>", "" + ColdDown.get(player, TeleportType.TPR));
					}
				});
				return true;
			}
			Teleporter.tpr(player);//随机传送
		return true;
	}

}

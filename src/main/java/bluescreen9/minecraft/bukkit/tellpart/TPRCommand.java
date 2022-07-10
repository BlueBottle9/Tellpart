package bluescreen9.minecraft.bukkit.tellpart;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class TPRCommand implements TabExecutor{

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return Main.EmptyComplete;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(Main.Language.getDefaultLang(), "command.playeronly")));
				return true;
			}
			Player player = (Player) sender;
			if (!Main.Config.getBoolean("tpr.enable")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tpr.disabled")));
				return true;
			}
			if (args.length != 0) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tpr.wrongusage")));
				return true;
			}
			if (ColdDown.get(player, TeleportType.TPR) > 0) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.tpr.coldingdown")).
						replaceAll("<%tpr-colding>", "" + ColdDown.get(player, TeleportType.TPR)));
				return true;
			}
			Teleporter.tpr(player);
		return true;
	}

}

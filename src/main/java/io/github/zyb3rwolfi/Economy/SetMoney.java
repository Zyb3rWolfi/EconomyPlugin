package io.github.zyb3rwolfi.Economy;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class SetMoney implements CommandExecutor {
    private Economy economy;

    public SetMoney(Economy economy) {
        this.economy = economy;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        try {

            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be run by a player");
                return true;
            }

            String playerName = args[0];
            Player targetPlayer = Bukkit.getServer().getPlayer(playerName);
            if (targetPlayer == null) {
                Player player = (Player) sender;
                economy.sendMessage("Player is not found", player);
                return true;
            }

            int amount;
            amount = Integer.parseInt(args[1]);
            Player player = (Player) sender;

            economy.getDatabaseManager().setPlayerMoney(targetPlayer, amount);

            try {
                int balance = economy.getDatabaseManager().getPlayerBalance(targetPlayer);
                economy.sendMessage("Your balance: " + balance, player);
            } catch (SQLException e) {

            }

        } catch (ArrayIndexOutOfBoundsException e) {
            Player player = (Player) sender;
            economy.sendMessage("/set <name> <amount>", player);
        }

        return true;
    }
}
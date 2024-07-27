package io.github.zyb3rwolfi.Economy;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Objects;

public class GiveMoney implements CommandExecutor {

    private Economy economy;

    public GiveMoney(Economy economy) {
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
                economy.sendMessage("Player is not found!", player);
                return true;
            }

            int amount = Integer.parseInt(args[1]);
            Player player = (Player) sender;

            if (targetPlayer.getUniqueId() == player.getUniqueId()) {
                economy.sendMessage("You cant send money to yourself!", player);
                return true;
            }

            try {
                int balance = economy.getDatabaseManager().getPlayerBalance(player);

                if (balance < amount) {
                    player.sendMessage("You do not have enough money");
                    return true;
                }

                economy.getDatabaseManager().giveMoneyToPlayer(player, targetPlayer, amount);

                economy.sendMessage("You have sent " + amount + " to " + playerName, player);
                economy.sendMessage("You have recieved " + amount + " from " + player.getName(), targetPlayer);

            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e);
            }
            return true;

        } catch (ArrayIndexOutOfBoundsException e) {
            Player player = (Player) sender;
            economy.sendMessage("/give <player> <amount>", player);
            return true;
        }
    }
}
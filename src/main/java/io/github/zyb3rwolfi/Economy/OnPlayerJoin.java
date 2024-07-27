package io.github.zyb3rwolfi.Economy;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class OnPlayerJoin  implements Listener {

    private Economy economy;
    private int startingBalance;
    public OnPlayerJoin(Economy economy, int startingBalance) {
        this.economy = economy; this.startingBalance = startingBalance;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        try {
            economy.getDatabaseManager().addPlayerToDB(player, startingBalance);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e);
        }



    }
}
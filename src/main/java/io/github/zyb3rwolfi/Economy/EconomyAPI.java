package io.github.zyb3rwolfi.Economy;

import org.bukkit.entity.Player;
import io.github.zyb3rwolfi.Economy.ShowBalance;
public interface EconomyAPI {
    double getBalance(Player player);
    double takeMoney(Player player, int amount);
    double giveMoney(Player player, int amount);
}

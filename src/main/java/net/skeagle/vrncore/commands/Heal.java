package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Heal extends SimpleCommand {

    public Heal() {
        super("heal");
        setDescription("Heal another player or yourself.");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    protected void onCommand() {
        if (args.length < 1) {
            checkConsole();
            Player p = getPlayer();
            hasPerm("vrn.heal.self");
            p.setHealth(20);
            p.setFoodLevel(20);
            p.setFireTicks(0);
            say(p, "Your health and hunger are now full.");
            return;
        }
        hasPerm("vrn.heal.others");
        Player a = findPlayer(args[0], VRNcore.noton);
        a.setHealth(20);
        a.setFoodLevel(20);
        a.setFireTicks(0);
        say(a, "Your health and hunger are now full.");
        say(getSender(), "&a" + a.getName() + "&7's health and hunger are now full.");
    }
}

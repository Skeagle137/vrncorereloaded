package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.GUIs.RewardsGUI;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.rewards.Reward;
import net.skeagle.vrnlib.commandmanager.CommandHook;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class RewardCommands {

    @CommandHook("createreward")
    public void onCreateReward(final CommandSender sender, final String name) {
        if (VRNcore.getInstance().getRewardManager().getReward(name) != null) {
            say(sender, "&cA reward with that name already exists.");
            return;
        }
        VRNcore.getInstance().getRewardManager().createReward(name);
        say(sender, "&7Reward created, edit it with &a/reward edit " + name + "&7.");
    }

    @CommandHook("delreward")
    public void onDeleteReward(final CommandSender sender, final Reward r) {
        say(sender, "&7Reward &a" + r.getName() + "&7 successfully deleted.");
        VRNcore.getInstance().getRewardManager().deleteReward(r);
    }

    @CommandHook("editreward")
    public void onEditReward(final Player player, final Reward r) {
        new RewardsGUI(r, player);
    }
}

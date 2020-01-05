package net.skeagle.vrncore.commands.tpa;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.TPAUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Tpaccept extends SimpleCommand {

    TPAUtil util = TPAUtil.getUtil();

    public Tpaccept() {
        super("tpaccept");
        setDescription("Accept a pending teleport request.");
        setPermission("vrn.tpaccept");
        setPermissionMessage(VRNcore.noperm);
    }

    public void onCommand() {
        checkConsole();
        Player p = getPlayer();
        if (!util.hasRequest(p)) {
            say(p, "&cYou do not have any pending teleport requests.");
            return;
        }
        Player a = util.getStoredPlayer(p);
        checkNotNull(a, VRNcore.noton);
        say(p, "&aTeleport request accepted.");
        say(a, "&aTeleport request accepted.");
        util.teleportPlayer(p, a);
    }
}

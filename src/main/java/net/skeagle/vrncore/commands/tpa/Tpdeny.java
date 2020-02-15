package net.skeagle.vrncore.commands.tpa;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.TPAUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Tpdeny extends SimpleCommand {

    private TPAUtil util = TPAUtil.getUtil();

    public Tpdeny() {
        super("tpdeny");
        setDescription("Deny a pending teleport request.");
        setPermission("vrn.tpdeny");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        Player p = getPlayer();
        if (!util.hasRequest(p)) {
            say(p, "&cYou do not have any pending teleport requests.");
            return;
        }
        Player a = util.getStoredPlayer(p);
        checkNotNull(a, VRNcore.noton);
        say(p, "&cDenied the current teleport request from " + a.getName() + ".");
        say(a, "&cYour teleport request was denied.");
        util.DelRequest(p.getUniqueId(), a.getUniqueId(), false);
    }
}

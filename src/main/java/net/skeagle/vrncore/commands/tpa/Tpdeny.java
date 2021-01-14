package net.skeagle.vrncore.commands.tpa;

import net.skeagle.vrncore.utils.TPAUtil;
import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Tpdeny extends SimpleCommand {

    private final TPAUtil util = TPAUtil.getInstance();

    public Tpdeny() {
        super("tpdeny");
        setDescription("Deny a pending teleport request.");
        setPermission("vrn.tpdeny");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        final Player p = getPlayer();
        if (!util.hasRequest(p)) {
            say(p, "&cYou do not have any pending teleport requests.");
            return;
        }
        final Player a = util.getStoredPlayer(p);
        checkNotNull(a, VRNUtil.noton);
        say(p, "&cDenied the current teleport request from " + a.getName() + ".");
        say(a, "&cYour teleport request was denied.");
        util.DelRequest(p.getUniqueId(), a.getUniqueId(), false);
    }
}

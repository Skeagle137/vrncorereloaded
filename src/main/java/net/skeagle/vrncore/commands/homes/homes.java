package net.skeagle.vrncore.commands.homes;

import net.skeagle.vrncore.utils.Resources;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.WarpsHomesUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class homes extends SimpleCommand {
    private final Resources r;
    private final WarpsHomesUtil util;

    public homes(final Resources r) {
        super("homes");
        this.r = r;
        util = new WarpsHomesUtil(r);
        setDescription("List all available homes.");
        setPermission("vrn.homes");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        final FileConfiguration config = this.r.getWarps();
        if (config.get("homes." + getPlayer().getUniqueId()) != null) {
            final List<String> homes = util.returnArray("homes." + getPlayer().getUniqueId());
            final String homeslist = String.join("&7,&a ", homes);
            say(getPlayer(), "&7Currently showing a list of &a" + homes.size() + "&7 home(s): &a" + homeslist + "&7.");
        } else {
            say(getPlayer(),"&cYou do not have any homes available.");
        }
    }
}


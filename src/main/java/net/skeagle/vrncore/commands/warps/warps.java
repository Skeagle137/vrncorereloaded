package net.skeagle.vrncore.commands.warps;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.Resources;
import net.skeagle.vrncore.utils.WarpsHomesUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class warps extends SimpleCommand {
    private Resources r;
    private WarpsHomesUtil util;

    public warps(final Resources r) {
        super("warps");
        this.r = r;
        util = new WarpsHomesUtil(r);
        setDescription("List all available warps.");
        setPermission("vrn.warps");
        setPermissionMessage(VRNcore.noperm);
    }

    public void onCommand() {
        checkConsole();
        FileConfiguration config = this.r.getWarps();
        if (config.get("warps.") != null) {
            List<String> warps = util.returnArray("warps.");
            say(getPlayer(), "&7Currently showing a list of &a" + warps.size() + "&7 warp(s): &a" + String.join("&7,&a ", warps) + "&7.");
        } else {
            say(getPlayer(),"&cThere are currently no warps available.");
        }
    }
}


package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.VRNUtil;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.plugin.SimplePlugin;

import static net.skeagle.vrncore.utils.VRNUtil.say;
import static net.skeagle.vrncore.utils.VRNUtil.sayNoPrefix;

public class Vrn extends SimpleCommand {

    public Vrn() {
        super("Vrn");
        setUsage("[reload]");
        setDescription("Display plugin information and admin options");
        setPermission(null);
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        if (args.length > 1) {
            if ("reload".equals(args[0].toLowerCase())) {
                checkPerm("vrn.reload");
                VRNcore.getInstance().reload();
                say(getSender(), "&aPlugin reloaded.");
            }
            return;
        }
        sayNoPrefix(getSender(), "&9-----------------------------------------------");
        sayNoPrefix(getSender(), "&aVRNcore &7is developed and maintained by &dSkeagle&7.");
        sayNoPrefix(getSender(), "&7This server is currently running version &b" + SimplePlugin.getVersion() + "&7.");
        sayNoPrefix(getSender(), "&7To view the changelog, go to this link:");
        sayNoPrefix(getSender(), "&bhttps://github.com/Skeagle137/vrncorereloaded/commits");
        sayNoPrefix(getSender(), "&9-----------------------------------------------");
    }
}

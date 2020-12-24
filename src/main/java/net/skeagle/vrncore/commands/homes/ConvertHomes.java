package net.skeagle.vrncore.commands.homes;

import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.homes.HomeManager;
import net.skeagle.vrncore.utils.storage.homes.HomesManager;
import net.skeagle.vrncore.utils.storage.homes.HomesResource;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class ConvertHomes extends SimpleCommand {

    public ConvertHomes() {
        super("converthomes");
        setDescription("Convert VRNcore 4.4.x homes to the new format used in VRNcore 4.5.x and above.");
        setPermission("vrn.convert");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        List<HomesManager> managers = HomesResource.getInstance().getManData();
        for (HomesManager man : managers)
            man.homes.forEach(h -> HomeManager.getInstance().importOld(h, man));
        say(getPlayer(), "&7Converted all homes.");
    }
}
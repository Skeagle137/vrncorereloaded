package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.npc.Npc;
import net.skeagle.vrnlib.commandmanager.CommandHook;
import org.bukkit.entity.Player;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class NpcCommands {

    @CommandHook("createnpc")
    public void onCreateNpc(final Player player, final String name) {
        if (VRNcore.getInstance().getNpcManager().getNpc(name) != null) {
            say(player, "&cThat npc name already exists.");
            return;
        }
        VRNcore.getInstance().getNpcManager().createNPC(name, player);
        say(player, "&7NPC created.");
    }

    @CommandHook("deletenpc")
    public void ondeleteNpc(final Player player, final Npc npc) {
        npc.delete();
        say(player, "&7NPC removed.");
    }
}

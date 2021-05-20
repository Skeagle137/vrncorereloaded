package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.utils.storage.npc.NPCResource;
import net.skeagle.vrnlib.commandmanager.CommandHook;
import org.bukkit.entity.Player;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class NpcCommands {

    @CommandHook("createnpc")
    public void onCreateNpc(final Player player, final String name) {
        if (NPCResource.getInstance().addNewNPC(name, player)) {
            NPCResource.getInstance().makeNPC(player, NPCResource.getInstance().getNPC(name));
            say(player, "&7NPC successfully created.");
            return;
        }
        say(player, "&cThat npc name already exists.");
    }

    @CommandHook("deletenpc")
    public void ondeleteNpc(final Player player, final String name) {
        if (NPCResource.getInstance().delNPC(name)) {
            say(player, "&7NPC successfully removed.");
            return;
        }
        say(player, "&cThat npc name already exists.");
    }
}

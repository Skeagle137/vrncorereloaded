package net.skeagle.vrncore.hook;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.MutableContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.track.Track;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public final class LuckPermsHook {

    private final LuckPerms luckperms;

    public LuckPermsHook() {
        RegisteredServiceProvider<LuckPerms> rsp = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        luckperms = rsp.getProvider();
    }

    public void setGroup(Player player, String s) {
        User user = luckperms.getUserManager().getUser(player.getUniqueId());
        if (groupExists(s) && user != null) {
            InheritanceNode node = InheritanceNode.builder(s).value(true).build();
            user.data().clear();
            user.data().add(node);
            luckperms.getUserManager().saveUser(user);
        }
        else {
            say(player, "&cCould not set group. Does the group exist?");
        }
    }

    public void addGroup(Player player, String... groups) {
        User user = luckperms.getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            user.getInheritedGroups(user.getQueryOptions())
                    .addAll(Arrays.stream(groups).map(luckperms.getGroupManager()::getGroup)
                            .filter(Objects::nonNull).collect(Collectors.toList()));
            luckperms.getUserManager().saveUser(user);
        }
    }

    public void promote(Player player, String s) {
        Track track = luckperms.getTrackManager().getTrack(s);
        User user = luckperms.getUserManager().getUser(player.getUniqueId());
        if (track != null && user != null) {
            track.promote(user, MutableContextSet.create());
            luckperms.getUserManager().saveUser(user);
        }
        else {
            say(player, "&cCould not promote. Does the group exist?");
        }
    }

    public boolean groupExists(String s) {
        return luckperms.getGroupManager().getGroup(s) != null;
    }

    public boolean trackExists(String s) {
        return luckperms.getTrackManager().getTrack(s) != null;
    }

    public LuckPerms getLuckperms() {
        return luckperms;
    }
}

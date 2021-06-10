package net.skeagle.vrncore.hook;

import net.milkbowl.vault.chat.Chat;
import net.skeagle.vrncore.config.Settings;
import net.skeagle.vrncore.utils.VRNPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class VaultHook {

    private final Chat chat;

    VaultHook() {
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
    }

    private String getPrefix(Player p) {
        if (!Settings.Chat.multiplePrefix)
            return chat.getPlayerPrefix(p.getWorld().getName(), p);
        List<String> prefixes = new ArrayList<>();
        Arrays.asList(chat.getPlayerGroups(p)).forEach(group -> {
            String prefix = chat.getGroupPrefix(p.getWorld(), group);
            if (prefix != null && !prefix.isEmpty())
                prefixes.add(prefix);
        });
        return String.join(" ", prefixes);
    }

    private String getSuffix(Player p) {
        if (!Settings.Chat.multipleSuffix)
            return chat.getPlayerSuffix(p.getWorld().getName(), p);
        List<String> suffixes = new ArrayList<>();
        Arrays.asList(chat.getPlayerGroups(p)).forEach(group -> {
            String suffix = chat.getGroupSuffix(p.getWorld(), group);
            if (suffix != null && !suffix.isEmpty())
                suffixes.add(suffix);
        });
        return String.join(" ", suffixes);
    }

    private String[] getGroups(Player p) {
        return chat.getPlayerGroups(p);
    }

    public String getName() {
        return chat.getName();
    }

    public String format(Player p) {
        VRNPlayer vrnPlayer = new VRNPlayer(p);
        String s = Settings.Chat.format;
        s = s.replaceAll("%prefix", getPrefix(p));
        s = s.replaceAll("%player", vrnPlayer.getName());
        s = s.replaceAll("%suffix", getSuffix(p));
        s = s.replaceAll("%world", p.getWorld().getName());
        s = s.replaceAll("%group", getGroups(p).length != 0 ? getGroups(p)[0] : "");
        return s;
    }

    public String format(String s, Player p) {
        VRNPlayer vrnPlayer = new VRNPlayer(p);
        s = s.replaceAll("%prefix", getPrefix(p));
        s = s.replaceAll("%player", vrnPlayer.getName());
        s = s.replaceAll("%suffix", getSuffix(p));
        s = s.replaceAll("%world", p.getWorld().getName());
        s = s.replaceAll("%group", getGroups(p).length != 0 ? getGroups(p)[0] : "");
        return s;
    }
}

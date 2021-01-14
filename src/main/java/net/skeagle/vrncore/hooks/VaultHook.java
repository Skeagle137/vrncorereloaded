package net.skeagle.vrncore.hooks;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.milkbowl.vault.chat.Chat;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.settings.Settings;
import net.skeagle.vrncore.utils.HookUtil;
import net.skeagle.vrncore.utils.storage.player.PlayerData;
import net.skeagle.vrncore.utils.storage.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VaultHook {

    @Getter
    private static VaultHook instance;
    private Chat chat;

    public static void load() {
        if (!(HookUtil.isVaultEnabled())) return;
        instance = new VaultHook();
        if (!instance.setupChat()) return;
        VRNcore.getInstance().getLogger().info("Hooked into Vault");
    }

    private String getPrefix(final Player p) {
        if (!Settings.Chat.MULTIPLE_PREFIX)
            return chat.getPlayerPrefix(p.getWorld().getName(), p);
        final List<String> prefixes = new ArrayList<>();
        Arrays.asList(chat.getPlayerGroups(p)).forEach(group -> {
            final String prefix = chat.getGroupPrefix(p.getWorld(), group);
            if (prefix != null && !prefix.isEmpty())
                prefixes.add(prefix);
        });
        return String.join(" ", prefixes);
    }

    private String getSuffix(final Player p) {
        if (!Settings.Chat.MULTIPLE_SUFFIX)
            return chat.getPlayerSuffix(p.getWorld().getName(), p);
        final List<String> suffixes = new ArrayList<>();
        Arrays.asList(chat.getPlayerGroups(p)).forEach(group -> {
            final String suffix = chat.getGroupSuffix(p.getWorld(), group);
            if (suffix != null && !suffix.isEmpty())
                suffixes.add(suffix);
        });
        return String.join(" ", suffixes);
    }

    private String[] getGroups(final Player p) {
        return chat.getPlayerGroups(p);
    }

    public String getName() {
        return chat.getName();
    }

    public String format(final Player p) {
        final PlayerData data = PlayerManager.getData(p);
        String s = Settings.Chat.FORMAT;
        s = s.replaceAll("%prefix", getPrefix(p));
        s = s.replaceAll("%player", data.getNickname() != null ? data.getNickname() + "&r" : p.getName());
        s = s.replaceAll("%suffix", getSuffix(p));
        s = s.replaceAll("%world", p.getWorld().getName());
        s = s.replaceAll("%group", getGroups(p).length != 0 ? getGroups(p)[0] : "");
        return s;
    }

    public String format(String s, final Player p) {
        final PlayerData data = PlayerManager.getData(p);
        s = s.replaceAll("%prefix", getPrefix(p));
        s = s.replaceAll("%player", data.getNickname() != null ? data.getNickname() + "&r" : p.getName());
        s = s.replaceAll("%suffix", getSuffix(p));
        s = s.replaceAll("%world", p.getWorld().getName());
        s = s.replaceAll("%group", getGroups(p).length != 0 ? getGroups(p)[0] : "");
        return s;
    }

    private boolean setupChat() {
        final RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) return false;
        rsp.getProvider();
        chat = rsp.getProvider();
        return chat.isEnabled();
    }
}

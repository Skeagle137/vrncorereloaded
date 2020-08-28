package net.skeagle.vrncore.utils;

import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.getServer;

public class HookUtil {

    public static boolean isVaultEnabled() {
        final Plugin p = getServer().getPluginManager().getPlugin("Vault");
        return p != null && p.isEnabled();
    }
}

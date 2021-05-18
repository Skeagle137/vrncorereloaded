package net.skeagle.vrncore.GUIs;

import org.bukkit.Material;

public enum GivePlusMaterial {
    BARRIER(Material.BARRIER, "Barrier"),
    CMDBLOCK(Material.COMMAND_BLOCK, "Command Block (Normal)"),
    CMD_CHAIN(Material.CHAIN_COMMAND_BLOCK, "Command Block (Chain)"),
    CMD_REPEAT(Material.REPEATING_COMMAND_BLOCK, "Command Block (Repeat)"),
    SPAWNER(Material.SPAWNER, "Spawner"),
    STRUCT_VOID(Material.STRUCTURE_VOID, "Structure Void"),
    STRUCT_BLOCK(Material.STRUCTURE_BLOCK, "Structure Block"),
    DEBUG_STICK(Material.DEBUG_STICK, "Debug Stick");

    private final Material mat;
    private final String name;

    GivePlusMaterial(final Material mat, final String name) {
        this.mat = mat;
        this.name = name;
    }

    public Material getMaterial() {
        return mat;
    }

    public String getName() {
        return name;
    }
}

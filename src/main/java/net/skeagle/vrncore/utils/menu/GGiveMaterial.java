package net.skeagle.vrncore.utils.menu;

import org.mineacademy.fo.remain.CompMaterial;

public enum GGiveMaterial {
    BARRIER(CompMaterial.BARRIER, "Barrier"),
    CMDBLOCK(CompMaterial.COMMAND_BLOCK, "Command Block (Normal)"),
    CMD_CHAIN(CompMaterial.CHAIN_COMMAND_BLOCK, "Command Block (Chain)"),
    CMD_REPEAT(CompMaterial.REPEATING_COMMAND_BLOCK, "Command Block (Repeat)"),
    SPAWNER(CompMaterial.SPAWNER, "Spawner"),
    STRUCT_VOID(CompMaterial.STRUCTURE_VOID, "Structure Void"),
    STRUCT_BLOCK(CompMaterial.STRUCTURE_BLOCK, "Structure Block"),
    DEBUG_STICK(CompMaterial.DEBUG_STICK, "Debug Stick");

    private final CompMaterial mat;
    private final String name;

    GGiveMaterial(final CompMaterial mat, final String name) {
        this.mat = mat;
        this.name = name;
    }

    public CompMaterial getMaterial() {
        return mat;
    }

    public String getName() {
        return name;
    }
}

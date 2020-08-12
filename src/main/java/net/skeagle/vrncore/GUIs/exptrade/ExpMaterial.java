package net.skeagle.vrncore.GUIs.exptrade;

import org.bukkit.Material;

public enum ExpMaterial {
    NETHERITE(0.5, Material.NETHERITE_INGOT), // 1/2
    ANCIENT_DEBRIS(0.25, Material.ANCIENT_DEBRIS), // 1/4
    EMERALD(0.0625, Material.EMERALD), // 1/16
    DIAMOND(0.125, Material.DIAMOND), // 1/8
    GOLD_INGOT(0.025, Material.GOLD_INGOT), // 1/40
    IRON_INGOT(0.03125, Material.IRON_INGOT), //  1/32
    GLOWSTONE_DUST(0.0208, Material.GLOWSTONE_DUST), //  1/48
    NETHER_QUARTZ(0.015, Material.QUARTZ), //  1/64
    LAPIS_LAZULI(0.0104167, Material.LAPIS_LAZULI), // 1/96
    COAL(0.015625, Material.COAL), //  1/128
    REDSTONE_DUST(0.015625, Material.REDSTONE); //  1/128

    private final double worth;
    private final Material icon;

    ExpMaterial(final double worth, final Material icon) {
        this.worth = worth;
        this.icon = icon;
    }

    public double getValue() {
        return worth;
    }

    public Material getIcon() {
        return icon;
    }
}

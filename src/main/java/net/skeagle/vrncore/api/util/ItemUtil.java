package net.skeagle.vrncore.api.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static net.skeagle.vrncore.api.util.VRNUtil.color;

public final class ItemUtil {

    public static ItemUtil.Builder genItem(final Material mat, final String name, final String... lore) {
        return builder().material(mat).name("&r" + name).lore(Arrays.asList(lore)).hideTags(true);
    }

    public static ItemUtil.Builder genItem(final Material mat) {
        return builder().material(mat);
    }

    public static ItemUtil.Builder genItem(final ItemStack item) {
        final ItemUtil.Builder builder = builder();
        final ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.getLore() != null)
            builder.lore(meta.getLore());

        return builder.material(item.getType()).amount(item.getAmount());
    }

    public static ItemUtil.Builder builder() {
        return new ItemUtil.Builder();
    }

    public static class Builder {
        private Material mat;
        private ItemMeta meta;
        private int amount;
        private String name;
        private ArrayList<String> lore;
        private ArrayList<Enchantment> enchants;
        private ArrayList<ItemFlag> flags;
        private boolean unbreakable;
        private boolean hide_tags;
        private boolean glint;

        Builder() {
        }

        public Builder material(final Material mat) {
            this.amount = 1;
            this.mat = mat;
            return this;
        }

        public Builder meta(final ItemMeta meta) {
            this.meta = meta;
            return this;
        }

        public Builder amount(final int amount) {
            this.amount = amount;
            return this;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder lore(final String lore) {
            if (this.lore == null)
                this.lore = new ArrayList<>();

            this.lore.add(lore);
            return this;
        }

        public Builder lore(final Collection<? extends String> lore) {
            if (this.lore == null || lore.isEmpty())
                this.lore = new ArrayList<>();

            this.lore.addAll(lore);
            return this;
        }

        public Builder enchant(final Enchantment enchant) {
            if (this.enchants == null)
                this.enchants = new ArrayList<>();

            this.enchants.add(enchant);
            return this;
        }

        public Builder enchants(final Collection<? extends Enchantment> enchants) {
            if (this.enchants == null)
                this.enchants = new ArrayList<>();

            this.enchants.addAll(enchants);
            return this;
        }

        public Builder flag(final ItemFlag flag) {
            if (this.flags == null)
                this.flags = new ArrayList<>();

            this.flags.add(flag);
            return this;
        }

        public Builder flags(final Collection<? extends ItemFlag> flags) {
            if (this.flags == null)
                this.flags = new ArrayList<>();

            this.flags.addAll(flags);
            return this;
        }

        public Builder unbreakable(final boolean unbreakable) {
            this.unbreakable = unbreakable;
            return this;
        }

        public Builder hideTags(final boolean hide_tags) {
            this.hide_tags = hide_tags;
            return this;
        }

        public Builder glint(final boolean glint) {
            this.glint = glint;
            return this;
        }

        public ItemStack build() {
            final ItemStack i = new ItemStack(mat, amount);
            final ItemMeta im = meta != null ? meta.clone() : i.getItemMeta();
            if (mat == Material.AIR)
                return i;

            if (name != null && !name.equals(""))
                im.setDisplayName(color(name));

            if (lore != null && !lore.isEmpty()) {
                final ArrayList<String> colored = new ArrayList<>();
                for (final String line : lore)
                    colored.add(color(line));

                im.setLore(colored);
            }

            if (enchants != null && !enchants.isEmpty()) {
                for (final Enchantment ench : enchants) {
                    if (im instanceof EnchantmentStorageMeta)
                        ((EnchantmentStorageMeta) im).addStoredEnchant(ench, ench.getStartLevel(), true);
                    else
                        im.addEnchant(ench, ench.getStartLevel(), true);
                }
            }

            if (flags == null)
                flags = new ArrayList<>();

            if (hide_tags) {
                final ItemFlag[] hidden = ItemFlag.values();
                for (final ItemFlag flag : hidden)
                    if (!flags.contains(flag))
                        flags.add(flag);
            }

            if (glint) {
                im.addEnchant(Enchantment.DURABILITY, 1, true);
                flags.add(ItemFlag.HIDE_ENCHANTS);
            }

            if (!flags.isEmpty())
                for (final ItemFlag flag : flags)
                    im.addItemFlags(flag);

            if (unbreakable) {
                flags.add(ItemFlag.HIDE_ATTRIBUTES);
                flags.add(ItemFlag.HIDE_UNBREAKABLE);
            }

            i.setItemMeta(im);
            return i;
        }
    }
}

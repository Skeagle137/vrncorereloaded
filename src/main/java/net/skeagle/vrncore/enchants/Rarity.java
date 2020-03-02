package net.skeagle.vrncore.enchants;

public enum Rarity {
    COMMON("&7Common (1✫)", 0),
    UNCOMMON("&aUncommon (2✫)", 100),
    RARE("&9Rare (3✫)", 250),
    EPIC("&d&oEpic &r&d(4✫)", 600),
    LEGENDARY("&6&lLegendary &r&6(5✫)", 1200),
    MYTHICAL("&5&lMythical &r&5(6✫)", 2500),
    ANCIENT("&4&k:&r&c&lAncient&r&4&k: &r&c(7✫)", 5000),
    COSMIC("&3&l&k:&r&b&lCosmic&r&3&l&k: &r&b(8✫)", 10000);

    private final String rarity;
    private final int pointsRequired;

    Rarity(final String rarity, final int pointsRequired) {
        this.rarity = rarity;
        this.pointsRequired = pointsRequired;
    }

    public String getRarity() {
        return rarity;
    }

    public int getPointsRequired() {
        return pointsRequired;
    }

    public String getRarityFromPoints(final Rarity rarity) {
        for (final Rarity r : Rarity.values()) {
            if (r.getPointsRequired() > rarity.getPointsRequired()) return rarity.getRarity();
        }
        return ANCIENT.getRarity();
    }
}

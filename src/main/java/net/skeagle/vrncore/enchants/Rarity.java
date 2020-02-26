package net.skeagle.vrncore.enchants;

public enum Rarity {
    COMMON("&7Common (1✫)", 0),
    UNCOMMON("&aUncommon (2✫)", 50),
    RARE("&9Rare (3✫)", 100),
    EPIC("&d&oEpic &r&d(4✫)", 200),
    LEGENDARY("&6&lLegendary &r&6(5✫)", 400),
    MYTHICAL("&5&lMythical &r&5(6✫)", 800),
    ANCIENT("&4&l&k:&r&c&lAncient&r&4&l&k: &r&c(7✫)", 1600);

    private String rarity;
    private int pointsRequired;

    Rarity(String rarity, int pointsRequired) {
        this.rarity = rarity;
        this.pointsRequired = pointsRequired;
    }

    public String getRarity() {
        return rarity;
    }

    public int getPointsRequired() {
        return pointsRequired;
    }

    public String getRarityFromPoints(Rarity rarity) {
        for (Rarity r : Rarity.values()) {
            if (r.getPointsRequired() > rarity.getPointsRequired()) return rarity.getRarity();
        }
        return ANCIENT.getRarity();
    }
}

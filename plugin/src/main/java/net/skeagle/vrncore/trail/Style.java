package net.skeagle.vrncore.trail;

import net.skeagle.vrncore.playerdata.TrailData;
import net.skeagle.vrncore.trail.style.*;
import net.skeagle.vrncore.trail.style.TrailStyle;
import net.skeagle.vrncore.trail.style.idle.FivePointStar;
import net.skeagle.vrncore.trail.style.idle.SixPointStar;

public enum Style {
    DEFAULT(Default::new),
    ORBIT(Orbit::new),
    SPREAD(Spread::new),
    CIRCLE(Circle::new),
    FIVE_POINTED_STAR(FivePointStar::new),
    SIX_POINTED_STAR(SixPointStar::new);

    private final StyleProvider provider;

    Style(StyleProvider provider) {
        this.provider = provider;
    }

    public TrailStyle create(TrailData data) {
        return provider.create(data);
    }

    @FunctionalInterface
    interface StyleProvider {
        TrailStyle create(TrailData data);
    }
}

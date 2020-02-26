package net.skeagle.vrncore.enchants;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.mineacademy.fo.exception.CommandException;

import java.util.Random;

public class GenericEvent {
    private double chanceFactor = 0;

    public void checkEntity(Entity e) {
        if (!(e instanceof LivingEntity))
            throw new CommandException();
    }

    public boolean calcChance(double chanceFactor, int level) {
        this.chanceFactor = chanceFactor;
        Random random = new Random();
        return random.nextInt(101) <= calcFactor(level);
    }

    private double calcFactor(int level) {
        if (level < 2) {
            return 1;
        }
        return chanceFactor * (level - 1);
    }
}

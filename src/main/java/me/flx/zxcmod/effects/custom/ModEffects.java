package me.flx.zxcmod.effects.custom;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEffects {

    public static final StatusEffect SOAKING = new Soaking();

    public static void registerEffects() {
        Registry.register(Registries.STATUS_EFFECT, new Identifier("zxcmod", "soaking"), SOAKING);
    }

}

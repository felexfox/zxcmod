package me.flx.zxcmod;

import me.flx.zxcmod.effects.custom.ModEffects;
import me.flx.zxcmod.items.custom.Star;
import me.flx.zxcmod.worlds.WorldListener;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class Zxcmod implements ModInitializer {

    public static final String MOD_ID = "zxcmod";

    public static final Star star = new Star(new FabricItemSettings().maxCount(1).rarity(Rarity.EPIC));

    @Override
    public void onInitialize() {
        ModEffects.registerEffects();
        WorldListener.register();
        Registry.register(Registries.ITEM, new Identifier("zxcmod", "star"), star);
    }

}

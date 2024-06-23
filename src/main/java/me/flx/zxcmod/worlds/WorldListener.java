package me.flx.zxcmod.worlds;

import me.flx.zxcmod.effects.custom.ModEffects;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WorldListener extends WorldEvents {
    private static final Map<UUID, Integer> playerRainTime = new HashMap<>();
    private static final int FREEZE_THRESHOLD = 200;

    public static void register() {
        ServerWorldEvents.LOAD.register(WorldListener::onWorldLoad);
        ServerTickEvents.END_SERVER_TICK.register(WorldListener::onServerTick);
    }

    public static void onWorldLoad(MinecraftServer server, ServerWorld world) {
        world.setWeather(0, 60000000, true, false);
        server.getPlayerManager().broadcast(net.minecraft.text.Text.of("Погода установлена изменена на дождь"), false);
    }
    private static void onServerTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            World world = player.getWorld();
            UUID playerId = player.getUuid();
            BlockPos playerPos = player.getBlockPos();

            boolean isInRain = world.isRaining() && world.isSkyVisible(playerPos);
            boolean isInPowderSnow = world.getBlockState(playerPos).isOf(Blocks.POWDER_SNOW);

            if (isInRain || isInPowderSnow) {
                int rainTime = playerRainTime.getOrDefault(playerId, 0) + 1;
                playerRainTime.put(playerId, rainTime);

                if (rainTime >= FREEZE_THRESHOLD) {
                    applySoakingEffect(player);
                }
            } else {
                if (playerRainTime.getOrDefault(playerId, 0) >= FREEZE_THRESHOLD) {
                    removeFreezingEffect(player);
                }
                playerRainTime.put(playerId, 0);
            }
        }
    }

    private static void applySoakingEffect(ServerPlayerEntity player) {
        StatusEffectInstance slowness = new StatusEffectInstance(ModEffects.SOAKING, 1 + 1 * playerRainTime.get(player.getUuid()), 1, false, true);
        player.addStatusEffect(slowness);
        if (player.getStatusEffects() == ModEffects.SOAKING) {
        }
        player.sendMessage(Text.of("Вы замерзли из-за дождя!"), true);
    }

    private static void removeFreezingEffect(ServerPlayerEntity player) {
        player.sendMessage(Text.of("Вы согрелись!"), true);
    }
}

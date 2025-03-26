package com.ayakacraft.carpetayakaaddition.utils;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameMode;

public final class ServerPlayerUtils {

    private static float wrapDegrees(float degrees) {
        float f = degrees % 360.0F;
        if (f >= 180.0F) {
            return (f - 360.0F);
        }

        if (f < -180.0F) {
            return (f + 360.0F);
        }

        return f;
    }

    public static boolean changeGameMode(ServerPlayerEntity player, GameMode gameMode) {
        //#if MC>=11700
        return player.changeGameMode(gameMode);
        //#else
        //$$ if(player.interactionManager.getGameMode() == gameMode) {
        //$$     return false;
        //$$ }
        //$$ player.setGameMode(gameMode);
        //$$ return true;
        //#endif
    }

    public static float getYaw(ServerPlayerEntity player) {
        //#if MC>=11700
        return wrapDegrees(player.getYaw());
        //#else
        //$$ return wrapDegrees(player.getYaw(1f));
        //#endif
    }

    public static float getPitch(ServerPlayerEntity player) {
        //#if MC>=11700
        return wrapDegrees(player.getPitch());
        //#else
        //$$ return wrapDegrees(player.getPitch(1f));
        //#endif
    }

    public static ServerWorld getServerWorld(ServerPlayerEntity player) {
        //#if MC>=12000
        return player.getServerWorld();
        //#elseif MC>=11800
        //$$ return player.getWorld();
        //#else
        //$$ return player.getServerWorld();
        //#endif
    }

}

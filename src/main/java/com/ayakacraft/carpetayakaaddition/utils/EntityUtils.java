package com.ayakacraft.carpetayakaaddition.utils;

import net.minecraft.entity.Entity;

public final class EntityUtils {

    public static void kill(Entity entity) {
        //#if MC>=12104
        //$$ if (!entity.getWorld().isClient()) {
        //$$     entity.kill((net.minecraft.server.world.ServerWorld)entity.getWorld());
        //$$ }
        //#else
        entity.kill();
        //#endif
    }

}

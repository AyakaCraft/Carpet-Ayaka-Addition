package com.ayakacraft.carpetayakaaddition.mixin.rules.disableBatSpawning;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BatEntity.class)
public class BatEntityMixin {

    @Inject(method = "canSpawn", at = @At("RETURN"), cancellable = true)
    private static void disableBatSpawning(EntityType<BatEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir) {
        if (spawnReason != SpawnReason.SPAWNER && CarpetAyakaSettings.disableBatSpawning && cir.getReturnValue()) {
            cir.setReturnValue(false);
        }
    }

}

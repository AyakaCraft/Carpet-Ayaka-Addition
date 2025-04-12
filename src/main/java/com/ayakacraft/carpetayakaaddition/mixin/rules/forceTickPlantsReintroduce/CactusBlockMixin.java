package com.ayakacraft.carpetayakaaddition.mixin.rules.forceTickPlantsReintroduce;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CactusBlock.class)
public abstract class CactusBlockMixin {

    @Inject(method = "scheduledTick", at = @At("RETURN"))
    private void onScheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (CarpetAyakaSettings.forceTickPlantsReintroduce && state.canPlaceAt(world, pos)) {
            randomTick(state, world, pos, random);
        }
    }

    @Shadow
    protected abstract void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random);

}

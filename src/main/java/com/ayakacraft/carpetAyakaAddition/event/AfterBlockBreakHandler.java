package com.ayakacraft.carpetayakaaddition.event;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static com.ayakacraft.carpetayakaaddition.stats.Statistics.MINED_ALL;

public class AfterBlockBreakHandler implements PlayerBlockBreakEvents.After {

    @Override
    public void afterBlockBreak(World world, PlayerEntity playerEntity, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity) {
        playerEntity.increaseStat(MINED_ALL, 1);
    }

}

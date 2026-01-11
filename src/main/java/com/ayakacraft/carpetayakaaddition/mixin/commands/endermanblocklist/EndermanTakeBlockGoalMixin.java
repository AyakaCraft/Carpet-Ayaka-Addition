/*
 * This file is part of the null project, licensed under the
 * GNU General Public License v3.0
 *
 * Copyright (C) 2026  Calboot and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.ayakacraft.carpetayakaaddition.mixin.commands.endermanblocklist;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition;
import com.ayakacraft.carpetayakaaddition.commands.endermanblocklist.EndermanBlockListConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.io.IOException;

//Do not remove the lines below
//TODO update in 1.17.1
@Mixin(EnderMan.EndermanTakeBlockGoal.class)
public class EndermanTakeBlockGoalMixin {

    @Shadow
    @Final
    private EnderMan enderman;

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"
            )
    )
    private boolean verifyBlockState(BlockState instance, TagKey<Block> tagKey, Operation<Boolean> original) {
        EndermanBlockListConfig config = null;
        try {
            config = EndermanBlockListConfig.get(enderman.level().getServer());
        } catch (IOException e) {
            CarpetAyakaAddition.LOGGER.warn("Failed to get enderman_block_list", e);
        }
        if (config == null) {
            return original.call(instance, tagKey);
        } else {
            boolean b = config.verifyBlock(instance.getBlock());
            if (config.getType() == EndermanBlockListConfig.Type.BLACKLIST_LOOSE) {
                return b;
            } else {
                return b && original.call(instance, tagKey);
            }
        }
    }

}

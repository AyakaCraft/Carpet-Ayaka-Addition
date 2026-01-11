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
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.io.IOException;

@Mixin(EnderMan.EndermanTakeBlockGoal.class)
public class EndermanTakeBlockGoalMixin {

    @Shadow
    @Final
    private EnderMan enderman;

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    //#if MC>=11700
                    target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/Tag;)Z"
                    //#else
                    //$$ target = "Lnet/minecraft/world/level/block/Block;is(Lnet/minecraft/tags/Tag;)Z"
                    //#endif
            )
    )
    private boolean verifyBlockState(
            //#if MC>=11700
            BlockState instance,
            //#else
            //$$ Block instance,
            //#endif
            Tag<Block> tag,
            Operation<Boolean> original
    ) {
        EndermanBlockListConfig config = null;
        try {
            config = EndermanBlockListConfig.get(enderman.level.getServer());
        } catch (IOException e) {
            CarpetAyakaAddition.LOGGER.warn("Failed to get enderman_block_list", e);
        }
        if (config == null) {
            return original.call(instance, tag);
        } else {
            boolean b = config.verifyBlock(
                    //#if MC>=11700
                    instance.getBlock()
                    //#else
                    //$$ instance
                    //#endif
            );
            if (config.getType() == EndermanBlockListConfig.Type.BLACKLIST_LOOSE) {
                return b;
            } else {
                return b && original.call(instance, tag);
            }
        }
    }

}

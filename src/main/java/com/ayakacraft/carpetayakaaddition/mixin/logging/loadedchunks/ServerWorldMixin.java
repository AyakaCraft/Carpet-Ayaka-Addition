/*
 * This file is part of the Carpet Ayaka Addition project, licensed under the
 * GNU General Public License v3.0
 *
 * Copyright (C) 2025  Calboot and contributors
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

package com.ayakacraft.carpetayakaaddition.mixin.logging.loadedchunks;

import com.ayakacraft.carpetayakaaddition.logging.loadedchunks.LoadedChunksLogger;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    @Inject(method = "tickChunk", at = @At("RETURN"))
    private void onTickChunk(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        LoadedChunksLogger.loadedChunksCountAll++;
        //#if MC>=11600
        String string = ((ServerWorld) (Object) this).getRegistryKey().getValue().toString();
        //#else
        //$$ String string = String.valueOf(net.minecraft.world.dimension.DimensionType.getId(((ServerWorld) (Object) this).getDimension().getType()));
        //#endif
        if ("minecraft:overworld".equals(string)) {
            LoadedChunksLogger.loadedChunksCountOverworld++;
        } else if ("minecraft:the_nether".equals(string)) {
            LoadedChunksLogger.loadedChunksCountNether++;
        } else if ("minecraft:the_end".equals(string)) {
            LoadedChunksLogger.loadedChunksCountEnd++;
        }
    }

}

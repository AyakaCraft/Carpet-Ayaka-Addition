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

import com.ayakacraft.carpetayakaaddition.logging.AyakaLoggerRegistry;
import com.ayakacraft.carpetayakaaddition.logging.loadedchunks.LoadedChunksLogger;
import com.ayakacraft.carpetayakaaddition.utils.IdentifierUtils;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {

    @Inject(method = "tickChunk", at = @At("RETURN"))
    private void onTickChunk(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        if (AyakaLoggerRegistry.__loadedChunks) {
            LoadedChunksLogger.INSTANCE.loadedChunksCountAllP++;

            String dim = IdentifierUtils.ofWorld((World) (Object) this).toString();

            if ("minecraft:overworld".equals(dim)) {
                LoadedChunksLogger.INSTANCE.loadedChunksCountOverworldP++;
            } else if ("minecraft:the_nether".equals(dim)) {
                LoadedChunksLogger.INSTANCE.loadedChunksCountNetherP++;
            } else if ("minecraft:the_end".equals(dim)) {
                LoadedChunksLogger.INSTANCE.loadedChunksCountEndP++;
            }
        }
    }

    @Shadow
    public abstract ServerChunkManager getChunkManager();

    @Inject(method = "tick", at = @At("RETURN"))
    private void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (AyakaLoggerRegistry.__loadedChunks) {
            int count = getChunkManager().threadedAnvilChunkStorage.getLoadedChunkCount();

            LoadedChunksLogger.INSTANCE.loadedChunksCountAll += count;

            String dim = IdentifierUtils.ofWorld((World) (Object) this).toString();
            if ("minecraft:overworld".equals(dim)) {
                LoadedChunksLogger.INSTANCE.loadedChunksCountOverworld += count;
            } else if ("minecraft:the_nether".equals(dim)) {
                LoadedChunksLogger.INSTANCE.loadedChunksCountNether += count;
            } else if ("minecraft:the_end".equals(dim)) {
                LoadedChunksLogger.INSTANCE.loadedChunksCountEnd += count;
            }
        }
    }

}

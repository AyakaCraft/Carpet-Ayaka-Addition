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
import net.minecraft.server.world.ChunkTicketManager;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.Identifier;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerChunkManager.class)
public abstract class ServerChunkManagerMixin {

    @Shadow
    @Final
    private ChunkTicketManager ticketManager;

    @Shadow
    @Final
    //#if MC<11700
    //$$ private
    //#elseif MC>=12104
    //$$ private
    //#endif
    ServerWorld world;

    @Shadow
    @Final
    public ThreadedAnvilChunkStorage threadedAnvilChunkStorage;

    @Inject(method = "tickChunks()V", at = @At("RETURN"))
    private void onTickChunks(CallbackInfo ci) {
        if (AyakaLoggerRegistry.__loadedChunks) {
            ThreadedAnvilChunkStorageInvoker tacsi = (ThreadedAnvilChunkStorageInvoker) this.threadedAnvilChunkStorage;

            int   count  = this.threadedAnvilChunkStorage.getLoadedChunkCount();
            int[] countP = {0};

            tacsi.getEntryIterator().forEach(chunkHolder -> {
                WorldChunk worldChunk = chunkHolder.getWorldChunk();
                if (worldChunk != null &&
                        //#if MC>=11800
                        this.ticketManager.shouldTickBlocks(worldChunk.getPos().toLong())
                    //#else
                    //$$ tacsi.whetherTooFarFromPlayersToSpawnMobs(worldChunk.getPos())
                    //#endif
                ) {
                    countP[0]++;
                }
            });

            Identifier id = world.getRegistryKey().getValue();

            LoadedChunksLogger.INSTANCE.loadedChunksCountAll += count;
            LoadedChunksLogger.INSTANCE.loadedChunksCountAllP += countP[0];

            LoadedChunksLogger.INSTANCE.loadedChunksCount.put(id, count);
            LoadedChunksLogger.INSTANCE.loadedChunksCountP.put(id, countP[0]);

        }
    }

}

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

package com.ayakacraft.carpetayakaaddition.logging.loadedchunks;

import com.ayakacraft.carpetayakaaddition.logging.AbstractAyakaHUDLoggerSingleLine;
import com.ayakacraft.carpetayakaaddition.logging.AyakaLoggerRegistry;
import com.ayakacraft.carpetayakaaddition.mixin.logging.loadedchunks.ChunkMapAccessor;
import com.ayakacraft.carpetayakaaddition.utils.InitializedPerTick;
import com.ayakacraft.carpetayakaaddition.utils.text.TextUtils;
import com.ayakacraft.carpetayakaaddition.utils.translation.Translator;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.List;
import java.util.function.Function;

public class LoadedChunksLogger extends AbstractAyakaHUDLoggerSingleLine implements InitializedPerTick {

    private static final String[] OPTIONS = {"all", "dynamic", "overworld", "the_nether", "the_end"};

    private static final short DEFAULT_INDEX = 1;

    private static final String FORMAT = "%d/%d";

    private static final Component SEPARATOR = Component.literal("/").withStyle(ChatFormatting.GRAY);

    private static final ResourceLocation OVW_ID = new ResourceLocation("overworld");

    private static final ResourceLocation NETHER_ID = new ResourceLocation("minecraft:the_nether");

    private static final ResourceLocation END_ID = new ResourceLocation("minecraft:the_end");

    public static final String NAME = "loadedChunks";

    public static final LoadedChunksLogger INSTANCE;

    public static final Translator TR = AyakaLoggerRegistry.LOGGER_TR.resolve(NAME);

    static {
        LoadedChunksLogger i = null;
        try {
            i = new LoadedChunksLogger();
        } catch (NoSuchFieldException ignored) {
        }
        INSTANCE = i;
    }

    private static String getCountString(int count) {
        return count == 0 ? "-" : Integer.toString(count);
    }

    public final Object2IntMap<ResourceLocation> loadedChunksCounts = new Object2IntOpenHashMap<>(3);

    public final Object2IntMap<ResourceLocation> loadedChunksCountsSpawnable = new Object2IntOpenHashMap<>(3);

    public int loadedChunksCountAll = 0;

    public int loadedChunksCountAllSpawnable = 0;

    private LoadedChunksLogger() throws NoSuchFieldException {
        super(NAME, OPTIONS[DEFAULT_INDEX], OPTIONS, false);
    }

    private Component getCountText(ResourceLocation id) {
        MutableComponent t1 = Component.literal(getCountString(loadedChunksCountsSpawnable.getInt(id)));
        MutableComponent t2 = Component.literal(getCountString(loadedChunksCounts.getInt(id)));
        if (OVW_ID.equals(id)) {
            t1.withStyle(ChatFormatting.DARK_GREEN);
            t2.withStyle(ChatFormatting.DARK_GREEN);
        } else if (NETHER_ID.equals(id)) {
            t1.withStyle(ChatFormatting.DARK_RED);
            t2.withStyle(ChatFormatting.DARK_RED);
        } else if (END_ID.equals(id)) {
            t1.withStyle(ChatFormatting.DARK_AQUA);
            t2.withStyle(ChatFormatting.DARK_AQUA);
        }

        return TextUtils.format("{}{}{}", t1, SEPARATOR, t2);
    }

    @Override
    public void init() {
        loadedChunksCountAll = 0;
        loadedChunksCountAllSpawnable = 0;

        loadedChunksCounts.clear();
        loadedChunksCountsSpawnable.clear();
    }

    @Override
    public boolean isEnabled() {
        return AyakaLoggerRegistry.__loadedChunks;
    }

    @Override
    public MutableComponent updateSingleLine(String playerOption, Player player) {
        ServerPlayer sPlayer = (ServerPlayer) player;
        Component    header  = TR.tr(sPlayer, null).withStyle(ChatFormatting.GRAY);
        Component    value;

        if (OPTIONS[1].equals(playerOption)) {
            playerOption = sPlayer.serverLevel().dimension().location().toString();
        }

        if (OPTIONS[0].equals(playerOption)) {
            List<Component> txtList = Lists.newLinkedList();
            txtList.add(header);
            txtList.add(TextUtils.format(FORMAT, loadedChunksCountAllSpawnable, loadedChunksCountAll).withStyle(ChatFormatting.GRAY));
            loadedChunksCounts.keySet().stream().map(this::getCountText).forEach(txtList::add);
            value = TextUtils.join(txtList, TextUtils.space(), Function.identity());
        } else {
            value = TextUtils.format("{} {}", header, getCountText(new ResourceLocation(playerOption)));
        }

        return (MutableComponent) value;
    }

    public void tryLog(ChunkMap chunkMap, ServerLevel world) {
        if (isEnabled()) {
            ChunkMapAccessor chunkMapAccessor = (ChunkMapAccessor) chunkMap;

            int count = chunkMap.size();
            int countSpawnable = (int) chunkMapAccessor.getVisibleChunks$Ayaka().values()
                    .stream()
                    .filter(chunkHolder -> {
                        LevelChunk worldChunk = chunkHolder.getTickingChunk();
                        return worldChunk != null
                                //#if MC>=11800
                                && chunkMapAccessor.shouldTick$Ayaka(worldChunk.getPos())
                                //#else
                                //$$ && !chunkMapAccessor.shouldNotTick$Ayaka(worldChunk.getPos())
                                //#endif
                                ;
                    })
                    .count();

            ResourceLocation id = world.dimension().location();

            loadedChunksCountAll += count;
            loadedChunksCountAllSpawnable += countSpawnable;

            loadedChunksCounts.put(id, count);
            loadedChunksCountsSpawnable.put(id, countSpawnable);

        }
    }

}

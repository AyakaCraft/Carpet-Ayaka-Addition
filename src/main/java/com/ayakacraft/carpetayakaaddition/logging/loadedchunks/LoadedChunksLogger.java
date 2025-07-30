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
import com.ayakacraft.carpetayakaaddition.utils.InitializedPerTick;
import com.ayakacraft.carpetayakaaddition.utils.text.TextUtils;
import com.ayakacraft.carpetayakaaddition.utils.translation.Translator;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Function;

public class LoadedChunksLogger extends AbstractAyakaHUDLoggerSingleLine implements InitializedPerTick {

    private static final String[] OPTIONS = {"all", "dynamic", "overworld", "the_nether", "the_end"};

    private static final short DEFAULT_INDEX = 1;

    private static final String FORMAT = "%d/%d";

    private static final Text SEPARATOR = Text.literal("/").formatted(Formatting.GRAY);

    private static final Identifier OVW_ID = new Identifier("overworld");

    private static final Identifier NETHER_ID = new Identifier("minecraft:the_nether");

    private static final Identifier END_ID = new Identifier("minecraft:the_end");

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

    public final Object2IntMap<Identifier> loadedChunksCounts = new Object2IntOpenHashMap<>(3);

    public final Object2IntMap<Identifier> loadedChunksCountsSpawnable = new Object2IntOpenHashMap<>(3);

    public int loadedChunksCountAll = 0;

    public int loadedChunksCountAllSpawnable = 0;

    private LoadedChunksLogger() throws NoSuchFieldException {
        super(NAME, OPTIONS[DEFAULT_INDEX], OPTIONS, false);
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
    public MutableText updateSingleLine(String playerOption, PlayerEntity player) {
        ServerPlayerEntity sPlayer = (ServerPlayerEntity) player;
        Text               header  = TR.tr(sPlayer, null).formatted(Formatting.GRAY);
        Text               value;

        if (OPTIONS[1].equals(playerOption)) {
            playerOption = sPlayer.getServerWorld().getRegistryKey().getValue().toString();
        }

        if (OPTIONS[0].equals(playerOption)) {
            List<Text> txtList = Lists.newLinkedList();
            txtList.add(header);
            txtList.add(TextUtils.format(FORMAT, loadedChunksCountAllSpawnable, loadedChunksCountAll).formatted(Formatting.GRAY));
            loadedChunksCounts.keySet().stream().map(this::getCountText).forEach(txtList::add);
            value = TextUtils.join(txtList, TextUtils.space(), Function.identity());
        } else {
            value = TextUtils.format("{} {}", header, getCountText(new Identifier(playerOption)));
        }

        return (MutableText) value;
    }

    public Text getCountText(Identifier id) {
        MutableText t1 = Text.literal(getCountString(loadedChunksCountsSpawnable.getInt(id)));
        MutableText t2 = Text.literal(getCountString(loadedChunksCounts.getInt(id)));
        if (OVW_ID.equals(id)) {
            t1.formatted(Formatting.DARK_GREEN);
            t2.formatted(Formatting.DARK_GREEN);
        } else if (NETHER_ID.equals(id)) {
            t1.formatted(Formatting.DARK_RED);
            t2.formatted(Formatting.DARK_RED);
        } else if (END_ID.equals(id)) {
            t1.formatted(Formatting.DARK_AQUA);
            t2.formatted(Formatting.DARK_AQUA);
        }

        return TextUtils.format("{}{}{}", t1, SEPARATOR, t2);
    }

    public String getCountString(int count) {
        return count == 0 ? "-" : Integer.toString(count);
    }

}

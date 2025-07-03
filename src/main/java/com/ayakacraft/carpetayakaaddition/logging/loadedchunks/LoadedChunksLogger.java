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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class LoadedChunksLogger extends AbstractAyakaHUDLoggerSingleLine implements InitializedPerTick {

    private static final String[] OPTIONS = {"all", "dynamic", "overworld", "the_nether", "the_end"};

    private static final short DEFAULT_INDEX = 1;

    private static final String FORMAT = "%d/%d";

    private static final Text SEPARATOR = Text.literal("/").formatted(Formatting.GRAY);

    private static final Identifier OVW_ID = new Identifier("minecraft:overworld");

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

    public final Map<Identifier, Integer> loadedChunksCount = new LinkedHashMap<>(3);

    public final Map<Identifier, Integer> loadedChunksCountP = new LinkedHashMap<>(3);

    public int loadedChunksCountAll = 0;

    public int loadedChunksCountAllP = 0;

    private LoadedChunksLogger() throws NoSuchFieldException {
        super(NAME, OPTIONS[DEFAULT_INDEX], OPTIONS, true);
    }

    @Override
    public void init() {
        loadedChunksCountAll = 0;
        loadedChunksCountAllP = 0;

        loadedChunksCount.clear();
        loadedChunksCountP.clear();

    }

    @Override
    public boolean isEnabled() {
        return AyakaLoggerRegistry.__loadedChunks;
    }

    @Override
    public MutableText updateSingleLine(String playerOption, PlayerEntity player) {
        Text header = TR.tr((ServerPlayerEntity) player, null).formatted(Formatting.GRAY);
        Text value;

        if (OPTIONS[1].equals(playerOption)) {
            playerOption = ((ServerPlayerEntity) player).getServerWorld().getRegistryKey().getValue().getPath();
        }

        if (OPTIONS[0].equals(playerOption)) {
            List<Text> txtList = new LinkedList<>();
            txtList.add(header);
            txtList.add(TextUtils.format(FORMAT, loadedChunksCountAllP, loadedChunksCountAll).formatted(Formatting.GRAY));
            loadedChunksCount.keySet().stream().map(this::getCountText).forEach(txtList::add);
            value = TextUtils.join(txtList, TextUtils.space(), Function.identity());
        } else if (OPTIONS[2].equals(playerOption)) {
            value = TextUtils.format("{} {}", header, getCountText(OVW_ID));
        } else if (OPTIONS[3].equals(playerOption)) {
            value = TextUtils.format("{} {}", header, getCountText(NETHER_ID));
        } else if (OPTIONS[4].equals(playerOption)) {
            value = TextUtils.format("{} {}", header, getCountText(END_ID));
        } else {
            value = null;
        }

        return (MutableText) value;
    }

    public Text getCountText(Identifier id) {
        MutableText t1 = TextUtils.li(loadedChunksCountP.getOrDefault(id, 0));
        MutableText t2 = TextUtils.li(loadedChunksCount.getOrDefault(id, 0));
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

}

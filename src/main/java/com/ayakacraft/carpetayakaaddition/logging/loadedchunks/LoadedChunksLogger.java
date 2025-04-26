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

import com.ayakacraft.carpetayakaaddition.logging.AbstractAyakaHUDLogger;
import com.ayakacraft.carpetayakaaddition.logging.AyakaLoggerRegistry;
import com.ayakacraft.carpetayakaaddition.utils.IdentifierUtils;
import com.ayakacraft.carpetayakaaddition.utils.TextUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class LoadedChunksLogger extends AbstractAyakaHUDLogger {

    public static final String NAME = "loadedChunks";

    private static final String[] OPTIONS = {"all", "dynamic", "overworld", "the_nether", "the_end"};

    private static final short DEFAULT_INDEX = 1;

    public static final LoadedChunksLogger INSTANCE;

    static {
        LoadedChunksLogger i = null;
        try {
            i = new LoadedChunksLogger();
        } catch (NoSuchFieldException ignored) {
        }
        INSTANCE = i;
    }

    public int loadedChunksCountAll = 0;

    public int loadedChunksCountOverworld = 0;

    public int loadedChunksCountNether = 0;

    public int loadedChunksCountEnd = 0;

    public int loadedChunksCountAllP = 0;

    public int loadedChunksCountOverworldP = 0;

    public int loadedChunksCountNetherP = 0;

    public int loadedChunksCountEndP = 0;

    private LoadedChunksLogger() throws NoSuchFieldException {
        super(NAME, OPTIONS[DEFAULT_INDEX], OPTIONS, false);
    }

    @Override
    public void init() {
        loadedChunksCountAll = 0;
        loadedChunksCountOverworld = 0;
        loadedChunksCountNether = 0;
        loadedChunksCountEnd = 0;
        loadedChunksCountAllP = 0;
        loadedChunksCountOverworldP = 0;
        loadedChunksCountNetherP = 0;
        loadedChunksCountEndP = 0;
    }

    @Override
    public boolean isEnabled() {
        return AyakaLoggerRegistry.__loadedChunks;
    }

    private static final String FORMAT = "%d/%d";

    @Override
    @SuppressWarnings("RedundantCast")
    public MutableText[] updateContent(String playerOption, PlayerEntity player) {
        MutableText header = (MutableText) TextUtils.tr("carpet-ayaka-addition.logger.loadedChunks").formatted(Formatting.GRAY);
        Text value;

        if (OPTIONS[1].equals(playerOption)) {
            playerOption = IdentifierUtils.ofWorld(player.getEntityWorld()).getPath();
        }

        if (OPTIONS[0].equals(playerOption)) {
            value = header
                    .append(" ")
                    .append(TextUtils.li(String.format(FORMAT, loadedChunksCountAllP, loadedChunksCountAll)))
                    .append(" ")
                    .append(TextUtils.li(String.format(FORMAT, loadedChunksCountOverworldP, loadedChunksCountOverworld)).formatted(Formatting.DARK_GREEN))
                    .append(" ")
                    .append(TextUtils.li(String.format(FORMAT, loadedChunksCountNetherP, loadedChunksCountNether)).formatted(Formatting.DARK_RED))
                    .append(" ")
                    .append(TextUtils.li(String.format(FORMAT, loadedChunksCountEndP, loadedChunksCountEnd)).formatted(Formatting.DARK_AQUA));
        } else if (OPTIONS[2].equals(playerOption)) {
            value = header
                    .append(" ")
                    .append(TextUtils.li(String.format(FORMAT, loadedChunksCountOverworldP, loadedChunksCountOverworld)).formatted(Formatting.DARK_GREEN));
        } else if (OPTIONS[3].equals(playerOption)) {
            value = header
                    .append(" ")
                    .append(TextUtils.li(String.format(FORMAT, loadedChunksCountNetherP, loadedChunksCountNether)).formatted(Formatting.DARK_RED));
        } else if (OPTIONS[4].equals(playerOption)) {
            value = header
                    .append(" ")
                    .append(TextUtils.li(String.format(FORMAT, loadedChunksCountEndP, loadedChunksCountEnd)).formatted(Formatting.DARK_AQUA));
        } else {
            value = null;
        }

        //noinspection CastCanBeRemovedNarrowingVariableType
        return new MutableText[]{(MutableText) value};
    }

}

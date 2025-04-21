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
import com.ayakacraft.carpetayakaaddition.utils.TextUtils;
import com.ayakacraft.carpetayakaaddition.utils.TickTask;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

public class LoadedChunksLogger extends AbstractAyakaHUDLogger {

    public static final String NAME = "loadedChunks";

    public static final LoadedChunksLogger INSTANCE;

    public static int loadedChunksCountAll = 0;

    public static int loadedChunksCountOverworld = 0;

    public static int loadedChunksCountNether = 0;

    public static int loadedChunksCountEnd = 0;

    public static final TickTask initLoadedChunksCountTask = new TickTask(null) {
        @Override
        public void tick() {
            loadedChunksCountAll = 0;
            loadedChunksCountOverworld = 0;
            loadedChunksCountNether = 0;
            loadedChunksCountEnd = 0;
        }
    };

    static {
        LoadedChunksLogger i = null;
        try {
            i = new LoadedChunksLogger();
        } catch (NoSuchFieldException ignored) {
        }
        INSTANCE = i;
    }

    private LoadedChunksLogger() throws NoSuchFieldException {
        super(NAME, null, new String[0], false);
    }

    @Override
    public MutableText[] onHUDUpdate(String playerOption, PlayerEntity player) {
        return new MutableText[]{
                //#if MC<11904
                //$$ (BaseText)
                //#endif
                TextUtils.li("Loaded Chunks " + loadedChunksCountAll)
                        .append(" ")
                        .append(TextUtils.li(Integer.toString(loadedChunksCountOverworld)).formatted(Formatting.GREEN))
                        .append(" ")
                        .append(TextUtils.li(Integer.toString(loadedChunksCountNether)).formatted(Formatting.RED))
                        .append(" ")
                        .append(TextUtils.li(Integer.toString(loadedChunksCountEnd)).formatted(Formatting.AQUA))
        };
    }

}

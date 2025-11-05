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

package com.ayakacraft.carpetayakaaddition.logging.poi;

import com.ayakacraft.carpetayakaaddition.logging.AbstractAyakaLogger;
import com.ayakacraft.carpetayakaaddition.logging.AyakaLoggerRegistry;
import com.ayakacraft.carpetayakaaddition.utils.StringUtils;
import com.ayakacraft.carpetayakaaddition.utils.translation.Translator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class POILogger extends AbstractAyakaLogger {

    public static final String NAME = "poi";

    public static final POILogger INSTANCE;

    public static final Translator TR = AyakaLoggerRegistry.LOGGER_TR.resolve(NAME);

    static {
        POILogger i = null;
        try {
            i = new POILogger();
        } catch (NoSuchFieldException ignored) {
        }
        INSTANCE = i;
    }

    private POILogger() throws NoSuchFieldException {
        super(NAME, null, new String[0], false);
    }

    private BaseComponent[] doAddedLogging(BlockPos pos, PoiType type, ServerPlayer player) {
        return new BaseComponent[]{TR.tr(
                player,
                "added",
                StringUtils.posString(SectionPos.of(pos)),
                StringUtils.posString(pos),
                type.toString()
        )};
    }

    private BaseComponent[] doRemovedLogging(BlockPos pos, ServerPlayer player) {
        return new BaseComponent[]{TR.tr(
                player,
                "removed",
                StringUtils.posString(SectionPos.of(pos)),
                StringUtils.posString(pos)
        )};
    }

    private BaseComponent[] doTickedReservedLogging(BlockPos pos, PoiType type, int freeTickets, ServerPlayer player) {
        return new BaseComponent[]{TR.tr(
                player,
                "ticket_reserved",
                StringUtils.posString(SectionPos.of(pos)),
                StringUtils.posString(pos),
                type.toString(),
                freeTickets, type.getMaxTickets()
        )};
    }

    private BaseComponent[] doTickedReleasedLogging(BlockPos pos, PoiType type, int freeTickets, ServerPlayer player) {
        return new BaseComponent[]{TR.tr(player,
                "ticket_released",
                StringUtils.posString(SectionPos.of(pos)),
                StringUtils.posString(pos),
                type.toString(),
                freeTickets, type.getMaxTickets()
        )};
    }

    @Override
    public boolean isEnabled() {
        return AyakaLoggerRegistry.__poi;
    }

    public void onAdded(BlockPos pos, PoiType type) {
        if (isEnabled()) {
            log((playerOption, player) -> doAddedLogging(pos, type, (ServerPlayer) player));
        }
    }

    public void onRemoved(BlockPos pos) {
        if (isEnabled()) {
            log(((playerOption, player) -> doRemovedLogging(pos, (ServerPlayer) player)));
        }
    }

    public void onTicketReserved(BlockPos pos, PoiType type, int freeTickets) {
        if (isEnabled()) {
            log(((playerOption, player) -> doTickedReservedLogging(pos, type, freeTickets, (ServerPlayer) player)));
        }
    }

    public void onTicketReleased(BlockPos pos, PoiType type, int freeTickets) {
        if (isEnabled()) {
            log(((playerOption, player) -> doTickedReleasedLogging(pos, type, freeTickets, (ServerPlayer) player)));
        }
    }

}

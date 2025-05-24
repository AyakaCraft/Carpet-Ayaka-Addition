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
import com.ayakacraft.carpetayakaaddition.utils.text.TextUtils;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.poi.PointOfInterestType;

public class POILogger extends AbstractAyakaLogger {

    public static final String NAME = "poi";

    public static final POILogger INSTANCE;

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

    private BaseText[] doAddedLogging(BlockPos pos, PointOfInterestType type, ServerPlayerEntity player) {
        ChunkSectionPos sectionPos = ChunkSectionPos.from(pos);
        return new BaseText[]{TextUtils.tr(player, "carpet-ayaka-addition.logger.poi.added",
                sectionPos.getX(), sectionPos.getY(), sectionPos.getZ(),
                pos.getX(), pos.getY(), pos.getZ(),
                type.toString()
        )};
    }

    private BaseText[] doRemovedLogging(BlockPos pos, ServerPlayerEntity player) {
        ChunkSectionPos sectionPos = ChunkSectionPos.from(pos);
        return new BaseText[]{TextUtils.tr(player, "carpet-ayaka-addition.logger.poi.removed",
                sectionPos.getX(), sectionPos.getY(), sectionPos.getZ(),
                pos.getX(), pos.getY(), pos.getZ()
        )};
    }

    private BaseText[] doTickedReservedLogging(BlockPos pos, PointOfInterestType type, int freeTickets, ServerPlayerEntity player) {
        ChunkSectionPos sectionPos = ChunkSectionPos.from(pos);
        return new BaseText[]{TextUtils.tr(player, "carpet-ayaka-addition.logger.poi.ticket_reserved",
                sectionPos.getX(), sectionPos.getY(), sectionPos.getZ(),
                pos.getX(), pos.getY(), pos.getZ(),
                type.toString(),
                freeTickets, type.getTicketCount()
        )};
    }

    private BaseText[] doTickedReleasedLogging(BlockPos pos, PointOfInterestType type, int freeTickets, ServerPlayerEntity player) {
        ChunkSectionPos sectionPos = ChunkSectionPos.from(pos);
        return new BaseText[]{TextUtils.tr(player, "carpet-ayaka-addition.logger.poi.ticket_released",
                sectionPos.getX(), sectionPos.getY(), sectionPos.getZ(),
                pos.getX(), pos.getY(), pos.getZ(),
                type.toString(),
                freeTickets, type.getTicketCount()
        )};
    }

    @Override
    public boolean isEnabled() {
        return AyakaLoggerRegistry.__poi;
    }

    public void onAdded(BlockPos pos, PointOfInterestType type) {
        if (isEnabled()) {
            log((playerOption, player) -> doAddedLogging(pos, type, (ServerPlayerEntity) player));
        }
    }

    public void onRemoved(BlockPos pos) {
        if (isEnabled()) {
            log(((playerOption, player) -> doRemovedLogging(pos, (ServerPlayerEntity) player)));
        }
    }

    public void onTicketReserved(BlockPos pos, PointOfInterestType type, int freeTickets) {
        if (isEnabled()) {
            log(((playerOption, player) -> doTickedReservedLogging(pos, type, freeTickets, (ServerPlayerEntity) player)));
        }
    }

    public void onTicketReleased(BlockPos pos, PointOfInterestType type, int freeTickets) {
        if (isEnabled()) {
            log(((playerOption, player) -> doTickedReleasedLogging(pos, type, freeTickets, (ServerPlayerEntity) player)));
        }
    }

}

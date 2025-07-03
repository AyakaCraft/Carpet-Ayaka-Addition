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
import com.ayakacraft.carpetayakaaddition.utils.RegistryUtils;
import com.ayakacraft.carpetayakaaddition.utils.StringUtils;
import com.ayakacraft.carpetayakaaddition.utils.translation.Translator;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.PointOfInterestTypeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.HashMap;
import java.util.Map;

//Do not remove the lines below
//TODO update in 1.18.2
public class POILogger extends AbstractAyakaLogger {

    private static final String[] OPTIONS = {"all", "village", "bee_home", "acquirable_job_site"};

    private static final short DEFAULT_INDEX = 0;

    private static final Map<String, TagKey<PointOfInterestType>> POI_TAGS = new HashMap<>(3);

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

        POI_TAGS.put("village", PointOfInterestTypeTags.VILLAGE);
        POI_TAGS.put("bee_home", PointOfInterestTypeTags.BEE_HOME);
        POI_TAGS.put("acquirable_job_site", PointOfInterestTypeTags.ACQUIRABLE_JOB_SITE);
    }

    private POILogger() throws NoSuchFieldException {
        super(NAME, OPTIONS[DEFAULT_INDEX], OPTIONS, true);
    }

    private MutableText[] doAddedLogging(BlockPos pos, RegistryEntry<PointOfInterestType> type, String option, ServerPlayerEntity player) {
        if (OPTIONS[0].equals(option) || type.streamTags().anyMatch(POI_TAGS.get(option)::equals)) {
            return new MutableText[]{TR.tr(
                    player,
                    "added",
                    StringUtils.toString(ChunkSectionPos.from(pos)),
                    StringUtils.toString(pos),
                    RegistryUtils.getIdAsString(type)
            )};
        } else {
            return null;
        }
    }

    private MutableText[] doRemovedLogging(BlockPos pos, ServerPlayerEntity player) {
        return new MutableText[]{TR.tr(
                player,
                "removed",
                StringUtils.toString(ChunkSectionPos.from(pos)),
                StringUtils.toString(pos)
        )};
    }

    private MutableText[] doTickedReservedLogging(BlockPos pos, RegistryEntry<PointOfInterestType> type, int freeTickets, String option, ServerPlayerEntity player) {
        if (OPTIONS[0].equals(option) || type.streamTags().anyMatch(POI_TAGS.get(option)::equals)) {
            return new MutableText[]{TR.tr(
                    player,
                    "ticket_reserved",
                    StringUtils.toString(ChunkSectionPos.from(pos)),
                    StringUtils.toString(pos),
                    RegistryUtils.getIdAsString(type),
                    freeTickets, type.value().ticketCount()
            )};
        } else {
            return null;
        }
    }

    private MutableText[] doTickedReleasedLogging(BlockPos pos, RegistryEntry<PointOfInterestType> type, int freeTickets, String option, ServerPlayerEntity player) {
        if (OPTIONS[0].equals(option) || type.streamTags().anyMatch(POI_TAGS.get(option)::equals)) {
            ChunkSectionPos sectionPos = ChunkSectionPos.from(pos);
            return new MutableText[]{TR.tr(
                    player,
                    "ticket_released",
                    StringUtils.toString(sectionPos),
                    StringUtils.toString(pos),
                    RegistryUtils.getIdAsString(type),
                    freeTickets, type.value().ticketCount()
            )};
        } else {
            return null;
        }
    }

    @Override
    public boolean isEnabled() {
        return AyakaLoggerRegistry.__poi;
    }

    public void onAdded(BlockPos pos, RegistryEntry<PointOfInterestType> type) {
        if (isEnabled()) {
            log((playerOption, player) -> doAddedLogging(pos, type, playerOption, (ServerPlayerEntity) player));
        }
    }

    public void onRemoved(BlockPos pos) {
        if (isEnabled()) {
            log(((playerOption, player) -> doRemovedLogging(pos, (ServerPlayerEntity) player)));
        }
    }

    public void onTicketReserved(BlockPos pos, RegistryEntry<PointOfInterestType> type, int freeTickets) {
        if (isEnabled()) {
            log(((playerOption, player) ->
                    doTickedReservedLogging(pos, type, freeTickets, playerOption, (ServerPlayerEntity) player)
            ));
        }
    }

    public void onTicketReleased(BlockPos pos, RegistryEntry<PointOfInterestType> type, int freeTickets) {
        if (isEnabled()) {
            log(((playerOption, player) ->
                    doTickedReleasedLogging(pos, type, freeTickets, playerOption, (ServerPlayerEntity) player)
            ));
        }
    }

}

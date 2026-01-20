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
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;

import java.util.Map;

//Do not remove the lines below
//TODO update in 1.18.2
public class POILogger extends AbstractAyakaLogger {

    public static final String NAME = "poi";

    public static final POILogger INSTANCE;

    public static final Translator TR = AyakaLoggerRegistry.LOGGER_TR.resolve(NAME);

    private static final String[] OPTIONS = {"all", "village", "bee_home", "acquirable_job_site"};

    private static final short DEFAULT_INDEX = 0;

    private static final Map<String, TagKey<PoiType>> POI_TAGS = Maps.newHashMap();

    static {
        POILogger i = null;
        try {
            i = new POILogger();
        } catch (NoSuchFieldException ignored) {
        }
        INSTANCE = i;

        POI_TAGS.put("village", PoiTypeTags.VILLAGE);
        POI_TAGS.put("bee_home", PoiTypeTags.BEE_HOME);
        POI_TAGS.put("acquirable_job_site", PoiTypeTags.ACQUIRABLE_JOB_SITE);
    }

    private POILogger() throws NoSuchFieldException {
        super(NAME, OPTIONS[DEFAULT_INDEX], OPTIONS, true);
    }

    private MutableComponent[] doAddedLogging(BlockPos pos, Holder<PoiType> type, String option, ServerPlayer player) {
        if (OPTIONS[0].equals(option) || type.tags().anyMatch(POI_TAGS.get(option)::equals)) {
            return new MutableComponent[]{TR.tr(
                    player,
                    "added",
                    StringUtils.posString(SectionPos.of(pos)),
                    StringUtils.posString(pos),
                    RegistryUtils.getRegisteredName(type)
            )};
        } else {
            return null;
        }
    }

    private MutableComponent[] doRemovedLogging(BlockPos pos, ServerPlayer player) {
        return new MutableComponent[]{TR.tr(
                player,
                "removed",
                StringUtils.posString(SectionPos.of(pos)),
                StringUtils.posString(pos)
        )};
    }

    private MutableComponent[] doTickedReservedLogging(BlockPos pos, Holder<PoiType> type, int freeTickets, String option, ServerPlayer player) {
        if (OPTIONS[0].equals(option) || type.tags().anyMatch(POI_TAGS.get(option)::equals)) {
            return new MutableComponent[]{TR.tr(
                    player,
                    "ticket_reserved",
                    StringUtils.posString(SectionPos.of(pos)),
                    StringUtils.posString(pos),
                    RegistryUtils.getRegisteredName(type),
                    freeTickets, type.value().maxTickets()
            )};
        } else {
            return null;
        }
    }

    private MutableComponent[] doTickedReleasedLogging(BlockPos pos, Holder<PoiType> type, int freeTickets, String option, ServerPlayer player) {
        if (OPTIONS[0].equals(option) || type.tags().anyMatch(POI_TAGS.get(option)::equals)) {
            SectionPos sectionPos = SectionPos.of(pos);
            return new MutableComponent[]{TR.tr(
                    player,
                    "ticket_released",
                    StringUtils.posString(sectionPos),
                    StringUtils.posString(pos),
                    RegistryUtils.getRegisteredName(type),
                    freeTickets, type.value().maxTickets()
            )};
        } else {
            return null;
        }
    }

    @Override
    public boolean isEnabled() {
        return AyakaLoggerRegistry.__poi;
    }

    public void onAdded(BlockPos pos, Holder<PoiType> type) {
        if (isEnabled()) {
            log((playerOption, player) -> doAddedLogging(pos, type, playerOption, (ServerPlayer) player));
        }
    }

    public void onRemoved(BlockPos pos) {
        if (isEnabled()) {
            log(((playerOption, player) -> doRemovedLogging(pos, (ServerPlayer) player)));
        }
    }

    public void onTicketReserved(BlockPos pos, Holder<PoiType> type, int freeTickets) {
        if (isEnabled()) {
            log(((playerOption, player) ->
                    doTickedReservedLogging(pos, type, freeTickets, playerOption, (ServerPlayer) player)
            ));
        }
    }

    public void onTicketReleased(BlockPos pos, Holder<PoiType> type, int freeTickets) {
        if (isEnabled()) {
            log(((playerOption, player) ->
                    doTickedReleasedLogging(pos, type, freeTickets, playerOption, (ServerPlayer) player)
            ));
        }
    }

}

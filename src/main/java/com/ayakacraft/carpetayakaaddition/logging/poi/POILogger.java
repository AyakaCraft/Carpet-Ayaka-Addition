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
import com.ayakacraft.carpetayakaaddition.utils.TextUtils;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.HashMap;
import java.util.Map;

public class POILogger extends AbstractAyakaLogger {

    //#if MC>=11900
    private static final String[] OPTIONS = {"all", "village", "bee_home", "acquirable_job_site"};
    //#endif

    //#if MC>=11900
    private static final short DEFAULT_INDEX = 0;
    //#endif

    //#if MC>=11900
    private static final Map<String, net.minecraft.registry.tag.TagKey<PointOfInterestType>> POI_TAGS = new HashMap<>(3);
    //#endif

    public static final String NAME = "poi";

    public static final POILogger INSTANCE;

    static {
        POILogger i = null;
        try {
            i = new POILogger();
        } catch (NoSuchFieldException ignored) {
        }
        INSTANCE = i;

        //#if MC>=11900
        POI_TAGS.put("village", net.minecraft.registry.tag.PointOfInterestTypeTags.VILLAGE);
        POI_TAGS.put("bee_home", net.minecraft.registry.tag.PointOfInterestTypeTags.BEE_HOME);
        POI_TAGS.put("acquirable_job_site", net.minecraft.registry.tag.PointOfInterestTypeTags.ACQUIRABLE_JOB_SITE);
        //#endif
    }

    private POILogger() throws NoSuchFieldException {
        //#if MC>=11900
        super(NAME, OPTIONS[DEFAULT_INDEX], OPTIONS, true);
        //#else
        //$$ super(NAME, null, new String[0], false);
        //#endif
    }

    //#if MC>=11900
    private MutableText[] doAddedLogging(BlockPos pos, net.minecraft.registry.entry.RegistryEntry<PointOfInterestType> type, String option) {
        if (OPTIONS[0].equals(option) || type.streamTags().anyMatch(POI_TAGS.get(option)::equals)) {
            ChunkSectionPos sectionPos = ChunkSectionPos.from(pos);
            return new MutableText[]{TextUtils.tr("carpet-ayaka-addition.logger.poi.added",
                    sectionPos.getX(), sectionPos.getY(), sectionPos.getZ(),
                    pos.getX(), pos.getY(), pos.getZ(),
                    RegistryUtils.getIdAsString(type)
            )};
        } else {
            return null;
        }
    }
    //#else
    //$$ private BaseText[] doAddedLogging(BlockPos pos, PointOfInterestType type, String option) {
    //$$     ChunkSectionPos sectionPos = ChunkSectionPos.from(pos);
    //$$     return new BaseText[]{TextUtils.tr("carpet-ayaka-addition.logger.poi.added",
    //$$             sectionPos.getX(), sectionPos.getY(), sectionPos.getZ(),
    //$$             pos.getX(), pos.getY(), pos.getZ(),
    //$$             type.toString()
    //$$     )};
    //$$ }
    //#endif

    private MutableText[] doRemovedLogging(BlockPos pos) {
        ChunkSectionPos sectionPos = ChunkSectionPos.from(pos);
        return new MutableText[]{TextUtils.tr("carpet-ayaka-addition.logger.poi.removed",
                sectionPos.getX(), sectionPos.getY(), sectionPos.getZ(),
                pos.getX(), pos.getY(), pos.getZ()
        )};
    }

    @Override
    public boolean isEnabled() {
        return AyakaLoggerRegistry.__poi;
    }

    public void onAdded(
            BlockPos pos,
            //#if MC>=11900
            net.minecraft.registry.entry.RegistryEntry<PointOfInterestType> type
            //#else
            //$$ PointOfInterestType type
            //#endif
    ) {
        if (isEnabled()) {
            log((playerOption, player) -> doAddedLogging(pos, type, playerOption));
        }
    }

    public void onRemoved(BlockPos pos) {
        if (isEnabled()) {
            log((playerOption -> doRemovedLogging(pos)));
        }
    }

}

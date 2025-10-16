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

package com.ayakacraft.carpetayakaaddition.utils;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.Optional;

public final class ModUtils {

    public static final FabricLoader LOADER = FabricLoader.getInstance();

    public static final String TIS_ID = "carpet-tis-addition";

    public static final String GCA_ID = "gca";

    public static final String MC_ID = "minecraft";

    public static final String CPT_ID = "carpet";

    @Contract(pure = true)
    public static boolean isModLoaded(String modId) {
        return getModContainer(modId).isPresent();
    }

    @Contract(pure = true)
    public static Optional<ModContainer> getModContainer(String modId) {
        return LOADER.getModContainer(modId);
    }

    @Contract(pure = true)
    public static boolean isModLoadedWithVersion(String modId, String... versionPredicates) {
        Optional<ModContainer> mod = getModContainer(modId);
        return mod
                .filter(modContainer ->
                        versionPredicates == null
                                || versionPredicates.length == 0
                                || Arrays.stream(versionPredicates).anyMatch(v -> {
                            try {
                                return VersionPredicate.parse(v).test(modContainer.getMetadata().getVersion());
                            } catch (VersionParsingException e) {
                                return false;
                            }
                        }))
                .isPresent();
    }

}

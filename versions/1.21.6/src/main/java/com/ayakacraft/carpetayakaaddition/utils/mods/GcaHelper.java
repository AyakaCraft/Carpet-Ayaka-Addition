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

package com.ayakacraft.carpetayakaaddition.utils.mods;

import carpet.patches.EntityPlayerMPFake;
import com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.dubhe.gugle.carpet.GcaSetting;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.NbtWriteView;
import net.minecraft.util.ErrorReporter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition.LOGGER;

public final class GcaHelper {

    private static final Method savePlayerMethod;

    static {
        Method spm = null;
        try {
            Optional<ModContainer> o = ModUtils.getModContainer(ModUtils.GCA_ID);
            if (o.isPresent()) {
                ClassLoader classLoader = GcaHelper.class.getClassLoader();
                Class<?>    clazz;
                try {
                    clazz = classLoader.loadClass("dev.dubhe.gugle.carpet.tools.FakePlayerResident");
                } catch (ClassNotFoundException e) {
                    clazz = classLoader.loadClass("dev.dubhe.gugle.carpet.tools.player.FakePlayerResident");
                }
                spm = clazz.getDeclaredMethod("save", PlayerEntity.class);
                spm.setAccessible(true);
            } else {
                LOGGER.warn("GCA not loaded, fakePlayerResidentBackupFix won't be activated");
            }
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            LOGGER.warn("Failed to load GCA, fakePlayerResidentBackupFix won't be activated", e);
        }
        savePlayerMethod = spm;
    }

    private static JsonElement invokeSavePlayer(ServerPlayerEntity player) {
        try {
            if (savePlayerMethod != null) {
                return (JsonElement) savePlayerMethod.invoke(null, player);
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOGGER.warn("Failed to invoke save player method in GCA, fakePlayerResidentBackupFix might not work", e);
        }
        return null;
    }

    public static void storeFakesIfNeeded(MinecraftServer server) {
        if (!GcaSetting.fakePlayerResident || server.isStopped() || savePlayerMethod == null) {
            return;
        }

        LOGGER.debug("Saving fake players");

        JsonObject fakePlayerList = new JsonObject();

        server.getPlayerManager()
                .getPlayerList()    // We don't need to ensure that the players are not logged out as the server is not closed yet
                .stream()
                .filter(player -> {
                    if (!(player instanceof EntityPlayerMPFake)) {
                        return false;
                    }
                    try (ErrorReporter.Logging reporter = new ErrorReporter.Logging(player.getErrorReporterContext(), LOGGER)) {
                        try {
                            NbtWriteView valueOutput = NbtWriteView.create(reporter, player.getRegistryManager());
                            player.writeData(valueOutput);
                            return !valueOutput.getNbt().contains("gca.NoResident");
                        } catch (Exception e) {
                            try {
                                reporter.close();
                            } catch (Exception t) {
                                e.addSuppressed(t);
                            }
                            throw e;
                        }
                    }
                })
                .forEach(p -> fakePlayerList.add(p.getName().getString(), invokeSavePlayer(p)));

        Path path = server.getSavePath(net.minecraft.util.WorldSavePath.ROOT).resolve("fake_player.gca.json");
        try {
            Files.write(path, CarpetAyakaAddition.GSON.toJson(fakePlayerList).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}

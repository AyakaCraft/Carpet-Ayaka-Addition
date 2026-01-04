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

package com.ayakacraft.carpetayakaaddition.helpers.mods;

import carpet.patches.EntityPlayerMPFake;
import com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.dubhe.gugle.carpet.GcaSetting;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition.LOGGER;

public final class GcaHelper {

    //#if MC<12105
    private static final java.lang.reflect.Method savePlayerMethod;
    //#endif

    //#if MC<12105
    static {
        java.lang.reflect.Method spm = null;
        try {
            ClassLoader classLoader = GcaHelper.class.getClassLoader();
            Class<?>    clazz;
            try {
                clazz = classLoader.loadClass("dev.dubhe.gugle.carpet.tools.FakePlayerResident");
            } catch (ClassNotFoundException e) {
                clazz = classLoader.loadClass("dev.dubhe.gugle.carpet.tools.player.FakePlayerResident");
            }
            spm = clazz.getDeclaredMethod("save", net.minecraft.world.entity.player.Player.class);
            spm.setAccessible(true);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            LOGGER.warn("Failed to load GCA, fakePlayerResidentBackupFix won't be activated", e);
        }
        savePlayerMethod = spm;
    }
    //#endif

    private static boolean doPlayerNeedResident(ServerPlayer player) {
        //#if MC>=12106
        //$$ if (!(player instanceof EntityPlayerMPFake)) {
        //$$     return false;
        //$$ }
        //$$ try (net.minecraft.util.ProblemReporter.ScopedCollector reporter = new net.minecraft.util.ProblemReporter.ScopedCollector(player.problemPath(), LOGGER)) {
        //$$     try {
        //$$         net.minecraft.world.level.storage.TagValueOutput valueOutput = net.minecraft.world.level.storage.TagValueOutput.createWithContext(reporter, player.registryAccess());
        //$$         player.saveWithoutId(valueOutput);
        //$$         return !valueOutput.buildResult().contains("gca.NoResident");
        //$$     } catch (Exception e) {
        //$$         try {
        //$$             reporter.close();
        //$$         } catch (Exception t) {
        //$$             e.addSuppressed(t);
        //$$         }
        //$$         throw e;
        //$$     }
        //$$ }
        //#else
        return player instanceof EntityPlayerMPFake && !player.saveWithoutId(new net.minecraft.nbt.CompoundTag()).contains("gca.NoResident");
        //#endif
    }

    private static JsonElement invokeSavePlayer(ServerPlayer player) {
        //#if MC>=12105
        //$$ return dev.dubhe.gugle.carpet.tools.player.FakePlayerResident.save(player);
        //#else
        try {
            if (savePlayerMethod != null) {
                return (JsonElement) savePlayerMethod.invoke(null, player);
            }
        } catch (java.lang.reflect.InvocationTargetException | IllegalAccessException e) {
            LOGGER.warn("Failed to invoke save player method in GCA, fakePlayerResidentBackupFix might not work", e);
        }
        return null;
        //#endif
    }

    public static void storeFakesIfNeeded(MinecraftServer server) {
        if (!GcaSetting.fakePlayerResident || server.isStopped()
                //#if MC<12105
                || savePlayerMethod == null
                //#endif
        ) {
            return;
        }

        LOGGER.debug("Saving fake players");

        JsonObject fakePlayers = new JsonObject();

        server.getPlayerList()
                .getPlayers()    // We don't need to ensure that the players are not logged out as the server is not closed yet
                .stream()
                .filter(GcaHelper::doPlayerNeedResident)
                .forEach(p -> {
                    JsonElement playerJson = invokeSavePlayer(p);
                    if (playerJson != null) {
                        fakePlayers.add(p.getName().getString(), playerJson);
                    }
                });

        Path path = server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT).resolve("fake_player.gca.json");
        try {
            Files.write(path, CarpetAyakaAddition.GSON.toJson(fakePlayers).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}

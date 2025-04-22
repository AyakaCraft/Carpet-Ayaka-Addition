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
import com.ayakacraft.carpetayakaaddition.utils.ServerUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.dubhe.gugle.carpet.GcaSetting;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.File;
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
        Method spm;
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
                spm = null;
            }
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            LOGGER.warn("Failed to load GCA, fakePlayerResidentBackupFix won't be activated", e);
            spm = null;
        }
        savePlayerMethod = spm;
    }

    private static JsonElement invokeSavePlayer(ServerPlayerEntity player) {
        try {
            if (savePlayerMethod != null) {
                return (JsonElement) savePlayerMethod.invoke(null, player);
            }
            return null;
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
                .filter(player ->
                        player instanceof EntityPlayerMPFake && !player.writeNbt(new NbtCompound()).contains("gca.NoResident"))
                .forEach(p -> fakePlayerList.add(p.getName().getString(), invokeSavePlayer(p)));

        Path path = ServerUtils.worldRootPath(server).resolve("fake_player.gca.json");
        File file = path.toFile();
        if (!file.isFile()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        try
        //(BufferedWriter bfw = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8))
        {
            //bfw.write(CarpetAyakaAddition.GSON.toJson(fakePlayerList));
            Files.write(path, CarpetAyakaAddition.GSON.toJson(fakePlayerList).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}

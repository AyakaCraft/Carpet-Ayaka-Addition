/*
 * This file is part of the Carpet Ayaka Addition project, licensed under the
 * GNU General Public License v3.0
 *
 * Copyright (C) 2025  Calboot
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

package com.ayakacraft.carpetayakaaddition.utils.waypoint;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition;
import com.ayakacraft.carpetayakaaddition.utils.ServerUtils;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import static com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition.LOGGER;

public class WaypointManager {

    private static final Type COLLECTION_TYPE = new TypeToken<Collection<Waypoint>>() {
    }.getType();

    private static final HashMap<MinecraftServer, WaypointManager> managerMap = new HashMap<>(1);

    public static final String WAYPOINT_FILE_NAME = "ayaka_waypoints.json";

    public static WaypointManager getOrCreate(MinecraftServer server) {
        if (managerMap.containsKey(server)) {
            return managerMap.get(server);
        }
        final WaypointManager instance = new WaypointManager(server);
        managerMap.put(server, instance);
        return instance;
    }

    public static void removeWaypointManager(MinecraftServer server) {
        if (!managerMap.containsKey(server)) {
            return;
        }
        try {
            managerMap.remove(server).save();
        } catch (IOException e) {
            LOGGER.error("Failed to save waypoints", e);
        }
    }

    private final Path waypointStoragePath;

    private final LinkedHashMap<String, Waypoint> waypointMap = new LinkedHashMap<>(3);

    private WaypointManager(MinecraftServer server) {
        waypointStoragePath = ServerUtils.worldRootPath(server).resolve(WAYPOINT_FILE_NAME);
        try {
            load();
        } catch (IOException e) {
            LOGGER.error("Failed to load waypoints", e);
        }
    }

    private Waypoint put(Waypoint waypoint) {
        return waypointMap.put(waypoint.getId(), waypoint);
    }

    public void load() throws IOException {
        LOGGER.debug("Loading waypoints from {}", waypointStoragePath);
        if (Files.notExists(waypointStoragePath) || !Files.isRegularFile(waypointStoragePath)) {
            Files.createFile(waypointStoragePath);
        }
        final String str = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(Files.readAllBytes(waypointStoragePath))).toString();

        waypointMap.clear();
        final Collection<Waypoint> obj = CarpetAyakaAddition.GSON.fromJson(str, COLLECTION_TYPE);
        if (obj != null) {
            obj.forEach(this::put);
        }
    }

    public void save() throws IOException {
        LOGGER.debug("Saving waypoints to {}", waypointStoragePath);
        Files.write(waypointStoragePath, CarpetAyakaAddition.GSON.toJson(waypointMap.values(), COLLECTION_TYPE).getBytes(StandardCharsets.UTF_8));
    }

    public Waypoint get(String id) {
        return waypointMap.get(id);
    }

    public Set<String> getIDs() {
        return waypointMap.keySet();
    }

    public Collection<Waypoint> getWaypoints() {
        return waypointMap.values();
    }

    public Waypoint rename(String oldId, String newId) throws IOException {
        Waypoint w = waypointMap.remove(oldId);
        if (w == null) {
            return null;
        }
        put(new Waypoint(newId, w.getDimension(), w.getPos(), w.getDesc()));
        save();
        return w;
    }

    public Waypoint remove(String id) throws IOException {
        Waypoint w = waypointMap.remove(id);
        save();
        return w;
    }

    public Waypoint set(Waypoint waypoint) throws IOException {
        Waypoint w = put(waypoint);
        save();
        return w;
    }

}

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
import java.util.Map;
import java.util.Set;

import static com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition.LOGGER;

public class WaypointManager {

    private static final Type COLLECTION_TYPE = new TypeToken<Collection<Waypoint>>() {
    }.getType();

    private static final Map<MinecraftServer, WaypointManager> waypointManagers = new HashMap<>(1);

    private static final String WAYPOINT_FILE_NAME = "ayaka_waypoints.json";

    public static WaypointManager getOrCreateWaypointManager(MinecraftServer server) {
        if (waypointManagers.containsKey(server)) {
            return waypointManagers.get(server);
        }
        final WaypointManager instance = new WaypointManager(server);
        waypointManagers.put(server, instance);
        return instance;
    }

    public static void removeWaypointManager(MinecraftServer server) {
        if (!waypointManagers.containsKey(server)) {
            return;
        }
        try {
            waypointManagers.remove(server).saveWaypoints();
        } catch (IOException e) {
            LOGGER.error("Failed to save waypoints", e);
        }
    }

    private final Path waypointStoragePath;

    private final Map<String, Waypoint> waypoints = new HashMap<>(3);

    private WaypointManager(MinecraftServer server) {
        waypointStoragePath = ServerUtils.serverRootPath(server).resolve(WAYPOINT_FILE_NAME);
        try {
            loadWaypoints();
        } catch (IOException e) {
            LOGGER.error("Failed to load waypoints", e);
        }
    }

    public void loadWaypoints() throws IOException {
        LOGGER.debug("Loading waypoints from {}", waypointStoragePath);
        if (Files.notExists(waypointStoragePath) || !Files.isRegularFile(waypointStoragePath)) {
            Files.createFile(waypointStoragePath);
        }
        final String str = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(Files.readAllBytes(waypointStoragePath))).toString();

        waypoints.clear();
        final Collection<Waypoint> obj = CarpetAyakaAddition.GSON.fromJson(str, COLLECTION_TYPE);
        if (obj != null) {
            obj.forEach(w -> waypoints.put(w.getId(), w));
        }
    }

    public void saveWaypoints() throws IOException {
        LOGGER.debug("Saving waypoints to {}", waypointStoragePath);
        Files.write(waypointStoragePath, CarpetAyakaAddition.GSON.toJson(waypoints.values(), COLLECTION_TYPE).getBytes(StandardCharsets.UTF_8));
    }

    public Waypoint get(String id) {
        return waypoints.get(id);
    }

    public Set<String> getIds() {
        return waypoints.keySet();
    }

    public Collection<Waypoint> getWaypoints() {
        return waypoints.values();
    }

    public Waypoint rename(String oldId, String newId) throws IOException {
        Waypoint w = waypoints.remove(oldId);
        if (w == null) {
            return null;
        }
        Waypoint w2 = new Waypoint(newId, w.getDimension(), w.getPos(), w.getDesc());
        waypoints.put(newId, w2);
        saveWaypoints();
        return w;
    }

    public Waypoint remove(String id) throws IOException {
        Waypoint w = waypoints.remove(id);
        saveWaypoints();
        return w;
    }

    public Waypoint set(Waypoint waypoint) throws IOException {
        Waypoint w = waypoints.put(waypoint.getId(), waypoint);
        saveWaypoints();
        return w;
    }

}

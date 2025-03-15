package com.ayakacraft.carpetayakaaddition.commands.waypoint;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class WaypointManager {

    private static final Type collectionType = new TypeToken<Collection<Waypoint>>() {
    }.getType();

    private static final Map<MinecraftServer, WaypointManager> waypointManagers = new HashMap<>(1);

    public static WaypointManager getWaypointManager(MinecraftServer server) {
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
            waypointManagers.get(server).saveWaypoints();
        } catch (IOException e) {
            CarpetAyakaAddition.LOGGER.error("Failed to save waypoints", e);
        }
        waypointManagers.remove(server);
    }

    private final Path waypointStoragePath;

    public final Map<String, Waypoint> waypoints = new HashMap<>(3);

    private WaypointManager(MinecraftServer server) {
        final Path worldPath = server.getSavePath(WorldSavePath.ROOT);
        waypointStoragePath = worldPath.resolve("ayaka_waypoints.json");
        try {
            loadWaypoints();
        } catch (IOException e) {
            CarpetAyakaAddition.LOGGER.error("Failed to load waypoints", e);
        }
    }

    public void loadWaypoints() throws IOException {
        CarpetAyakaAddition.LOGGER.info("Loading waypoints from {}", waypointStoragePath);
        if (Files.notExists(waypointStoragePath) || !Files.isRegularFile(waypointStoragePath)) {
            Files.createFile(waypointStoragePath);
        }
        String str = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(Files.readAllBytes(waypointStoragePath))).toString();

        waypoints.clear();
        Collection<Waypoint> obj = CarpetAyakaAddition.GSON.fromJson(str, collectionType);
        (obj != null ? obj : new LinkedList<Waypoint>()).forEach(w -> waypoints.put(w.id, w));
    }

    public void saveWaypoints() throws IOException {
        CarpetAyakaAddition.LOGGER.info("Saving waypoints to {}", waypointStoragePath);
        Files.write(waypointStoragePath, CarpetAyakaAddition.GSON.toJson(waypoints.values(), collectionType).getBytes(StandardCharsets.UTF_8));
    }

}

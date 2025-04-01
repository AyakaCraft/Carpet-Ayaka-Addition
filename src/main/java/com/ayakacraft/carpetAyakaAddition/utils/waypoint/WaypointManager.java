package com.ayakacraft.carpetayakaaddition.utils.waypoint;

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

import static com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition.LOGGER;

public class WaypointManager {

    private static final Type collectionType = new TypeToken<Collection<Waypoint>>() {
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
        waypointStoragePath = server.getSavePath(WorldSavePath.ROOT).resolve(WAYPOINT_FILE_NAME);
        try {
            loadWaypoints();
        } catch (IOException e) {
            LOGGER.error("Failed to load waypoints", e);
        }
    }

    public void loadWaypoints() throws IOException {
        LOGGER.info("Loading waypoints from {}", waypointStoragePath);
        if (Files.notExists(waypointStoragePath) || !Files.isRegularFile(waypointStoragePath)) {
            Files.createFile(waypointStoragePath);
        }
        final String str = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(Files.readAllBytes(waypointStoragePath))).toString();

        waypoints.clear();
        final Collection<Waypoint> obj = CarpetAyakaAddition.GSON.fromJson(str, collectionType);
        if (obj != null) {
            obj.forEach(w -> waypoints.put(w.getId(), w));
        }
    }

    public void saveWaypoints() throws IOException {
        LOGGER.info("Saving waypoints to {}", waypointStoragePath);
        Files.write(waypointStoragePath, CarpetAyakaAddition.GSON.toJson(waypoints.values(), collectionType).getBytes(StandardCharsets.UTF_8));
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

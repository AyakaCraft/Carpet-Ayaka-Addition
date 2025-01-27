package com.ayakacraft.carpetAyakaAddition.data;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.ayakacraft.carpetAyakaAddition.CarpetAyakaAddition.GSON;

public class WaypointManager {

    private static final Type collectionType = new TypeToken<Collection<Waypoint>>() {
    }.getType();

    public static Map<String, Waypoint> waypoints = new HashMap<>(3);

    public static Path currentWaypointStoragePath;

    public static void reloadWaypoints(Path worldPath) throws IOException {
        if (worldPath != null) {
            currentWaypointStoragePath = worldPath.resolve("ayaka_waypoints.json");
        }
        if (Files.notExists(currentWaypointStoragePath) || !Files.isRegularFile(currentWaypointStoragePath)) {
            Files.deleteIfExists(currentWaypointStoragePath);
            Files.createFile(currentWaypointStoragePath);
        }
        String str = Files.readString(currentWaypointStoragePath);

        waypoints.clear();
        Objects.<Collection<Waypoint>>requireNonNullElse(GSON.fromJson(str, collectionType), List.of()).forEach(w -> waypoints.put(w.id, w));
    }

    public static void saveWaypoints() throws IOException {
        Files.writeString(currentWaypointStoragePath, GSON.toJson(waypoints.values(), collectionType));
    }

}

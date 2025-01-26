package com.ayakacraft.carpetAyakaAddition.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class WaypointManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Type listType = new TypeToken<LinkedList<Waypoint>>() {
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
        LinkedList<Waypoint> l = Objects.requireNonNullElse(GSON.fromJson(str, listType), new LinkedList<>());
        waypoints.clear();
        l.forEach(w -> waypoints.put(w.id, w));
    }

    public static void saveWaypoints() throws IOException {
        List<Waypoint> l   = waypoints.values().stream().toList();
        String str = GSON.toJson(l, listType);
        Files.writeString(currentWaypointStoragePath, str);
    }

}

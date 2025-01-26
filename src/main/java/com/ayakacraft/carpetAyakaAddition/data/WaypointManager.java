package com.ayakacraft.carpetAyakaAddition.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mojang.logging.LogUtils;
import lombok.val;
import org.slf4j.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

public class WaypointManager {

    private static final Logger LOGGER = LogUtils.getLogger();

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
        val str = Files.readString(currentWaypointStoragePath);
        val l   = Objects.requireNonNullElse(GSON.fromJson(str, listType), new LinkedList<Waypoint>());
        waypoints.clear();
        l.forEach(w -> waypoints.put(w.id, w));
    }

    public static void saveWaypoints() throws IOException {
        val l   = waypoints.values().stream().toList();
        val str = GSON.toJson(l, listType);
        Files.writeString(currentWaypointStoragePath, str);
    }

}

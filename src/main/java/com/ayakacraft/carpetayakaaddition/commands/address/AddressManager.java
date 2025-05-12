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

package com.ayakacraft.carpetayakaaddition.commands.address;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition.LOGGER;

public class AddressManager {

    private static final Type COLLECTION_TYPE = new TypeToken<Collection<Address>>() {
    }.getType();

    private static final HashMap<MinecraftServer, AddressManager> managerMap = new HashMap<>(1);

    @Deprecated
    public static final String WAYPOINT_FILE_NAME_OLD = "ayaka_waypoints.json";

    public static final String WAYPOINT_FILE_NAME = "ayaka_addresses.json";

    private static Collection<Address> loadFromPath(Path storagePath) throws IOException {
        String              str       = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(Files.readAllBytes(storagePath))).toString();
        Collection<Address> addresses = CarpetAyakaAddition.GSON.fromJson(str, COLLECTION_TYPE);
        return addresses == null ? Collections.emptyList() : addresses;
    }

    public static AddressManager getOrCreate(MinecraftServer server) {
        if (managerMap.containsKey(server)) {
            return managerMap.get(server);
        }
        AddressManager instance = new AddressManager(server);
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
            LOGGER.error("Failed to save addresses", e);
        }
    }

    @Deprecated
    private final Path waypointStoragePathOld;

    private final Path waypointStoragePath;

    private final LinkedHashMap<String, Address> addressMap = new LinkedHashMap<>(3);

    private AddressManager(MinecraftServer server) {
        waypointStoragePath = server.getSavePath(net.minecraft.util.WorldSavePath.ROOT).resolve(WAYPOINT_FILE_NAME);
        waypointStoragePathOld = server.getSavePath(net.minecraft.util.WorldSavePath.ROOT).resolve(WAYPOINT_FILE_NAME_OLD);
        try {
            load();
        } catch (IOException e) {
            LOGGER.error("Failed to load addresses", e);
        }
    }

    private Address put(Address address) {
        LOGGER.debug("Put address {}", address);
        return addressMap.put(address.getId(), address);
    }

    public void load() throws IOException {
        addressMap.clear();

        if (Files.isRegularFile(waypointStoragePathOld)) {
            LOGGER.warn("Loading addresses from {} which is deprecated", waypointStoragePathOld);
            loadFromPath(waypointStoragePathOld).forEach(this::put);
            Files.delete(waypointStoragePathOld);
        }

        if (Files.isRegularFile(waypointStoragePath)) {
            LOGGER.debug("Loading addresses from {}", waypointStoragePath);
            loadFromPath(waypointStoragePath).forEach(this::put);
        }

        save();

    }

    public void save() throws IOException {
        LOGGER.debug("Saving addresses to {}", waypointStoragePath);
        Files.write(waypointStoragePath, CarpetAyakaAddition.GSON.toJson(addressMap.values(), COLLECTION_TYPE).getBytes(StandardCharsets.UTF_8));
    }

    public Address get(String id) {
        return addressMap.get(id);
    }

    public Set<String> getIDs() {
        return addressMap.keySet();
    }

    public Collection<Address> getWaypoints() {
        return addressMap.values();
    }

    public Address rename(String oldId, String newId) throws IOException {
        Address w = addressMap.remove(oldId);
        if (w == null) {
            return null;
        }
        Address a = new Address(newId, w.getDim(), w.getPos(), w.getDesc());
        if (put(a) != null) {
            LOGGER.warn("Address named {} already exists", newId);
        }
        LOGGER.debug("Renaming {} to {}", w, a);
        save();
        return w;
    }

    public Address remove(String id) throws IOException {
        Address w = addressMap.remove(id);
        LOGGER.debug("Removed address {}", w);
        save();
        return w;
    }

    public Address set(Address address) throws IOException {
        Address w = put(address);
        LOGGER.debug("Set address {}", address);
        save();
        return w;
    }

}

/*
 * This file is part of the null project, licensed under the
 * GNU General Public License v3.0
 *
 * Copyright (C) 2026  Calboot and contributors
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

package com.ayakacraft.carpetayakaaddition.commands.endermanblocklist;

import com.ayakacraft.carpetayakaaddition.utils.FileUtils;
import com.ayakacraft.carpetayakaaddition.utils.IdentifierUtils;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.annotations.SerializedName;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;

import static com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition.GSON;
import static com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition.LOGGER;

public final class EndermanBlockListConfig {

    private static final Map<MinecraftServer, EndermanBlockListConfig> serverToConfigMap = Maps.newHashMap();

    public static final String FILE_NAME = "ayaka_enderman_block_list.json";

    private static Path getConfigPath(MinecraftServer server) {
        return server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT).resolve(FILE_NAME);
    }

    private static EndermanBlockListConfig create(MinecraftServer server) throws IOException {
        Path file = getConfigPath(server);
        LOGGER.debug("Reading enderman_block_list at '{}'", file);
        if (Files.notExists(file)) {
            Files.createFile(file);
        }
        return GSON.fromJson(FileUtils.readFile(file), EndermanBlockListConfig.class);
    }

    @Contract("null -> null; !null -> !null")
    public static EndermanBlockListConfig get(MinecraftServer server) throws IOException {
        if (server == null) {
            return null;
        }
        EndermanBlockListConfig config = serverToConfigMap.get(server);
        if (config == null) {
            config = Objects.requireNonNullElseGet(create(server), EndermanBlockListConfig::new);
            config.ensureNullSafety();
            serverToConfigMap.put(server, config);
        }
        return config;
    }

    public static void save(MinecraftServer server) throws IOException {
        EndermanBlockListConfig config = serverToConfigMap.get(server);
        if (config == null) {
            return;
        }
        Path file = getConfigPath(server);
        LOGGER.debug("Saving enderman_block_list to '{}'", file);
        FileUtils.writeFile(file, GSON.toJson(config));
    }

    public static void remove(MinecraftServer server) {
        serverToConfigMap.remove(server);
    }

    private Type type;

    private TreeSet<String> blacklist;

    private TreeSet<String> whitelist;

    private EndermanBlockListConfig() {
        this.type = Type.VANILLA;
        this.blacklist = Sets.newTreeSet();
        this.whitelist = Sets.newTreeSet();
    }

    private void ensureNullSafety() {
        if (this.type == null) {
            this.type = Type.VANILLA;
        }
        if (this.blacklist == null) {
            this.blacklist = Sets.newTreeSet();
        }
        if (this.whitelist == null) {
            this.whitelist = Sets.newTreeSet();
        }
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type == null ? Type.VANILLA : type;
    }

    public TreeSet<String> getWhitelist() {
        return whitelist;
    }

    public TreeSet<String> getBlacklist() {
        return blacklist;
    }

    public boolean verifyBlock(Block block) {
        String s = IdentifierUtils.ofBlock(block).toString();
        switch (type) {
            case DISABLE_ALL:
                return false;
            case WHITELIST:
                return getWhitelist().contains(s);
            case BLACKLIST:
            case BLACKLIST_LOOSE:
                return !getBlacklist().contains(s);
            default:
                return true;
        }
    }

    public enum Type {
        BLACKLIST,
        BLACKLIST_LOOSE,
        WHITELIST,
        DISABLE_ALL,
        @SerializedName(value = "VANILLA", alternate = "DISABLED")
        VANILLA
    }

}

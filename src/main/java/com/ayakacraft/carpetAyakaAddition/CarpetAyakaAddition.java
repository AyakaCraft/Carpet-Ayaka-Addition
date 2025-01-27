package com.ayakacraft.carpetAyakaAddition;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.ayakacraft.carpetAyakaAddition.commands.CCommand;
import com.ayakacraft.carpetAyakaAddition.commands.GoHomeCommand;
import com.ayakacraft.carpetAyakaAddition.commands.TptCommand;
import com.ayakacraft.carpetAyakaAddition.commands.WaypointCommand;
import com.ayakacraft.carpetAyakaAddition.data.WaypointManager;
import com.ayakacraft.carpetAyakaAddition.event.AfterBlockBreakHandler;
import com.ayakacraft.carpetAyakaAddition.mixin.LevelStorageSessionAccessor;
import com.ayakacraft.carpetAyakaAddition.mixin.MinecraftServerAccessor;
import com.ayakacraft.carpetAyakaAddition.stats.Statistics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

public class CarpetAyakaAddition implements ModInitializer, CarpetExtension {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().create();

    public static final String MOD_ID = "carpet-ayaka-addition";

    public static final String MOD_NAME = "Carpet Ayaka Addition";

    public static final String MOD_VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata().getVersion().toString();

    public static Identifier identifier(String path) {
        return Identifier.of(MOD_ID, path);
    }

    public MinecraftServer mcServer;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing {} Version {}", MOD_NAME, MOD_VERSION);
        Statistics.registerAll();
        registerEvents();

        CarpetServer.manageExtension(new CarpetAyakaAddition());
    }

    @Override
    public void onServerLoaded(MinecraftServer server) {
        this.mcServer = server;
        CarpetServer.settingsManager.parseSettingsClass(CarpetAyakaSettings.class);
    }

    @Override
    public void onServerLoadedWorlds(MinecraftServer server) {
        final Path worldPath = ((LevelStorageSessionAccessor) ((MinecraftServerAccessor) server).getSession()).getDirectory().path();

        try {
            WaypointManager.reloadWaypoints(worldPath);
        } catch (IOException e) {
            LOGGER.error("Failed to load waypoints", e);
        }
    }

    @Override
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, final CommandRegistryAccess commandBuildContext) {
        TptCommand.register(dispatcher);
        GoHomeCommand.register(dispatcher);
        WaypointCommand.register(dispatcher);
        CCommand.register(dispatcher);
    }

    @Override
    public String version() {
        return MOD_ID;
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        final InputStream langStream = CarpetAyakaAddition.class.getClassLoader().getResourceAsStream("assets/carpet-ayaka-addition/lang/%s.json".formatted(lang));
        if (langStream == null) {
            // we don't have that language
            return Collections.emptyMap();
        }
        final String jsonData;
        try {
            jsonData = new String(langStream.readAllBytes(), StandardCharsets.UTF_8);
            langStream.close();
        } catch (final IOException e) {
            return Collections.emptyMap();
        }
        return GSON.fromJson(jsonData, new TypeToken<Map<String, String>>() {
        }.getType());
    }

    public void registerEvents() {
        PlayerBlockBreakEvents.AFTER.register(new AfterBlockBreakHandler());
//        UseBlockCallback.EVENT.register(new UseBlockHandler());
    }


}

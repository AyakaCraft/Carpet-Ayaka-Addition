package com.ayakacraft.carpetAyakaAddition;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.CarpetSettings;
import com.ayakacraft.carpetAyakaAddition.commands.GoHomeCommand;
import com.ayakacraft.carpetAyakaAddition.commands.TptCommand;
import com.ayakacraft.carpetAyakaAddition.commands.WaypointCommand;
import com.ayakacraft.carpetAyakaAddition.data.WaypointManager;
import com.ayakacraft.carpetAyakaAddition.mixin.LevelStorageSessionAccessor;
import com.ayakacraft.carpetAyakaAddition.mixin.MinecraftServerAccessor;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import lombok.val;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import org.slf4j.Logger;

import java.io.IOException;

public class CarpetAyakaAddition implements ModInitializer, CarpetExtension {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String MOD_ID = "carpet-ayaka-addition";

    public static final String MOD_NAME = "Carpet Ayaka Addition";

    public static final String MOD_VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata().getVersion().toString();

    public MinecraftServer mcServer;

    @Override
    public void onInitialize() {
        CarpetServer.manageExtension(new CarpetAyakaAddition());
    }

    @Override
    public void onServerLoaded(MinecraftServer server) {
        this.mcServer = server;
        CarpetServer.settingsManager.parseSettingsClass(CarpetSettings.class);
    }

    @Override
    public void onServerLoadedWorlds(MinecraftServer server) {
        val worldPath = ((LevelStorageSessionAccessor) ((MinecraftServerAccessor) server).getSession()).getDirectory().path();

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
    }

    @Override
    public String version() {
        return MOD_ID;
    }

}
